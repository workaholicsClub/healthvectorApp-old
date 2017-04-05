package ru.android.childdiary.presentation.events.core;

import android.support.annotation.NonNull;

import com.annimon.stream.Stream;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.CalendarInteractor;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
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

    private Disposable subscription;

    @SuppressWarnings("unchecked")
    public void requestDefaultEventDetail(@NonNull EventType eventType) {
        unsubscribeOnDestroy(calendarInteractor.getDefaultEventDetail(eventType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(event -> logger.debug("default event details: " + event))
                .subscribe(event -> getViewState().showDefaultEventDetail((T) event)));
    }

    private void unsubscribe() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    @SuppressWarnings("unchecked")
    public void requestEventDetails(@NonNull MasterEvent masterEvent) {
        unsubscribe();
        subscription = unsubscribeOnDestroy(calendarInteractor.getEventDetail(masterEvent)
                .map(event -> {
                    childInteractor.setActiveChild(event.getChild());
                    calendarInteractor.setSelectedDate(event.getDateTime().toLocalDate());
                    return event;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(event -> logger.debug("event details: " + event))
                .subscribe(event -> getViewState().showEventDetail((T) event), this::onUnexpectedError));
    }

    public void requestTimeDialog(String tag, TimeDialog.Parameters parameters) {
        unsubscribeOnDestroy(childInteractor.getActiveChildOnce()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(child -> getViewState().showTimeDialog(tag, child, parameters), this::onUnexpectedError));
    }

    @SuppressWarnings("unchecked")
    public void addEvent(@NonNull T event, boolean afterButtonPressed) {
        unsubscribeOnDestroy(calendarInteractor.add(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(addedEvent -> logger.debug("event added: " + addedEvent))
                .doOnNext(this::requestEventDetails)
                .subscribe(addedEvent -> getViewState().eventAdded((T) addedEvent, afterButtonPressed), this::onUnexpectedError));
    }

    public void updateEvent(@NonNull T event, boolean afterButtonPressed) {
        unsubscribeOnDestroy(calendarInteractor.update(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(updatedEvent -> logger.debug("event updated: " + updatedEvent))
                .doOnNext(this::requestEventDetails)
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
