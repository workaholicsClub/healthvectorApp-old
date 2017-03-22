package ru.android.childdiary.presentation.events.core;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.CalendarInteractor;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.requests.AddEventRequest;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.presentation.events.dialogs.TimeDialog;

public abstract class EventDetailPresenter<V extends EventDetailView<T>, T extends MasterEvent> extends BasePresenter<V> {
    @Inject
    protected ChildInteractor childInteractor;

    @Inject
    protected CalendarInteractor calendarInteractor;

    private boolean isSubscribedToEventDetails;

    @SuppressWarnings("unchecked")
    @CallSuper
    public void requestDefaultEventDetail(@NonNull EventType eventType) {
        unsubscribeOnDestroy(childInteractor.getActiveChildOnce()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(child -> logger.debug("showChild: " + child))
                .subscribe(getViewState()::showChild, this::onUnexpectedError));
        unsubscribeOnDestroy(calendarInteractor.getDefaultEventDetail(eventType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(event -> logger.debug("default event details: " + event))
                .subscribe(event -> getViewState().showDefaultEventDetail((T) event)));
    }

    @SuppressWarnings("unchecked")
    public void requestEventDetails(@NonNull MasterEvent masterEvent) {
        if (!isSubscribedToEventDetails) {
            unsubscribeOnDestroy(childInteractor.setActiveChild(masterEvent.getChild())
                    .flatMap(child -> calendarInteractor.getEventDetail(masterEvent)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext(event -> logger.debug("event details: " + event)))
                    .subscribe(event -> getViewState().showEventDetail((T) event), this::onUnexpectedError));
            isSubscribedToEventDetails = true;
        }
    }

    public void requestFoodMeasureDialog(String tag) {
        unsubscribeOnDestroy(childInteractor.getActiveChildOnce()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(child -> getViewState().showFoodMeasureDialog(tag, child), this::onUnexpectedError));
    }

    public void requestTimeDialog(String tag, TimeDialog.Parameters parameters) {
        unsubscribeOnDestroy(childInteractor.getActiveChildOnce()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(child -> getViewState().showTimeDialog(tag, child, parameters), this::onUnexpectedError));
    }

    @SuppressWarnings("unchecked")
    public void addEvent(@NonNull T event) {
        unsubscribeOnDestroy(childInteractor.getActiveChildOnce()
                .map(child -> AddEventRequest.builder().child(child).event(event).build())
                .flatMap(calendarInteractor::add)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(addedEvent -> logger.debug("event added: " + addedEvent))
                .subscribe(addedEvent -> getViewState().eventAdded((T) addedEvent), this::onUnexpectedError));
    }

    public void updateEvent(@NonNull T event) {
        unsubscribeOnDestroy(calendarInteractor.update(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(updatedEvent -> logger.debug("event updated: " + updatedEvent))
                .subscribe(getViewState()::eventUpdated, this::onUnexpectedError));
    }
}
