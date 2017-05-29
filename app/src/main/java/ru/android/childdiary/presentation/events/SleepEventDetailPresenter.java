package ru.android.childdiary.presentation.events;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.Observable;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
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
    protected Observable<SleepEvent> getEventDetail(@NonNull MasterEvent masterEvent) {
        return calendarInteractor.getEventDetail(masterEvent);
    }

    @Override
    protected void doOnAddEvent(@NonNull SleepEvent event) {
        requestEventDetails(event);
    }

    @Override
    protected EventType getEventType() {
        return EventType.SLEEP;
    }
}
