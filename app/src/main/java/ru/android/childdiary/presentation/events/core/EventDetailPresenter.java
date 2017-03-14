package ru.android.childdiary.presentation.events.core;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.domain.interactors.calendar.CalendarInteractor;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.requests.AddEventRequest;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

public abstract class EventDetailPresenter<T extends MasterEvent> extends BasePresenter<EventDetailView<T>> {
    @Inject
    ChildInteractor childInteractor;

    @Inject
    CalendarInteractor calendarInteractor;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(childInteractor.getActiveChild()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(child -> logger.debug("showChild: " + child))
                .subscribe(getViewState()::showChild, this::onUnexpectedError));
    }

    @SuppressWarnings("unchecked")
    public void requestEventDetails(@NonNull MasterEvent masterEvent) {
        unsubscribeOnDestroy(calendarInteractor.getEventDetail(masterEvent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(event -> logger.debug("event details: " + event))
                .subscribe(event -> getViewState().showEventDetail((T) event), this::onUnexpectedError));
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
