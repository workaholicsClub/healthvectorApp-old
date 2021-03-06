package ru.android.healthvector.presentation.calendar.partitions;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import org.joda.time.LocalDate;

import java.util.Arrays;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.calendar.CalendarInteractor;
import ru.android.healthvector.domain.calendar.data.DoctorVisitEvent;
import ru.android.healthvector.domain.calendar.data.ExerciseEvent;
import ru.android.healthvector.domain.calendar.data.MedicineTakingEvent;
import ru.android.healthvector.domain.calendar.data.core.MasterEvent;
import ru.android.healthvector.domain.calendar.data.standard.DiaperEvent;
import ru.android.healthvector.domain.calendar.data.standard.FeedEvent;
import ru.android.healthvector.domain.calendar.data.standard.OtherEvent;
import ru.android.healthvector.domain.calendar.data.standard.PumpEvent;
import ru.android.healthvector.domain.calendar.data.standard.SleepEvent;
import ru.android.healthvector.domain.calendar.requests.GetEventsFilter;
import ru.android.healthvector.domain.calendar.requests.GetEventsRequest;
import ru.android.healthvector.domain.calendar.requests.GetEventsResponse;
import ru.android.healthvector.domain.child.ChildInteractor;
import ru.android.healthvector.presentation.core.BasePresenter;

@InjectViewState
public class BaseCalendarPresenter extends BasePresenter<BaseCalendarView> {
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

        unsubscribeOnDestroy(
                Observable.combineLatest(
                        calendarInteractor.getSelectedDate(),
                        calendarInteractor.getSelectedFilterValue(),
                        childInteractor.getActiveChild(),
                        (date, filter, child) -> GetEventsRequest.builder()
                                .date(date)
                                .filter(filter)
                                .child(child)
                                .build())
                        .doOnNext(request -> logger.debug("onGetRequest: " + request))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::requestData, this::onUnexpectedError));
    }

    private void requestData(@NonNull GetEventsRequest request) {
        unsubscribe(subscription);
        subscription = unsubscribeOnDestroy(
                calendarInteractor.getAll(request)
                        .doOnNext(response -> logger.debug("onGetData: " + response))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onGetData, this::onUnexpectedError));
    }

    private void onGetData(@NonNull GetEventsResponse response) {
        getViewState().showCalendarState(CalendarState.builder()
                .child(response.getRequest().getChild())
                .date(response.getRequest().getDate())
                .events(response.getEvents())
                .build());
    }

    public void switchDate(@NonNull LocalDate date) {
        logger.debug("user switch date: " + date);
        unsubscribeOnDestroy(
                calendarInteractor.setSelectedDateObservable(date)
                        .ignoreElements()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                        }, this::onUnexpectedError));
    }

    public void delete(@NonNull MasterEvent event) {
        if (event.getLinearGroup() == null) {
            getViewState().confirmDeleteOneEvent(event);
        } else {
            getViewState().askDeleteOneEventOrLinerGroup(event);
        }
    }

    public void deleteOneEvent(@NonNull MasterEvent event) {
        unsubscribeOnDestroy(
                calendarInteractor.delete(event)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(deletedEvent -> logger.debug("event deleted: " + deletedEvent))
                        .subscribe(deletedEvent -> {
                        }, this::onUnexpectedError));
    }

    public void deleteLinearGroup(@NonNull MasterEvent event) {
        getViewState().showDeletingEvents(true);
        unsubscribeOnDestroy(
                calendarInteractor.deleteLinearGroup(event)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(count -> logger.debug("deleted " + count + " events"))
                        .doOnNext(response -> getViewState().showDeletingEvents(false))
                        .doOnError(throwable -> getViewState().showDeletingEvents(false))
                        .subscribe(count -> {
                        }, this::onUnexpectedError));
    }

    public void moveOneEvent(@NonNull MasterEvent event, int minutes) {
        unsubscribeOnDestroy(
                calendarInteractor.move(event, minutes)
                        .map(masterEvent -> {
                            calendarInteractor.setSelectedDate(masterEvent.getDateTime().toLocalDate());
                            return masterEvent;
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(movedEvent -> logger.debug("event moved: " + movedEvent))
                        .subscribe(movedEvent -> {
                        }, this::onUnexpectedError));
    }

    public void moveLinearGroup(@NonNull MasterEvent event, int minutes) {
        getViewState().showUpdatingEvents(true);
        unsubscribeOnDestroy(
                calendarInteractor.moveLinearGroup(event, minutes)
                        .map(masterEvent -> {
                            calendarInteractor.setSelectedDate(masterEvent.getDateTime().toLocalDate());
                            return masterEvent;
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(movedEvent -> logger.debug("event moved: " + movedEvent))
                        .doOnNext(response -> getViewState().showUpdatingEvents(false))
                        .doOnError(throwable -> getViewState().showUpdatingEvents(false))
                        .subscribe(movedEvent -> {
                        }, this::onUnexpectedError));
    }

    public void requestEventDetail(@NonNull MasterEvent event) {
        switch (event.getEventType()) {
            case DIAPER:
                navigateToDiaperEvent(event);
                break;
            case FEED:
                navigateToFeedEvent(event);
                break;
            case OTHER:
                navigateToOtherEvent(event);
                break;
            case PUMP:
                navigateToPumpEvent(event);
                break;
            case SLEEP:
                navigateToSleepEvent(event);
                break;
            case DOCTOR_VISIT:
                navigateToDoctorVisitEvent(event);
                break;
            case MEDICINE_TAKING:
                navigateToMedicineTaking(event);
                break;
            case EXERCISE:
                navigateToExercise(event);
                break;
        }
    }

    private void navigateToDiaperEvent(@NonNull MasterEvent event) {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        calendarInteractor.getEventDetailOnce(event),
                        calendarInteractor.getDefaultDiaperEvent(),
                        (eventDetail, defaultEvent) -> Arrays.asList(eventDetail, defaultEvent))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                events -> getViewState().navigateToDiaperEvent(
                                        (MasterEvent) events.get(0), (DiaperEvent) events.get(1)),
                                this::onUnexpectedError));
    }

    private void navigateToFeedEvent(@NonNull MasterEvent event) {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        calendarInteractor.getEventDetailOnce(event),
                        calendarInteractor.getDefaultFeedEvent(),
                        (eventDetail, defaultEvent) -> Arrays.asList(eventDetail, defaultEvent))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                events -> getViewState().navigateToFeedEvent(
                                        (MasterEvent) events.get(0), (FeedEvent) events.get(1)),
                                this::onUnexpectedError));
    }

    private void navigateToOtherEvent(@NonNull MasterEvent event) {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        calendarInteractor.getEventDetailOnce(event),
                        calendarInteractor.getDefaultOtherEvent(),
                        (eventDetail, defaultEvent) -> Arrays.asList(eventDetail, defaultEvent))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                events -> getViewState().navigateToOtherEvent(
                                        (MasterEvent) events.get(0), (OtherEvent) events.get(1)),
                                this::onUnexpectedError));
    }

    private void navigateToPumpEvent(@NonNull MasterEvent event) {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        calendarInteractor.getEventDetailOnce(event),
                        calendarInteractor.getDefaultPumpEvent(),
                        (eventDetail, defaultEvent) -> Arrays.asList(eventDetail, defaultEvent))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                events -> getViewState().navigateToPumpEvent(
                                        (MasterEvent) events.get(0), (PumpEvent) events.get(1)),
                                this::onUnexpectedError));
    }

    private void navigateToSleepEvent(@NonNull MasterEvent event) {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        calendarInteractor.getEventDetailOnce(event),
                        calendarInteractor.getDefaultSleepEvent(),
                        (eventDetail, defaultEvent) -> Arrays.asList(eventDetail, defaultEvent))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                events -> getViewState().navigateToSleepEvent(
                                        (MasterEvent) events.get(0), (SleepEvent) events.get(1)),
                                this::onUnexpectedError));
    }

    private void navigateToDoctorVisitEvent(@NonNull MasterEvent event) {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        calendarInteractor.getEventDetailOnce(event),
                        calendarInteractor.getDefaultDoctorVisitEvent(),
                        (eventDetail, defaultEvent) -> Arrays.asList(eventDetail, defaultEvent))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                events -> getViewState().navigateToDoctorVisitEvent(
                                        (MasterEvent) events.get(0), (DoctorVisitEvent) events.get(1)),
                                this::onUnexpectedError));
    }

    private void navigateToMedicineTaking(@NonNull MasterEvent event) {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        calendarInteractor.getEventDetailOnce(event),
                        calendarInteractor.getDefaultMedicineTakingEvent(),
                        (eventDetail, defaultEvent) -> Arrays.asList(eventDetail, defaultEvent))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                events -> getViewState().navigateToMedicineTakingEvent(
                                        (MasterEvent) events.get(0), (MedicineTakingEvent) events.get(1)),
                                this::onUnexpectedError));
    }

    private void navigateToExercise(@NonNull MasterEvent event) {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        calendarInteractor.getEventDetailOnce(event),
                        calendarInteractor.getDefaultExerciseEvent(),
                        (eventDetail, defaultEvent) -> Arrays.asList(eventDetail, defaultEvent))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                events -> getViewState().navigateToExerciseEvent(
                                        (MasterEvent) events.get(0), (ExerciseEvent) events.get(1)),
                                this::onUnexpectedError));
    }

    public void done(@NonNull MasterEvent event) {
        switchDone(event);
    }

    private void switchDone(@NonNull MasterEvent event) {
        unsubscribeOnDestroy(
                calendarInteractor.done(event)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(doneEvent -> logger.debug("event isDone: " + doneEvent))
                        .subscribe(doneEvent -> {
                        }, this::onUnexpectedError));
    }

    public void requestFilterDialog() {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        calendarInteractor.getSelectedDateOnce(),
                        calendarInteractor.getSelectedFilterValueOnce(),
                        childInteractor.getActiveChildOnce(),
                        (date, filter, child) -> GetEventsRequest.builder()
                                .date(date)
                                .filter(filter)
                                .child(child)
                                .build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(request -> {
                            if (request.getChild().getId() == null) {
                                getViewState().noChildSpecified();
                                return;
                            }
                            getViewState().showFilterDialog(request.getFilter().getEventTypes());
                        }, this::onUnexpectedError));
    }

    public void setFilter(@NonNull Set<EventType> eventTypes) {
        unsubscribeOnDestroy(
                calendarInteractor.setSelectedFilterValueObservable(
                        GetEventsFilter.builder()
                                .eventTypes(eventTypes)
                                .build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(selectedFilter -> {
                        }, this::onUnexpectedError));
    }

    public void addProfile() {
        getViewState().navigateToProfileAdd();
    }
}
