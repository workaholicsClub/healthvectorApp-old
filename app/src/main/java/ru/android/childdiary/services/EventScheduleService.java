package ru.android.childdiary.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.db.exceptions.DowngradeDatabaseException;
import ru.android.childdiary.data.services.ScheduleHelper;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.calendar.CalendarInteractor;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.calendar.requests.GetEventsFilter;
import ru.android.childdiary.domain.calendar.requests.GetEventsRequest;
import ru.android.childdiary.domain.calendar.requests.GetEventsResponse;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.receivers.EventNotificationReceiver;
import ru.android.childdiary.services.core.BaseService;
import ru.android.childdiary.utils.log.LogSystem;
import ru.android.childdiary.utils.strings.DateUtils;

public class EventScheduleService extends BaseService {
    @Inject
    CalendarInteractor calendarInteractor;

    @Inject
    ScheduleHelper scheduleHelper;

    private Disposable subscription;

    private static Intent getServiceIntent(Context context) {
        Intent intent = new Intent(context, EventScheduleService.class);
        intent.setAction(String.valueOf(SystemClock.elapsedRealtime()));
        return intent;
    }

    public static void startService(Context context) {
        Intent intent = getServiceIntent(context);
        context.startService(intent);
    }

    public static PendingIntent getPendingIntent(int requestCode, Context context) {
        Intent intent = getServiceIntent(context);
        return PendingIntent.getService(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Nullable
    @Override
    protected IBinder getBinder() {
        return null;
    }

    @Override
    protected void onCreate(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
        subscribeOnUpdates();
    }

    @Override
    protected void handleIntent(@Nullable Intent intent) {
        LocalDate today = LocalDate.now();
        DateTime midnight = DateUtils.nextDayMidnight(today);
        scheduleHelper.installAlarm(getPendingIntent(0, this), midnight.getMillis());
        subscribeOnUpdates();
    }

    private void subscribeOnUpdates() {
        unsubscribe(subscription);
        try {
            DateTime now = DateTime.now();
            subscription = unsubscribeOnDestroy(
                    calendarInteractor.getAll(
                            GetEventsRequest.builder()
                                    .date(now.toLocalDate())
                                    .filter(GetEventsFilter.builder()
                                            .eventTypes(Arrays.asList(EventType.values()))
                                            .build())
                                    .child(Child.NULL)
                                    .build())
                            .map(GetEventsResponse::getEvents)
                            .flatMap(Observable::fromIterable)
                            .filter(event -> event.getMasterEventId() != null
                                    && event.getDateTime() != null
                                    && event.getDateTime().isAfter(now))
                            .map(event -> {
                                MasterEvent defaultEvent = calendarInteractor.getDefaultEvent(event).blockingFirst();
                                return Arrays.asList(event, defaultEvent);
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::handleResult, this::onUnexpectedError));
        } catch (DowngradeDatabaseException e) {
            logger.error("Can't subscribe to updates", e);
        }
    }

    private void handleResult(@NonNull List<MasterEvent> events) {
        MasterEvent event = events.get(0);
        MasterEvent defaultEvent = events.get(1);
        logger.debug("schedule event: " + event);
        // нельзя шедулить больше 500 будильников, по крайней мере, на самсунге
        Long masterEventId = event.getMasterEventId();
        int requestCode = (int) (masterEventId % Integer.MAX_VALUE);
        Intent intent = new Intent(this, EventNotificationReceiver.class);
        intent.putExtra(ExtraConstants.EXTRA_EVENT, event);
        intent.putExtra(ExtraConstants.EXTRA_DEFAULT_EVENT, defaultEvent);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        scheduleHelper.installAlarm(pendingIntent, event.getDateTime().getMillis());
    }

    private void onUnexpectedError(Throwable e) {
        LogSystem.report(logger, "unexpected error", e);
    }
}
