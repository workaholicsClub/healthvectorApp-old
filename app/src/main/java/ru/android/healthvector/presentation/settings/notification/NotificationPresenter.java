package ru.android.healthvector.presentation.settings.notification;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.calendar.CalendarInteractor;
import ru.android.healthvector.domain.calendar.data.core.EventNotification;
import ru.android.healthvector.presentation.core.BasePresenter;

@InjectViewState
public class NotificationPresenter extends BasePresenter<NotificationView> {
    @Inject
    CalendarInteractor calendarInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void init(@NonNull EventType eventType) {
        unsubscribeOnDestroy(calendarInteractor.getNotificationSettingsOnce(eventType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showNotificationSettings, this::onUnexpectedError));
    }

    public void save(@NonNull EventNotification eventNotification) {
        unsubscribeOnDestroy(
                calendarInteractor.getNotificationSettingsOnce(eventNotification.getEventType())
                        .map(eventNotification::equals)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(notChanged -> {
                            if (notChanged) {
                                getViewState().exit(eventNotification);
                            } else {
                                getViewState().confirmSave(eventNotification);
                            }
                        }, this::onUnexpectedError));
    }

    public void forceSave(@NonNull EventNotification eventNotification) {
        unsubscribeOnDestroy(calendarInteractor.setNotificationSettings(eventNotification)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::exit, this::onUnexpectedError));
    }
}
