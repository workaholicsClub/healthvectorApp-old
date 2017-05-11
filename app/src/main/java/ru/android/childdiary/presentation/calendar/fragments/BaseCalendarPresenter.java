package ru.android.childdiary.presentation.calendar.fragments;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import org.joda.time.LocalDate;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.CalendarInteractor;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.requests.EventsRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.EventsResponse;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class BaseCalendarPresenter extends BasePresenter<BaseCalendarView> {
    private final EventsRequest.EventsRequestBuilder requestBuilder = EventsRequest.builder();

    @Inject
    ChildInteractor childInteractor;

    @Inject
    CalendarInteractor calendarInteractor;

    private Disposable subscription;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(Observable.combineLatest(
                calendarInteractor.getSelectedDate(),
                childInteractor.getActiveChild(),
                (date, child) -> EventsRequest.builder().date(date).child(child).build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetRequest, this::onUnexpectedError));
    }

    private void onGetRequest(@NonNull EventsRequest request) {
        logger.debug("onGetRequest: " + request);
        getViewState().showChild(request.getChild());
        getViewState().setSelectedDate(request.getDate());
        requestBuilder.date(request.getDate()).child(request.getChild());
        requestData();
    }

    private void requestData() {
        unsubscribe(subscription);
        subscription = unsubscribeOnDestroy(calendarInteractor.getAll(requestBuilder.build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetData, this::onUnexpectedError));
    }

    private void onGetData(@NonNull EventsResponse response) {
        logger.debug("onGetData: " + response);
        getViewState().showEvents(response.getRequest().getDate(), response.getEvents());
    }

    public void switchDate(@NonNull LocalDate date) {
        logger.debug("user switch date: " + date);
        unsubscribeOnDestroy(calendarInteractor.setSelectedDateObservable(date)
                .ignoreElements()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, this::onUnexpectedError));
    }

    public void delete(@NonNull MasterEvent event) {
        unsubscribeOnDestroy(calendarInteractor.delete(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deletedEvent -> logger.debug("event deleted: " + deletedEvent), this::onUnexpectedError));
    }

    public void move(@NonNull MasterEvent event) {
    }

    public void requestEventDetail(@NonNull MasterEvent event) {
        switch (event.getEventType()) {
            case DIAPER:
                unsubscribeOnDestroy(calendarInteractor.getDefaultDiaperEvent()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(defaultEvent -> getViewState().navigateToDiaperEvent(event, defaultEvent),
                                this::onUnexpectedError));
                break;
            case FEED:
                unsubscribeOnDestroy(calendarInteractor.getDefaultFeedEvent()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(defaultEvent -> getViewState().navigateToFeedEvent(event, defaultEvent),
                                this::onUnexpectedError));
                break;
            case OTHER:
                unsubscribeOnDestroy(calendarInteractor.getDefaultOtherEvent()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(defaultEvent -> getViewState().navigateToOtherEvent(event, defaultEvent),
                                this::onUnexpectedError));
                break;
            case PUMP:
                unsubscribeOnDestroy(calendarInteractor.getDefaultPumpEvent()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(defaultEvent -> getViewState().navigateToPumpEvent(event, defaultEvent),
                                this::onUnexpectedError));
                break;
            case SLEEP:
                unsubscribeOnDestroy(calendarInteractor.getDefaultSleepEvent()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(defaultEvent -> getViewState().navigateToSleepEvent(event, defaultEvent),
                                this::onUnexpectedError));
                break;
            case DOCTOR_VISIT:
                unsubscribeOnDestroy(calendarInteractor.getDefaultDoctorVisitEvent()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(defaultEvent -> getViewState().navigateToDoctorVisitEvent(event, defaultEvent),
                                this::onUnexpectedError));
                break;
            case MEDICINE_TAKING:
                unsubscribeOnDestroy(calendarInteractor.getDefaultMedicineTakingEvent()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(defaultEvent -> getViewState().navigateToMedicineTakingEvent(event, defaultEvent),
                                this::onUnexpectedError));
                break;
            // TODO EXERCISE
        }
    }

    public void done(@NonNull MasterEvent event) {
        unsubscribeOnDestroy(calendarInteractor.done(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(doneEvent -> logger.debug("event done: " + doneEvent), this::onUnexpectedError));
    }
}
