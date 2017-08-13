package ru.android.childdiary.presentation.core;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.domain.calendar.CalendarInteractor;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.child.ChildInteractor;

public abstract class AppPartitionPresenter<V extends AppPartitionView> extends BasePresenter<V> {
    @Inject
    protected ChildInteractor childInteractor;

    @Inject
    protected CalendarInteractor calendarInteractor;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(calendarInteractor.getSelectedDate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(selectedDate -> logger.debug("showSelectedDate: " + selectedDate))
                .subscribe(this::onSelectedDateLoaded, this::onUnexpectedError));

        unsubscribeOnDestroy(childInteractor.getActiveChild()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(child -> logger.debug("showChild: " + child))
                .subscribe(this::onChildLoaded, this::onUnexpectedError));
    }

    @CallSuper
    protected void onSelectedDateLoaded(LocalDate date) {
        getViewState().showSelectedDate(date);
    }

    @CallSuper
    protected void onChildLoaded(@NonNull Child child) {
        getViewState().showChild(child);
    }
}
