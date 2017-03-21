package ru.android.childdiary.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.app.ChildDiaryApplication;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.CalendarInteractor;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.utils.log.LogSystem;

public class TimerService extends Service {
    private final Logger logger = LoggerFactory.getLogger(toString());
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final TimerServiceBinder binder = new TimerServiceBinder(this);
    private final NotificationManager notificationManager = new NotificationManager();

    @Inject
    CalendarInteractor calendarInteractor;

    private Timer timer;

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        logger.debug("onStartCommand: " + intent);

        unsubscribeOnDestroy(calendarInteractor.getSleepEventsWithTimer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResult, this::onUnexpectedError));

        return START_STICKY;
    }

    private void handleResult(@NonNull List<SleepEvent> events) {
        notificationManager.updateNotifications(this, events);
        if (events.isEmpty()) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    private void onUnexpectedError(Throwable e) {
        LogSystem.report(logger, "unexpected error", e);
        stopTimer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        logger.debug("onBind: " + intent);
        return binder;
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

        stopTimer();
        compositeDisposable.dispose();
    }

    private void startTimer() {
        if (timer == null) {
            Handler handler = new Handler(getMainLooper());
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    handler.post(TimerService.this::timerTick);
                }
            }, 0, 1000);
        }
    }

    private void timerTick() {
        notificationManager.updateNotifications(this);
        binder.onTimerTick();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private void unsubscribeOnDestroy(@NonNull Disposable disposable) {
        compositeDisposable.add(disposable);
    }
}
