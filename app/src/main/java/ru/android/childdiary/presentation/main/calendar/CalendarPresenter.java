package ru.android.childdiary.presentation.main.calendar;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.LocalDate;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.repositories.core.events.ActiveChildChangedEvent;
import ru.android.childdiary.data.repositories.core.events.SelectedDateChangedEvent;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.CalendarInteractor;
import ru.android.childdiary.domain.interactors.calendar.requests.EventsRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.EventsResponse;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class CalendarPresenter extends BasePresenter<CalendarView> {
    @Inject
    ChildInteractor childInteractor;

    @Inject
    CalendarInteractor calendarInteractor;

    @Inject
    EventBus bus;

    private Disposable subscription;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        requestEvents();
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unsubscribe();
        bus.unregister(this);
    }

    private void unsubscribe() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    private void requestEvents() {
        unsubscribe();
        subscription = Observable.combineLatest(
                calendarInteractor.getSelectedDate(),
                childInteractor.getActiveChildOnce(),
                (date, child) -> EventsRequest.builder().date(date).child(child).build())
                .flatMap(request -> calendarInteractor.getAll(request))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetData, this::onUnexpectedError);
    }

    private void onGetData(EventsResponse response) {
        getViewState().setActive(response.getRequest().getChild());
        getViewState().setSelected(response.getRequest().getDate());
        getViewState().showEvents(response.getEvents());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setActiveChild(ActiveChildChangedEvent event) {
        requestEvents();
    }

    public void switchDate(@NonNull LocalDate date) {
        logger.debug("user switch date: " + date);
        unsubscribeOnDestroy(calendarInteractor.setSelectedDate(date)
                .ignoreElements()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, this::onUnexpectedError));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setSelectedDate(SelectedDateChangedEvent event) {
        requestEvents();
    }
}
