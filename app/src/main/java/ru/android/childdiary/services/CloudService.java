package ru.android.childdiary.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.R;
import ru.android.childdiary.app.ChildDiaryApplication;
import ru.android.childdiary.data.availability.NetworkAvailability;
import ru.android.childdiary.data.availability.exceptions.NetworkUnavailableException;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.cloud.CloudInteractor;
import ru.android.childdiary.domain.interactors.core.settings.SettingsInteractor;
import ru.android.childdiary.utils.log.LogSystem;
import ru.android.childdiary.utils.strings.DateUtils;
import ru.android.childdiary.utils.ui.NotificationHelper;

public class CloudService extends Service {
    private static final int NOTIFICATION_ID_ERROR = -1;
    private static final int NOTIFICATION_ID_BACKUP = -2;

    private final Logger logger = LoggerFactory.getLogger(toString());

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    NetworkAvailability networkAvailability;

    @Inject
    SettingsInteractor settingsInteractor;

    @Inject
    CloudInteractor cloudInteractor;

    @Inject
    NotificationHelper notificationHelper;

    public static void installAlarm(Context context) {
        PendingIntent pendingIntent = getPendingIntent(context);

        DateTime dateTime = DateUtils.nextDayMidnight(LocalDate.now());
        long midnightMillis = dateTime.getMillis();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, midnightMillis, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, CloudService.class);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        logger.debug("onStartCommand: " + intent);

        handleIntent(intent);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        logger.debug("onBind: " + intent);
        return null;
    }

    @Override
    public void onCreate() {
        logger.debug("onCreate");

        ApplicationComponent component = ChildDiaryApplication.getApplicationComponent();
        component.inject(this);
    }

    @Override
    public void onDestroy() {
        logger.debug("onDestroy");

        compositeDisposable.dispose();

        notificationHelper.hideNotification(NOTIFICATION_ID_BACKUP);
    }

    private void handleIntent(@Nullable Intent intent) {
        unsubscribeOnDestroy(
                settingsInteractor.getAccountNameOnce()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(accountName -> {
                                    if (TextUtils.isEmpty(accountName)) {
                                        logger.debug("account name is empty");
                                    } else {
                                        backup();
                                    }
                                },
                                this::onUnexpectedError));
    }

    private void backup() {
        notificationHelper.showBackupProgressNotification(NOTIFICATION_ID_BACKUP);
        unsubscribeOnDestroy(
                networkAvailability.checkNetworkAvailability(true)
                        .flatMap(isNetworkAvailable -> cloudInteractor.backup())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(throwable -> logger.error("backup", throwable))
                        .subscribe(
                                this::handleResult,
                                throwable -> {
                                    boolean processed = processGoogleDriveError(throwable);
                                    if (!processed) {
                                        logger.error("backup failed", throwable);
                                    }
                                    stopSelf();
                                }
                        ));
    }

    private void handleResult(boolean isUploaded) {
        logger.debug("backup succeeded");
        stopSelf();
    }

    private boolean processGoogleDriveError(Throwable throwable) {
        if (throwable instanceof GooglePlayServicesAvailabilityIOException) {
            int connectionStatusCode = ((GooglePlayServicesAvailabilityIOException) throwable).getConnectionStatusCode();
            PendingIntent pendingIntent = null;
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
                pendingIntent = apiAvailability.getErrorResolutionPendingIntent(this, connectionStatusCode, 0);
            }
            notificationHelper.showBackupErrorNotification(NOTIFICATION_ID_ERROR,
                    getString(R.string.notification_title_play_services),
                    getString(R.string.notification_text_play_services),
                    pendingIntent);
            return true;
        } else if (throwable instanceof UserRecoverableAuthIOException) {
            Intent intent = ((UserRecoverableAuthIOException) throwable).getIntent();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            notificationHelper.showBackupErrorNotification(NOTIFICATION_ID_ERROR,
                    getString(R.string.notification_title_authorization),
                    getString(R.string.notification_text_authorization_recoverable),
                    pendingIntent);
            return true;
        } else if (throwable instanceof NetworkUnavailableException) {
            PendingIntent pendingIntent = getPendingIntent(this);
            notificationHelper.showBackupErrorNotification(NOTIFICATION_ID_ERROR,
                    getString(R.string.notification_title_network),
                    getString(R.string.notification_text_network),
                    pendingIntent);
            return true;
        } else if (throwable instanceof SecurityException) {
            PendingIntent pendingIntent = getPendingIntent(this);
            notificationHelper.showBackupErrorNotification(NOTIFICATION_ID_ERROR,
                    getString(R.string.notification_title_authorization),
                    getString(R.string.notification_text_authorization_unrecoverable),
                    pendingIntent);
            return true;
        }
        return false;
    }

    private void onUnexpectedError(Throwable e) {
        LogSystem.report(logger, "unexpected error", e);

        stopSelf();
    }

    private Disposable unsubscribeOnDestroy(@NonNull Disposable disposable) {
        compositeDisposable.add(disposable);
        return disposable;
    }
}
