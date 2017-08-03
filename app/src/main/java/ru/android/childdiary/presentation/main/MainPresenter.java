package ru.android.childdiary.presentation.main;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.CalendarInteractor;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.domain.interactors.child.requests.DeleteChildRequest;
import ru.android.childdiary.domain.interactors.child.requests.DeleteChildResponse;
import ru.android.childdiary.presentation.core.AppPartitionArguments;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.utils.strings.StringUtils;

@InjectViewState
public class MainPresenter extends BasePresenter<MainView> {
    @Inject
    ChildInteractor childInteractor;

    @Inject
    CalendarInteractor calendarInteractor;

    private boolean isFirstTime = true;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(childInteractor.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetChildList, this::onUnexpectedError));
    }

    private void onGetChildList(@NonNull List<Child> childList) {
        logger.debug("onGetChildList: " + StringUtils.toString(childList));
        getViewState().showChildList(childList);
        if (isFirstTime) {
            if (childList.isEmpty()) {
                getViewState().navigateToProfileAddFirstTime();
            }
            unsubscribeOnDestroy(childInteractor.getActiveChild()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::showChild, this::onUnexpectedError));
            isFirstTime = false;
        }
    }

    public void switchChild(@NonNull Child child) {
        logger.debug("user switch child: " + child);
        unsubscribeOnDestroy(childInteractor.setActiveChildObservable(child)
                .ignoreElements()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, this::onUnexpectedError));
    }

    public void addChild() {
        getViewState().navigateToProfileAdd();
    }

    public void editChild() {
        unsubscribeOnDestroy(childInteractor.getActiveChildOnce()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::navigateToProfileEdit, this::onUnexpectedError));
    }

    public void deleteChild() {
        unsubscribeOnDestroy(childInteractor.getActiveChildOnce()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showDeleteChildConfirmation, this::onUnexpectedError));
    }

    public void forceDeleteChild(@NonNull Child child) {
        unsubscribeOnDestroy(childInteractor.delete(DeleteChildRequest.builder().child(child).build())
                .map(DeleteChildResponse::getRequest)
                .map(DeleteChildRequest::getChild)
                .doOnNext(deletedChild -> logger.debug("child deleted: " + deletedChild))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::childDeleted, this::onUnexpectedError));
    }

    public void openCalendar() {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        childInteractor.getActiveChildOnce(),
                        calendarInteractor.getSelectedDateOnce(),
                        (child, selectedDate) -> AppPartitionArguments.builder()
                                .child(child)
                                .selectedDate(selectedDate)
                                .build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::navigateToCalendar, this::onUnexpectedError));
    }

    public void openDevelopmentDiary() {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        childInteractor.getActiveChildOnce(),
                        calendarInteractor.getSelectedDateOnce(),
                        (child, selectedDate) -> AppPartitionArguments.builder()
                                .child(child)
                                .selectedDate(selectedDate)
                                .build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::navigateToDevelopmentDiary, this::onUnexpectedError));
    }

    public void openExercises() {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        childInteractor.getActiveChildOnce(),
                        calendarInteractor.getSelectedDateOnce(),
                        (child, selectedDate) -> AppPartitionArguments.builder()
                                .child(child)
                                .selectedDate(selectedDate)
                                .build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::navigateToExercises, this::onUnexpectedError));
    }

    public void openMedicalData() {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        childInteractor.getActiveChildOnce(),
                        calendarInteractor.getSelectedDateOnce(),
                        (child, selectedDate) -> AppPartitionArguments.builder()
                                .child(child)
                                .selectedDate(selectedDate)
                                .build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::navigateToMedicalData, this::onUnexpectedError));
    }

    public void openSettings() {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        childInteractor.getActiveChildOnce(),
                        calendarInteractor.getSelectedDateOnce(),
                        (child, selectedDate) -> AppPartitionArguments.builder()
                                .child(child)
                                .selectedDate(selectedDate)
                                .build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::navigateToSettings, this::onUnexpectedError));
    }

    public void openHelp() {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        childInteractor.getActiveChildOnce(),
                        calendarInteractor.getSelectedDateOnce(),
                        (child, selectedDate) -> AppPartitionArguments.builder()
                                .child(child)
                                .selectedDate(selectedDate)
                                .build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::navigateToHelp, this::onUnexpectedError));
    }
}
