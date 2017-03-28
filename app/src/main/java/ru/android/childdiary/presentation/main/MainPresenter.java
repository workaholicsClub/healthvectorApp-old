package ru.android.childdiary.presentation.main;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.CalendarInteractor;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.utils.StringUtils;

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
        logger.debug("onGetChildList: " + StringUtils.childList(childList));
        getViewState().showChildList(childList);
        if (isFirstTime) {
            if (childList.isEmpty()) {
                getViewState().navigateToProfileAdd();
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

    public void reviewChild() {
        getViewState().navigateToProfileReview();
    }

    public void deleteChild() {
        unsubscribeOnDestroy(childInteractor.getActiveChildOnce()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showDeleteChildConfirmation, this::onUnexpectedError));
    }

    public void deleteChild(@NonNull Child child) {
        unsubscribeOnDestroy(childInteractor.delete(child)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deletedChild -> logger.debug("child deleted: " + deletedChild), this::onUnexpectedError));
    }

    public void addDiaperEvent() {
        getViewState().navigateToDiaperEventAdd();
    }

    public void addSleepEvent() {
        getViewState().navigateToSleepEventAdd();
    }

    public void addFeedEvent() {
        getViewState().navigateToFeedEventAdd();
    }

    public void addPumpEventClick() {
        getViewState().navigateToPumpEventAdd();
    }

    public void addOtherEventClick() {
        getViewState().navigateToOtherEventAdd();
    }

    private void onEventAdded(@NonNull MasterEvent event) {
        logger.debug("onEventAdded: " + event);
    }
}
