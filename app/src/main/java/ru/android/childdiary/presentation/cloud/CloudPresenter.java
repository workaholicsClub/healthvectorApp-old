package ru.android.childdiary.presentation.cloud;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.availability.NetworkAvailability;
import ru.android.childdiary.data.availability.PlayServicesAvailability;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.cloud.CloudInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class CloudPresenter extends BasePresenter<CloudView> {
    @Inject
    NetworkAvailability networkAvailability;

    @Inject
    PlayServicesAvailability playServicesAvailability;

    @Inject
    CloudInteractor cloudInteractor;

    private PreConditions preConditions;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void later() {
        getViewState().navigateToMain();
    }

    public void bindAccount() {
        checkPreConditions();
    }

    private void checkPreConditions() {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        playServicesAvailability.isPlayServicesAvailable().toObservable(),
                        networkAvailability.isNetworkAvailable().toObservable(),
                        cloudInteractor.getAccountNameOnce(),
                        (isPlayServicesAvailable, isNetworkAvailable, accountName) -> PreConditions.builder()
                                .isPlayServicesAvailable(isPlayServicesAvailable)
                                .isNetworkAvailable(isNetworkAvailable).accountName(accountName)
                                .build())
                        .subscribe(this::processPreConditions, this::onUnexpectedError));
    }

    private void processPreConditions(PreConditions preConditions) {
        this.preConditions = preConditions;
        if (!preConditions.isPlayServicesAvailable()) {
            getViewState().acquireGooglePlayServices();
        } else if (TextUtils.isEmpty(preConditions.getAccountName())) {
            getViewState().requestPermission();
        } else if (!preConditions.isNetworkAvailable()) {
            getViewState().connectionUnavailable();
        } else {
            checkIsBackupAvailable();
        }
    }

    public void permissionGranted() {
        String accountName = preConditions.getAccountName();
        if (TextUtils.isEmpty(accountName)) {
            getViewState().chooseAccount();
        } else {
            checkPreConditions();
        }
    }

    public void permissionDenied() {
        getViewState().navigateToMain();
    }

    private void checkIsBackupAvailable() {
        getViewState().showBackupLoading(true);
        unsubscribeOnDestroy(
                cloudInteractor.checkIsBackupAvailable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess(result -> getViewState().showBackupLoading(false))
                        .doOnError(throwable -> getViewState().showBackupLoading(false))
                        .subscribe(result -> {
                                    getViewState().navigateToMain();
                                    // TODO: диалог о предложении восстановиться или переход в главное окно
                                },
                                this::onUnexpectedError));
    }

    public void playServicesResolved() {
        checkPreConditions();
    }

    public void accountChosen(@Nullable String accountName) {
        cloudInteractor.setAccountName(accountName);
        checkPreConditions();
    }

    @Value
    @Builder
    private static class PreConditions {
        boolean isPlayServicesAvailable;
        boolean isNetworkAvailable;
        String accountName;
    }
}
