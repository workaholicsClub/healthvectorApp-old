package ru.android.childdiary.presentation.settings;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.calendar.CalendarInteractor;
import ru.android.childdiary.domain.child.ChildInteractor;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.child.requests.DeleteChildRequest;
import ru.android.childdiary.domain.child.requests.DeleteChildResponse;
import ru.android.childdiary.presentation.cloud.core.CloudPresenter;

@InjectViewState
public class SettingsPresenter extends CloudPresenter<SettingsView> {
    @Inject
    ChildInteractor childInteractor;

    @Inject
    CalendarInteractor calendarInteractor;

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
                .subscribe(getViewState()::showChildList, this::onUnexpectedError));

        unsubscribeOnDestroy(calendarInteractor.getSelectedDate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(selectedDate -> logger.debug("showSelectedDate: " + selectedDate))
                .subscribe(getViewState()::showSelectedDate, this::onUnexpectedError));

        unsubscribeOnDestroy(childInteractor.getActiveChild()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(child -> logger.debug("showChild: " + child))
                .subscribe(getViewState()::showChild, this::onUnexpectedError));

        unsubscribeOnDestroy(settingsInteractor.getAccountName()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(accountName -> logger.debug("showSelectedAccount: " + accountName))
                .subscribe(getViewState()::showSelectedAccount, this::onUnexpectedError));
    }

    public void addChild() {
        getViewState().navigateToProfileAdd();
    }

    public void editChild(@NonNull Child child) {
        getViewState().navigateToProfileEdit(child);
    }

    public void deleteChild(@NonNull Child child) {
        getViewState().showDeleteChildConfirmation(child);
    }

    public void forceDeleteChild(@NonNull Child child) {
        getViewState().showDeletingProfile(true);
        unsubscribeOnDestroy(childInteractor.delete(DeleteChildRequest.builder().child(child).build())
                .map(DeleteChildResponse::getRequest)
                .map(DeleteChildRequest::getChild)
                .doOnNext(deletedChild -> logger.debug("child deleted: " + deletedChild))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(response -> getViewState().showDeletingProfile(false))
                .doOnError(throwable -> getViewState().showDeletingProfile(false))
                .subscribe(getViewState()::childDeleted, this::onUnexpectedError));
    }
}
