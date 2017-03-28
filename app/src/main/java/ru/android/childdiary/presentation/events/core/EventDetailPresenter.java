package ru.android.childdiary.presentation.events.core;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.annimon.stream.Stream;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.CalendarInteractor;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.requests.AddEventRequest;
import ru.android.childdiary.domain.interactors.calendar.validation.CalendarValidationException;
import ru.android.childdiary.domain.interactors.calendar.validation.CalendarValidationResult;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.presentation.events.dialogs.TimeDialog;

public abstract class EventDetailPresenter<V extends EventDetailView<T>, T extends MasterEvent> extends BasePresenter<V> {
    @Inject
    protected ChildInteractor childInteractor;

    @Inject
    protected CalendarInteractor calendarInteractor;

    private boolean isSubscribedToEventDetails;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(calendarInteractor.getDefaultNotifyTimeInMinutes(getEventType())
                .map(defaultNotifyTime -> defaultNotifyTime > 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showNotifyTimeView));
    }

    @SuppressWarnings("unchecked")
    @CallSuper
    public void requestDefaultEventDetail(@NonNull EventType eventType) {
        unsubscribeOnDestroy(calendarInteractor.getDefaultEventDetail(eventType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(event -> logger.debug("default event details: " + event))
                .subscribe(event -> getViewState().showDefaultEventDetail((T) event)));
    }

    @SuppressWarnings("unchecked")
    public void requestEventDetails(@NonNull MasterEvent masterEvent) {
        if (!isSubscribedToEventDetails) {
            unsubscribeOnDestroy(calendarInteractor.getEventDetail(masterEvent)
                    .map(event -> {
                        childInteractor.setActiveChild(event.getChild());
                        calendarInteractor.setSelectedDate(event.getDateTime().toLocalDate());
                        return event;
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(event -> logger.debug("event details: " + event))
                    .subscribe(event -> getViewState().showEventDetail((T) event), this::onUnexpectedError));
            isSubscribedToEventDetails = true;
        }
    }

    public void requestTimeDialog(String tag, TimeDialog.Parameters parameters) {
        unsubscribeOnDestroy(childInteractor.getActiveChildOnce()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(child -> getViewState().showTimeDialog(tag, child, parameters), this::onUnexpectedError));
    }

    @SuppressWarnings("unchecked")
    public void addEvent(@NonNull T event, boolean afterButtonPressed) {
        unsubscribeOnDestroy(childInteractor.getActiveChildOnce()
                .map(child -> AddEventRequest.builder().child(child).event(event).build())
                .flatMap(calendarInteractor::add)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(addedEvent -> logger.debug("event added: " + addedEvent))
                .subscribe(addedEvent -> getViewState().eventAdded((T) addedEvent, afterButtonPressed), this::onUnexpectedError));
    }

    public void updateEvent(@NonNull T event, boolean afterButtonPressed) {
        unsubscribeOnDestroy(calendarInteractor.update(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(updatedEvent -> logger.debug("event updated: " + updatedEvent))
                .subscribe(updatedEvent -> getViewState().eventUpdated(updatedEvent, afterButtonPressed), this::onUnexpectedError));
    }

    public void deleteEvent(@NonNull T event) {
        unsubscribeOnDestroy(calendarInteractor.delete(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(deletedEvent -> logger.debug("event deleted: " + deletedEvent))
                .subscribe(deletedEvent -> getViewState().eventDeleted(deletedEvent), this::onUnexpectedError));
    }

    public void doneEvent(@NonNull T event) {
        unsubscribeOnDestroy(calendarInteractor.done(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(doneEvent -> logger.debug("event done: " + doneEvent))
                .subscribe(doneEvent -> getViewState().eventDone(doneEvent), this::onUnexpectedError));
    }

    @Override
    public void onUnexpectedError(Throwable e) {
        if (e instanceof CalendarValidationException) {
            List<CalendarValidationResult> results = ((CalendarValidationException) e).getValidationResults();
            if (results.isEmpty()) {
                logger.error("calendar validation results empty");
                return;
            }

            getViewState().validationFailed();
            String msg = Stream.of(results)
                    .filter(CalendarValidationResult::notValid)
                    .map(CalendarValidationResult::toString)
                    .findFirst().orElse(null);
            getViewState().showValidationErrorMessage(msg);
            handleValidationResult(results);
        } else {
            super.onUnexpectedError(e);
        }
    }

    protected abstract EventType getEventType();

    protected void handleValidationResult(List<CalendarValidationResult> results) {
    }
}
