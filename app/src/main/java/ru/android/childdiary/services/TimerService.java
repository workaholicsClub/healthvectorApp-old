package ru.android.childdiary.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.app.ChildDiaryApplication;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.CalendarInteractor;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.utils.ObjectUtils;
import ru.android.childdiary.utils.log.LogSystem;
import ru.android.childdiary.utils.ui.NotificationUtils;

public class TimerService extends Service {
    private static final long TIMER_PERIOD = 1000;
    private final Logger logger = LoggerFactory.getLogger(toString());

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final TimerServiceBinder binder = new TimerServiceBinder(this);
    @Inject
    CalendarInteractor calendarInteractor;
    private Map<Long, NotificationCompat.Builder> notificationBuilders = new HashMap<>();
    private List<SleepEvent> events = new ArrayList<>();
    private Handler handler;

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        logger.debug("onStartCommand: " + intent);

        return START_STICKY;
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
        unsubscribeOnDestroy(calendarInteractor.getSleepEventsWithTimer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResult, this::onUnexpectedError));
    }

    @Override
    public void onDestroy() {
        logger.debug("onDestroy");

        compositeDisposable.dispose();

        updateNotifications(this, Collections.emptyList());
        stopTimer();
    }

    private void unsubscribeOnDestroy(@NonNull Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    private void handleResult(@NonNull List<SleepEvent> events) {
        updateNotifications(this, events);
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

    private void startTimer() {
        if (handler == null) {
            handler = new Handler(getMainLooper());
            handler.postDelayed(this::timerTick, TIMER_PERIOD);
        }
    }

    private void timerTick() {
        if (handler == null) {
            logger.debug("timer is already stopped");
            return;
        }
        updateNotifications(this);
        for (SleepEvent event : events) {
            binder.onTimerTick(event);
        }
        handler.postDelayed(this::timerTick, TIMER_PERIOD);
    }

    private void stopTimer() {
        if (handler == null) {
            logger.debug("timer is already stopped");
            return;
        }
        handler.removeCallbacks(this::timerTick);
        handler = null;
    }

    private void updateNotifications(Context context, List<SleepEvent> newEvents) {
        removeAll(events, newEvents);
        for (SleepEvent event : events) {
            Long masterEventId = event.getMasterEventId();
            NotificationUtils.hideNotification(context, (int) (masterEventId % Integer.MAX_VALUE));
        }
        events = new ArrayList<>(newEvents);
    }

    private void removeAll(List<SleepEvent> from, List<SleepEvent> what) {
        for (SleepEvent whatEvent : what) {
            SleepEvent found = null;
            for (SleepEvent fromEvent : from) {
                if (ObjectUtils.equals(whatEvent.getId(), fromEvent.getId())) {
                    found = fromEvent;
                    break;
                }
            }
            if (found != null) {
                from.remove(found);
            }
        }
    }

    private void updateNotifications(Context context) {
        for (SleepEvent event : events) {
            Long masterEventId = event.getMasterEventId();
            NotificationCompat.Builder builder = notificationBuilders.get(masterEventId);
            if (builder == null) {
                builder = NotificationUtils.buildNotification(context, event);
                notificationBuilders.put(masterEventId, builder);
            } else {
                NotificationUtils.updateNotification(context, builder, event);
            }
            NotificationUtils.showNotification(context, (int) (masterEventId % Integer.MAX_VALUE), builder);
        }
    }
}
