package ru.android.childdiary.presentation.calendar;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.core.requests.ChildResponse;
import ru.android.childdiary.presentation.core.AppPartitionPresenter;

@InjectViewState
public class CalendarPresenter extends AppPartitionPresenter<CalendarView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void addDiaperEvent() {
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .flatMap(child -> calendarInteractor.getDefaultDiaperEvent()
                                .map(item -> new ChildResponse<>(child, item)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getChild().getId() == null) {
                                        getViewState().noChildSpecified();
                                        return;
                                    }
                                    getViewState().navigateToDiaperEventAdd(response.getResponse());
                                },
                                this::onUnexpectedError));
    }

    public void addSleepEvent() {
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .flatMap(child -> calendarInteractor.getDefaultSleepEvent()
                                .map(item -> new ChildResponse<>(child, item)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getChild().getId() == null) {
                                        getViewState().noChildSpecified();
                                        return;
                                    }
                                    getViewState().navigateToSleepEventAdd(response.getResponse());
                                },
                                this::onUnexpectedError));
    }

    public void addFeedEvent() {
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .flatMap(child -> calendarInteractor.getDefaultFeedEvent()
                                .map(item -> new ChildResponse<>(child, item)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getChild().getId() == null) {
                                        getViewState().noChildSpecified();
                                        return;
                                    }
                                    getViewState().navigateToFeedEventAdd(response.getResponse());
                                },
                                this::onUnexpectedError));
    }

    public void addPumpEvent() {
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .flatMap(child -> calendarInteractor.getDefaultPumpEvent()
                                .map(item -> new ChildResponse<>(child, item)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getChild().getId() == null) {
                                        getViewState().noChildSpecified();
                                        return;
                                    }
                                    getViewState().navigateToPumpEventAdd(response.getResponse());
                                },
                                this::onUnexpectedError));
    }

    public void addOtherEvent() {
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .flatMap(child -> calendarInteractor.getDefaultOtherEvent()
                                .map(item -> new ChildResponse<>(child, item)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getChild().getId() == null) {
                                        getViewState().noChildSpecified();
                                        return;
                                    }
                                    getViewState().navigateToOtherEventAdd(response.getResponse());
                                },
                                this::onUnexpectedError));
    }

    public void addProfile() {
        getViewState().navigateToProfileAdd();
    }
}
