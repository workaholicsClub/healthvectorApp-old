package ru.android.childdiary.presentation.calendar.partitions;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import org.joda.time.LocalDate;

import java.util.Arrays;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.CalendarInteractor;
import ru.android.childdiary.domain.interactors.calendar.events.DoctorVisitEvent;
import ru.android.childdiary.domain.interactors.calendar.events.MedicineTakingEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.calendar.requests.GetEventsRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.GetEventsResponse;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.utils.EventHelper;

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

        unsubscribeOnDestroy(Observable.combineLatest(
                calendarInteractor.getSelectedDate(),
                childInteractor.getActiveChild(),
                (date, child) -> GetEventsRequest.builder().date(date).child(child).build())
                .doOnNext(request -> logger.debug("onGetRequest: " + request))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::requestData, this::onUnexpectedError));
    }

    private void requestData(@NonNull GetEventsRequest request) {
        unsubscribe(subscription);
        subscription = unsubscribeOnDestroy(calendarInteractor.getAll(request)
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
        unsubscribeOnDestroy(calendarInteractor.setSelectedDateObservable(date)
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
        unsubscribeOnDestroy(calendarInteractor.delete(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(deletedEvent -> logger.debug("event deleted: " + deletedEvent))
                .subscribe(deletedEvent -> {
                }, this::onUnexpectedError));
    }

    public void deleteLinearGroup(@NonNull MasterEvent event) {
        getViewState().showDeletingEvents(true);
        unsubscribeOnDestroy(calendarInteractor.deleteLinearGroup(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(count -> logger.debug("deleted " + count + " events"))
                .doOnNext(response -> getViewState().showDeletingEvents(false))
                .doOnError(throwable -> getViewState().showDeletingEvents(false))
                .subscribe(count -> {
                }, this::onUnexpectedError));
    }

    public void move(@NonNull MasterEvent event) {
        getViewState().showUpdatingEvents(true);
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
            // TODO EXERCISE
        }
    }

    private void navigateToDiaperEvent(@NonNull MasterEvent event) {
        unsubscribeOnDestroy(Observable.combineLatest(
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
        unsubscribeOnDestroy(Observable.combineLatest(
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
        unsubscribeOnDestroy(Observable.combineLatest(
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
        unsubscribeOnDestroy(Observable.combineLatest(
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
        unsubscribeOnDestroy(Observable.combineLatest(
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
        unsubscribeOnDestroy(Observable.combineLatest(
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
        unsubscribeOnDestroy(Observable.combineLatest(
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

    // TODO EXERCISE

    public void done(@NonNull MasterEvent event) {
        boolean willBeUndone = EventHelper.isDone(event);
        if (willBeUndone) {
            // можно развыполнить, не делаем проверок
            switchDone(event);
        } else {
            // проверяем, можно ли выполнить событие
            if (EventHelper.needToFillNoteOrPhoto(event)) {
                getViewState().showNeedToFillNoteOrPhoto();
            } else {
                switchDone(event);
            }
        }
    }

    private void switchDone(@NonNull MasterEvent event) {
        unsubscribeOnDestroy(calendarInteractor.done(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(doneEvent -> logger.debug("event isDone: " + doneEvent))
                .subscribe(doneEvent -> {
                }, this::onUnexpectedError));
    }
}
