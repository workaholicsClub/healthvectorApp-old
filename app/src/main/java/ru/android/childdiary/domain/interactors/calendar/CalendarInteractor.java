package ru.android.childdiary.domain.interactors.calendar;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.calendar.CalendarDataRepository;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.core.Interactor;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.calendar.requests.AddDiaperRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.AddFeedRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.AddOtherRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.AddPumpRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.AddSleepRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.EventsRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.EventsResponse;

public class CalendarInteractor implements Interactor {
    private final Logger logger = LoggerFactory.getLogger(toString());

    private final CalendarDataRepository calendarRepository;

    @Inject
    public CalendarInteractor(CalendarDataRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    public Observable<LocalDate> getSelectedDate() {
        return calendarRepository.getSelectedDate();
    }

    public Observable<LocalDate> getSelectedDateOnce() {
        return calendarRepository.getSelectedDate().first(LocalDate.now()).toObservable();
    }

    public Observable<LocalDate> setSelectedDate(@NonNull LocalDate date) {
        return calendarRepository.setSelectedDate(date);
    }

    public Observable<EventsResponse> getAll(@NonNull EventsRequest request) {
        return calendarRepository.getAll(request.getChild(), request.getDate())
                .map(events -> EventsResponse.builder().request(request).events(events).build());
    }

    public Observable<DiaperEvent> getDiaperEventDetail(@NonNull MasterEvent event) {
        return calendarRepository.getDiaperEventDetail(event);
    }

    public Observable<FeedEvent> getFeedEventDetail(@NonNull MasterEvent event) {
        return calendarRepository.getFeedEventDetail(event);
    }

    public Observable<OtherEvent> getOtherEventDetail(@NonNull MasterEvent event) {
        return calendarRepository.getOtherEventDetail(event);
    }

    public Observable<PumpEvent> getPumpEventDetail(@NonNull MasterEvent event) {
        return calendarRepository.getPumpEventDetail(event);
    }

    public Observable<SleepEvent> getSleepEventDetail(@NonNull MasterEvent event) {
        return calendarRepository.getSleepEventDetail(event);
    }

    public Observable<DiaperEvent> add(@NonNull AddDiaperRequest request) {
        DiaperEvent event = request.getEvent().toBuilder().eventType(EventType.DIAPER).build();
        return calendarRepository.add(request.getChild(), event);
    }

    public Observable<FeedEvent> add(@NonNull AddFeedRequest request) {
        FeedEvent event = request.getEvent().toBuilder().eventType(EventType.FEED).build();
        return calendarRepository.add(request.getChild(), event);
    }

    public Observable<OtherEvent> add(@NonNull AddOtherRequest request) {
        OtherEvent event = request.getEvent().toBuilder().eventType(EventType.OTHER).build();
        return calendarRepository.add(request.getChild(), event);
    }

    public Observable<PumpEvent> add(@NonNull AddPumpRequest request) {
        PumpEvent event = request.getEvent().toBuilder().eventType(EventType.PUMP).build();
        return calendarRepository.add(request.getChild(), event);
    }

    public Observable<SleepEvent> add(@NonNull AddSleepRequest request) {
        SleepEvent event = request.getEvent().toBuilder().eventType(EventType.SLEEP).build();
        return calendarRepository.add(request.getChild(), event);
    }

    public Observable<DiaperEvent> update(@NonNull DiaperEvent event) {
        return calendarRepository.update(event);
    }

    public Observable<FeedEvent> update(@NonNull FeedEvent event) {
        return calendarRepository.update(event);
    }

    public Observable<OtherEvent> update(@NonNull OtherEvent event) {
        return calendarRepository.update(event);
    }

    public Observable<PumpEvent> update(@NonNull PumpEvent event) {
        return calendarRepository.update(event);
    }

    public Observable<SleepEvent> update(@NonNull SleepEvent event) {
        return calendarRepository.update(event);
    }

    public Observable<MasterEvent> delete(@NonNull MasterEvent event) {
        return calendarRepository.delete(event);
    }

    public Observable<MasterEvent> done(@NonNull MasterEvent event) {
        return calendarRepository.done(event);
    }
}
