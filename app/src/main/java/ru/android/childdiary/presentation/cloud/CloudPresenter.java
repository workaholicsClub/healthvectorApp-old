package ru.android.childdiary.presentation.cloud;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;
import com.google.android.gms.common.ConnectionResult;

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
    PlayServicesAvailability playServicesAvailability;

    @Inject
    NetworkAvailability networkAvailability;

    @Inject
    CloudInteractor cloudInteractor;

    private PreConditions preConditions;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void moveNext() {
        getViewState().navigateToMain();
    }

    public void bindAccount() {
        checkPreConditions();
    }

    private void checkPreConditions() {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        playServicesAvailability.checkPlayServicesAvailability().toObservable(),
                        networkAvailability.checkNetworkAvailability().toObservable(),
                        cloudInteractor.getAccountNameOnce(),
                        (connectionStatusCode, isNetworkAvailable, accountName) -> PreConditions.builder()
                                .isPlayServicesAvailable(connectionStatusCode == ConnectionResult.SUCCESS)
                                .connectionStatusCode(connectionStatusCode)
                                .isNetworkAvailable(isNetworkAvailable)
                                .isAccountNameAvailable(!TextUtils.isEmpty(accountName))
                                .accountName(accountName)
                                .build())
                        .subscribe(this::processPreConditions, this::onUnexpectedError));
    }

    private void processPreConditions(PreConditions preConditions) {
        this.preConditions = preConditions;
        if (!preConditions.isPlayServicesAvailable()) {
            getViewState().showPlayServicesErrorDialog(preConditions.getConnectionStatusCode());
        } else if (!preConditions.isAccountNameAvailable()) {
            getViewState().requestPermission();
        } else if (!preConditions.isNetworkAvailable()) {
            getViewState().connectionUnavailable();
        } else {
            checkIsBackupAvailable();
        }
    }

    public void permissionGranted() {
        if (!preConditions.isAccountNameAvailable()) {
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
                        .doOnSuccess(isBackupAvailable -> getViewState().showBackupLoading(false))
                        .doOnError(throwable -> getViewState().showBackupLoading(false))
                        .doOnError(throwable -> logger.error("checkIsBackupAvailable", throwable))
                        .subscribe(
                                isBackupAvailable -> {
                                    if (isBackupAvailable) {
                                        getViewState().foundBackup();
                                    } else {
                                        getViewState().navigateToMain();
                                    }
                                },
                                throwable -> getViewState().failedToCheckBackupAvailability())
        );
    }

    public void restoreFromBackup() {
        getViewState().showBackupRestoring(true);
        unsubscribeOnDestroy(
                cloudInteractor.restore()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess(isRestored -> getViewState().showBackupRestoring(false))
                        .doOnError(throwable -> getViewState().showBackupRestoring(false))
                        .doOnError(throwable -> logger.error("restoreFromBackup", throwable))
                        .subscribe(
                                isRestored -> getViewState().backupRestored(),
                                throwable -> getViewState().failedToRestoreBackup()
                        ));
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
        boolean isAccountNameAvailable;
        int connectionStatusCode;
        String accountName;
    }
}
