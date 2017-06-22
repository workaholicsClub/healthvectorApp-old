package ru.android.childdiary.presentation.cloud;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.google.android.gms.common.ConnectionResult;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.availability.NetworkAvailability;
import ru.android.childdiary.data.availability.NetworkUnavailableException;
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

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void moveNext() {
        getViewState().navigateToMain();
    }

    public void bindAccount() {
        unsubscribeOnDestroy(
                playServicesAvailability.checkPlayServicesAvailability()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                connectionStatusCode -> {
                                    if (connectionStatusCode == ConnectionResult.SUCCESS) {
                                        getViewState().requestPermission();
                                    } else {
                                        getViewState().showPlayServicesErrorDialog(connectionStatusCode);
                                    }
                                },
                                this::onUnexpectedError));
    }

    public void permissionGranted() {
        unsubscribeOnDestroy(
                cloudInteractor.getAccountNameOnce()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                accountName -> getViewState().chooseAccount(accountName),
                                this::onUnexpectedError));
    }

    public void permissionDenied() {
        getViewState().navigateToMain();
    }

    public void accountChosen(@Nullable String accountName) {
        cloudInteractor.setAccountName(accountName);
        checkIsBackupAvailable();
    }

    public void checkIsBackupAvailable() {
        // проверяем сперва доступность интернета, т.к. в google drive api установлено большое
        // количество повторных попыток, что приводит к длительному ожиданию пользователя
        // ИЛИ надо делать возможность отмены операции пользователем
        getViewState().showBackupLoading(true);
        unsubscribeOnDestroy(
                networkAvailability.checkNetworkAvailability(true)
                        .flatMap(isNetworkAvailable -> cloudInteractor.checkIsBackupAvailable())
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
                                throwable -> {
                                    boolean processed = processGoogleDriveError(throwable, CloudOperation.CHECK);
                                    if (!processed) {
                                        getViewState().failedToCheckBackupAvailability();
                                    }
                                }
                        ));
    }

    public void restoreFromBackup() {
        getViewState().showBackupRestoring(true);
        unsubscribeOnDestroy(
                networkAvailability.checkNetworkAvailability(true)
                        .flatMap(isNetworkAvailable -> cloudInteractor.restore())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess(isRestored -> getViewState().showBackupRestoring(false))
                        .doOnError(throwable -> getViewState().showBackupRestoring(false))
                        .doOnError(throwable -> logger.error("restoreFromBackup", throwable))
                        .subscribe(
                                isRestored -> getViewState().backupRestored(),
                                throwable -> {
                                    boolean processed = processGoogleDriveError(throwable, CloudOperation.RESTORE);
                                    if (!processed) {
                                        getViewState().failedToRestoreBackup();
                                    }
                                }
                        ));
    }

    private boolean processGoogleDriveError(Throwable throwable, CloudOperation cloudOperation) {
        if (throwable instanceof GooglePlayServicesAvailabilityIOException) {
            int connectionStatusCode = ((GooglePlayServicesAvailabilityIOException) throwable).getConnectionStatusCode();
            getViewState().showPlayServicesErrorDialog(connectionStatusCode);
            return true;
        } else if (throwable instanceof UserRecoverableAuthIOException) {
            Intent intent = ((UserRecoverableAuthIOException) throwable).getIntent();
            getViewState().requestAuthorization(intent);
            return true;
        } else if (throwable instanceof NetworkUnavailableException) {
            getViewState().connectionUnavailable(cloudOperation);
            return true;
        }
        return false;
    }
}
