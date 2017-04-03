package ru.android.childdiary.presentation.events;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.presentation.events.core.EventDetailPresenter;
import ru.android.childdiary.presentation.events.core.EventDetailView;

@InjectViewState
public class SleepEventDetailPresenter extends EventDetailPresenter<EventDetailView<SleepEvent>, SleepEvent> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected EventType getEventType() {
        return EventType.SLEEP;
    }

    public void startTimer(@NonNull SleepEvent event) {
        unsubscribeOnDestroy(calendarInteractor.startTimer(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showEventDetail, this::onUnexpectedError));
    }

    public void stopTimer(@NonNull SleepEvent event) {
        unsubscribeOnDestroy(calendarInteractor.stopTimer(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showEventDetail, this::onUnexpectedError));
    }
}
