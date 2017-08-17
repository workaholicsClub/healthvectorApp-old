package ru.android.childdiary.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.R;
import ru.android.childdiary.data.availability.NetworkAvailability;
import ru.android.childdiary.data.availability.exceptions.NetworkUnavailableException;
import ru.android.childdiary.data.services.ScheduleHelper;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.cloud.CloudInteractor;
import ru.android.childdiary.domain.core.settings.SettingsInteractor;
import ru.android.childdiary.services.core.BaseService;
import ru.android.childdiary.utils.log.LogSystem;
import ru.android.childdiary.utils.notifications.CloudNotificationHelper;
import ru.android.childdiary.utils.strings.DateUtils;

public class CloudService extends BaseService {
    @Inject
    NetworkAvailability networkAvailability;

    @Inject
    SettingsInteractor settingsInteractor;

    @Inject
    CloudInteractor cloudInteractor;

    @Inject
    ScheduleHelper scheduleHelper;

    @Inject
    CloudNotificationHelper cloudNotificationHelper;

    private static Intent getServiceIntent(Context context) {
        return new Intent(context, CloudService.class);
    }

    public static void startService(Context context) {
        Intent intent = getServiceIntent(context);
        context.startService(intent);
    }

    public static PendingIntent getPendingIntent(int requestCode, Context context) {
        Intent intent = getServiceIntent(context);
        return PendingIntent.getService(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void reschedule(ScheduleHelper scheduleHelper, Context context) {
        LocalDate today = LocalDate.now();
        DateTime midnight = DateUtils.nextDayMidnight(today);
        scheduleHelper.installExactAlarm(getPendingIntent(0, context), midnight.getMillis());
    }

    @Nullable
    @Override
    protected IBinder getBinder() {
        return null;
    }

    @Override
    protected void onCreate(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cloudNotificationHelper.hideBackupProgressNotification();
    }

    @Override
    protected void handleIntent(@Nullable Intent intent) {
        reschedule(scheduleHelper, this);
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
        cloudNotificationHelper.showBackupProgressNotification();
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
            cloudNotificationHelper.showBackupErrorNotification(
                    getString(R.string.notification_title_play_services),
                    getString(R.string.notification_text_play_services),
                    pendingIntent);
            return true;
        } else if (throwable instanceof UserRecoverableAuthIOException) {
            Intent intent = ((UserRecoverableAuthIOException) throwable).getIntent();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            cloudNotificationHelper.showBackupErrorNotification(
                    getString(R.string.notification_title_authorization),
                    getString(R.string.notification_text_authorization_recoverable),
                    pendingIntent);
            return true;
        } else if (throwable instanceof NetworkUnavailableException) {
            PendingIntent pendingIntent = getPendingIntent(0, this);
            cloudNotificationHelper.showBackupErrorNotification(
                    getString(R.string.notification_title_network),
                    getString(R.string.notification_text_network),
                    pendingIntent);
            return true;
        } else if (throwable instanceof SecurityException) {
            PendingIntent pendingIntent = getPendingIntent(0, this);
            cloudNotificationHelper.showBackupErrorNotification(
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
}
