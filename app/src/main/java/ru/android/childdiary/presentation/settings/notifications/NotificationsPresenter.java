package ru.android.childdiary.presentation.settings.notifications;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.calendar.CalendarInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class NotificationsPresenter extends BasePresenter<NotificationsView> {
    @Inject
    CalendarInteractor calendarInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(calendarInteractor.getNotificationSettings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showNotificationSettings, this::onUnexpectedError));
    }
}
