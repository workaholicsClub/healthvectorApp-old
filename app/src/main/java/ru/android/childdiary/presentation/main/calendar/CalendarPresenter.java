package ru.android.childdiary.presentation.main.calendar;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.core.events.ActiveChildChangedEvent;
import ru.android.childdiary.domain.core.events.SelectedDateChangedEvent;
import ru.android.childdiary.domain.interactors.calendar.CalendarInteractor;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;
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

    private LocalDate selectedDate = LocalDate.now();
    private Child activeChild;
    private Disposable subscription;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(childInteractor.getActiveChild()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setActiveChild, this::onUnexpectedError));
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setActiveChild(ActiveChildChangedEvent event) {
        setActiveChild(event.getChild());
    }

    private void setActiveChild(@NonNull Child child) {
        logger.debug("setActiveChild: " + child);
        if (!child.equals(activeChild)) {
            logger.debug("active child changed");
            if (child == Child.NULL) {
                activeChild = null;
            } else {
                activeChild = child;
            }
            getViewState().setActive(activeChild);
            requestEvents();
        }
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
        setSelectedDate(event.getDate());
    }

    private void setSelectedDate(@NonNull LocalDate date) {
        logger.debug("setSelectedDate: " + date);
        if (!date.equals(selectedDate)) {
            logger.debug("selected date changed");
            selectedDate = date;
            getViewState().setSelected(selectedDate);
            requestEvents();
        }
    }

    private void unsubscribe() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    private void requestEvents() {
        unsubscribe();

        if (activeChild == null) {
            getViewState().showEvents(Collections.emptyList());
            return;
        }

        subscription = calendarInteractor.getAll(activeChild, selectedDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetEvents, this::onUnexpectedError);
    }

    private void onGetEvents(@NonNull List<MasterEvent> events) {
        getViewState().showEvents(events);
    }
}
