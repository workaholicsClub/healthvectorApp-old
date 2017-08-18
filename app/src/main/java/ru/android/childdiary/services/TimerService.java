package ru.android.childdiary.services;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.calendar.CalendarInteractor;
import ru.android.childdiary.domain.calendar.data.standard.SleepEvent;
import ru.android.childdiary.domain.calendar.requests.GetSleepEventsRequest;
import ru.android.childdiary.domain.calendar.requests.GetSleepEventsResponse;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.services.core.BaseService;
import ru.android.childdiary.utils.log.LogSystem;
import ru.android.childdiary.utils.notifications.SleepEventNotificationHelper;
import ru.android.childdiary.utils.strings.EventUtils;

public class TimerService extends BaseService {
    public static final String ACTION_STOP_SLEEP_EVENT_TIMER = "TimerService.ACTION_STOP_SLEEP_EVENT_TIMER";
    public static final String ACTION_RESUBSCRIBE_TIMER = "TimerService.ACTION_RESUBSCRIBE_TIMER";
    private static final String EXTRA_ACTION = "TimerService.EXTRA_ACTION";
    private static final long TIMER_PERIOD = 1000;

    @Getter
    private final TimerServiceBinder binder = new TimerServiceBinder(this);

    @SuppressLint("UseSparseArrays")
    private final Map<Long, NotificationCompat.Builder> notificationBuilders = new HashMap<>();

    @Inject
    CalendarInteractor calendarInteractor;

    @Inject
    SleepEventNotificationHelper sleepEventNotificationHelper;

    private Disposable subscription;
    private Handler handler;
    private List<SleepEvent> events = new ArrayList<>();

    private static Intent getServiceIntent(Context context,
                                           @Nullable String action,
                                           @Nullable SleepEvent event) {
        return new Intent(context, TimerService.class)
                .putExtra(EXTRA_ACTION, action)
                .putExtra(ExtraConstants.EXTRA_EVENT, event);
    }

    public static void startService(Context context,
                                    @Nullable String action,
                                    @Nullable SleepEvent event) {
        Intent intent = getServiceIntent(context, action, event);
        context.startService(intent);
    }

    public static PendingIntent getPendingIntent(int requestCode, Context context,
                                                 @Nullable String action,
                                                 @Nullable SleepEvent event) {
        Intent intent = getServiceIntent(context, action, event)
                .setAction(String.valueOf(SystemClock.elapsedRealtime()));
        return PendingIntent.getService(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onCreate(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
        subscribeOnUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clear();
    }

    @Override
    protected void handleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }

        String action = intent.getStringExtra(EXTRA_ACTION);
        if (action == null) {
            return;
        }

        switch (action) {
            case ACTION_STOP_SLEEP_EVENT_TIMER:
                SleepEvent event = (SleepEvent) intent.getSerializableExtra(ExtraConstants.EXTRA_EVENT);
                if (event == null) {
                    return;
                }

                stopSleepEventTimer(event);
                break;
            case ACTION_RESUBSCRIBE_TIMER:
                subscribeOnUpdates();
                break;
        }
    }

    private void subscribeOnUpdates() {
        unsubscribe(subscription);
        subscription = unsubscribeOnDestroy(
                calendarInteractor.getSleepEvents(
                        GetSleepEventsRequest.builder()
                                .child(null)
                                .withStartedTimer(true)
                                .build())
                        .map(GetSleepEventsResponse::getEvents)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleResult, this::onUnexpectedError));
    }

    private void stopSleepEventTimer(@NonNull SleepEvent event) {
        DateTime now = DateTime.now();
        unsubscribeOnDestroy(
                calendarInteractor.getEventDetailOnce(event)
                        .map(sleepEvent -> (SleepEvent) sleepEvent)
                        .map(sleepEvent -> sleepEvent.toBuilder()
                                .isTimerStarted(false)
                                .finishDateTime(now.isAfter(sleepEvent.getDateTime()) ? now : null)
                                .build())
                        .flatMap(calendarInteractor::update)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(stoppedEvent -> logger.debug("event stopped: " + stoppedEvent), this::onUnexpectedError));
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
        clear();
    }

    private void clear() {
        updateNotifications(this, Collections.emptyList());
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
        //noinspection Convert2streamapi
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
            sleepEventNotificationHelper.hideNotification(event);
        }
        events = new ArrayList<>(newEvents);
    }

    private void removeAll(List<SleepEvent> from, List<SleepEvent> what) {
        for (SleepEvent whatEvent : what) {
            SleepEvent found = null;
            for (SleepEvent fromEvent : from) {
                if (EventUtils.sameEvent(whatEvent, fromEvent)) {
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
                SleepEvent defaultEvent = calendarInteractor.getDefaultSleepEvent().blockingFirst();
                builder = sleepEventNotificationHelper.buildSleepNotification(event, defaultEvent);
                notificationBuilders.put(masterEventId, builder);
            } else {
                sleepEventNotificationHelper.updateSleepNotification(builder, event);
            }
            sleepEventNotificationHelper.showNotification(event, builder);
        }
    }
}
