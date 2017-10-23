package ru.android.childdiary.presentation.events.core;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.calendar.CalendarInteractor;
import ru.android.childdiary.domain.calendar.data.core.LinearGroupFieldType;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.calendar.validation.CalendarValidationException;
import ru.android.childdiary.domain.calendar.validation.CalendarValidationResult;
import ru.android.childdiary.domain.child.ChildInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.utils.strings.EventUtils;

public abstract class EventDetailPresenter<V extends EventDetailView<T>, T extends MasterEvent> extends BasePresenter<V> {
    @Inject
    protected ChildInteractor childInteractor;

    @Inject
    protected CalendarInteractor calendarInteractor;

    private Disposable subscription;

    @SuppressWarnings("unchecked")
    public void requestEventDetails(@NonNull MasterEvent masterEvent) {
        unsubscribe(subscription);
        subscription = unsubscribeOnDestroy(calendarInteractor.getEventDetail(masterEvent)
                .distinctUntilChanged(Object::equals)
                .map(event -> {
                    if (event.getChild() != null) {
                        childInteractor.setActiveChild(event.getChild());
                    }
                    calendarInteractor.setSelectedDate(event.getDateTime().toLocalDate());
                    return event;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(event -> logger.debug("event details: " + event))
                .subscribe(event -> getViewState().showEventDetail((T) event), this::onUnexpectedError));
    }

    public void addEvent(@NonNull T event, boolean afterButtonPressed) {
        getViewState().setButtonAddEnabled(false);
        unsubscribeOnDestroy(calendarInteractor.add(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(addedEvent -> logger.debug("event added: " + addedEvent))
                .doOnNext(this::requestEventDetails)
                .doOnNext(addedEvent -> getViewState().setButtonAddEnabled(true))
                .doOnError(throwable -> getViewState().setButtonAddEnabled(true))
                .subscribe(addedEvent -> getViewState().eventAdded(addedEvent, afterButtonPressed), this::onUnexpectedError));
    }

    public void updateEvent(@NonNull T event, boolean afterButtonPressed) {
        unsubscribeOnDestroy(calendarInteractor.update(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(updatedEvent -> logger.debug("event updated: " + updatedEvent))
                .subscribe(updatedEvent -> getViewState().eventUpdated(updatedEvent, afterButtonPressed), this::onUnexpectedError));
    }

    public void updateLinearGroup(@NonNull T event, List<LinearGroupFieldType> fields, boolean afterButtonPressed) {
        getViewState().showUpdatingEvents(true);
        unsubscribeOnDestroy(calendarInteractor.updateLinearGroup(event, fields)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(updatedEvent -> logger.debug("linear group updated: " + updatedEvent))
                .doOnNext(response -> getViewState().showUpdatingEvents(false))
                .doOnError(throwable -> getViewState().showUpdatingEvents(false))
                .subscribe(updatedEvent -> getViewState().eventUpdated(updatedEvent, afterButtonPressed), this::onUnexpectedError));
    }

    public void updateEventSilently(@NonNull T event) {
        unsubscribeOnDestroy(calendarInteractor.update(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updatedEvent -> logger.debug("event updated silently: " + updatedEvent), this::onUnexpectedError));
    }

    public void delete(@NonNull MasterEvent event) {
        if (event.getLinearGroup() == null) {
            getViewState().confirmDeleteOneEvent(event);
        } else {
            getViewState().askDeleteOneEventOrLinerGroup(event);
        }
    }

    public void deleteOneEvent(@NonNull MasterEvent event) {
        unsubscribe(subscription);
        unsubscribeOnDestroy(calendarInteractor.delete(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(deletedEvent -> logger.debug("event deleted: " + deletedEvent))
                .subscribe(getViewState()::eventDeleted, this::onUnexpectedError));
    }

    public void deleteLinearGroup(@NonNull MasterEvent event) {
        unsubscribe(subscription);
        getViewState().showDeletingEvents(true);
        unsubscribeOnDestroy(calendarInteractor.deleteLinearGroup(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(count -> logger.debug("deleted " + count + " events"))
                .doOnNext(response -> getViewState().showDeletingEvents(false))
                .doOnError(throwable -> getViewState().showDeletingEvents(false))
                .subscribe(count -> getViewState().eventDeleted(event), this::onUnexpectedError));
    }

    public void done(@NonNull T event) {
        boolean isDone = EventUtils.isDone(event);
        switchDone(!isDone);
    }

    private void switchDone(boolean done) {
        // будет сохранено в базу при выходе из окна деталей события
        getViewState().eventDone(done);
    }

    @Override
    public void onUnexpectedError(Throwable e) {
        if (e instanceof CalendarValidationException) {
            List<CalendarValidationResult> results = ((CalendarValidationException) e).getValidationResults();
            if (results.isEmpty()) {
                logger.error("validation results empty");
                return;
            }

            getViewState().validationFailed();
            String msg = Observable.fromIterable(results)
                    .filter(CalendarValidationResult::notValid)
                    .map(CalendarValidationResult::toString)
                    .blockingFirst();
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
