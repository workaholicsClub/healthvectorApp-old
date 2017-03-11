package ru.android.childdiary.presentation.events.core;

import com.arellomobile.mvp.InjectViewState;

import org.joda.time.LocalTime;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.CalendarInteractor;
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
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class EventDetailPresenter extends BasePresenter<EventDetailView> {
    @Inject
    ChildInteractor childInteractor;

    @Inject
    CalendarInteractor calendarInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void addDiaperEvent() {
        unsubscribeOnDestroy(Observable.combineLatest(
                calendarInteractor.getSelectedDateOnce().map(date -> DiaperEvent.builder().dateTime(date.toDateTime(LocalTime.now())).build()),
                childInteractor.getActiveChildOnce(),
                (event, child) -> AddDiaperRequest.builder().event(event).child(child).build())
                .flatMap(request -> calendarInteractor.add(request))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(addedEvent -> logger.debug("event added: " + addedEvent))
                .subscribe(getViewState()::eventAdded, this::onUnexpectedError));
    }

    public void addSleepEvent() {
        unsubscribeOnDestroy(Observable.combineLatest(
                calendarInteractor.getSelectedDateOnce().map(date -> SleepEvent.builder().dateTime(date.toDateTime(LocalTime.now())).build()),
                childInteractor.getActiveChildOnce(),
                (event, child) -> AddSleepRequest.builder().event(event).child(child).build())
                .flatMap(request -> calendarInteractor.add(request))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(addedEvent -> logger.debug("event added: " + addedEvent))
                .subscribe(getViewState()::eventAdded, this::onUnexpectedError));
    }

    public void addFeedEvent() {
        unsubscribeOnDestroy(Observable.combineLatest(
                calendarInteractor.getSelectedDateOnce().map(date -> FeedEvent.builder().dateTime(date.toDateTime(LocalTime.now())).build()),
                childInteractor.getActiveChildOnce(),
                (event, child) -> AddFeedRequest.builder().event(event).child(child).build())
                .flatMap(request -> calendarInteractor.add(request))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(addedEvent -> logger.debug("event added: " + addedEvent))
                .subscribe(getViewState()::eventAdded, this::onUnexpectedError));
    }

    public void addPumpEvent() {
        unsubscribeOnDestroy(Observable.combineLatest(
                calendarInteractor.getSelectedDateOnce().map(date -> PumpEvent.builder().dateTime(date.toDateTime(LocalTime.now())).build()),
                childInteractor.getActiveChildOnce(),
                (event, child) -> AddPumpRequest.builder().event(event).child(child).build())
                .flatMap(request -> calendarInteractor.add(request))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(addedEvent -> logger.debug("event added: " + addedEvent))
                .subscribe(getViewState()::eventAdded, this::onUnexpectedError));
    }

    public void addOtherEvent() {
        unsubscribeOnDestroy(Observable.combineLatest(
                calendarInteractor.getSelectedDateOnce().map(date -> OtherEvent.builder().dateTime(date.toDateTime(LocalTime.now())).build()),
                childInteractor.getActiveChildOnce(),
                (event, child) -> AddOtherRequest.builder().event(event).child(child).build())
                .flatMap(request -> calendarInteractor.add(request))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(addedEvent -> logger.debug("event added: " + addedEvent))
                .subscribe(getViewState()::eventAdded, this::onUnexpectedError));
    }
}
