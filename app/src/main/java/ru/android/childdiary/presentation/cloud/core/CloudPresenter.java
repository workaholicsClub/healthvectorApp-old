package ru.android.childdiary.presentation.cloud.core;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.availability.NetworkAvailability;
import ru.android.childdiary.data.availability.NetworkUnavailableException;
import ru.android.childdiary.data.availability.PlayServicesAvailability;
import ru.android.childdiary.data.cloud.BackupUnavailableException;
import ru.android.childdiary.domain.cloud.AccountNameNotSpecifiedException;
import ru.android.childdiary.domain.cloud.CloudInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

public abstract class CloudPresenter<T extends CloudView> extends BasePresenter<T> {
    @Inject
    protected CloudInteractor cloudInteractor;
    protected boolean needAppRestart;

    @Inject
    PlayServicesAvailability playServicesAvailability;

    @Inject
    NetworkAvailability networkAvailability;

    private State state = State.BIND_ACCOUNT;

    public abstract void moveNext();

    public void continueAfterErrorResolved() {
        switch (state) {
            case BIND_ACCOUNT:
                bindAccount();
                break;
            case AUTHORIZE:
                checkIsBackupAvailable();
                break;
            case RESTORE:
                restore();
                break;
            case BACKUP:
                backup();
                break;
        }
    }

    public void bindAccount() {
        state = State.BIND_ACCOUNT;
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
        moveNext();
    }

    public void accountChosen(@Nullable String accountName) {
        cloudInteractor.setAccountName(accountName);
        checkIsBackupAvailable();
    }

    public void checkIsBackupAvailable() {
        state = State.AUTHORIZE;
        // проверяем сперва доступность интернета, т.к. в google drive api установлено большое
        // количество повторных попыток, что приводит к длительному ожиданию пользователя
        // ИЛИ надо делать возможность отмены операции пользователем
        getViewState().showCheckBackupAvailabilityLoading(true);
        unsubscribeOnDestroy(
                networkAvailability.checkNetworkAvailability(true)
                        .flatMap(isNetworkAvailable -> cloudInteractor.checkIsBackupAvailable())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess(isBackupAvailable -> getViewState().showCheckBackupAvailabilityLoading(false))
                        .doOnError(throwable -> getViewState().showCheckBackupAvailabilityLoading(false))
                        .doOnError(throwable -> logger.error("checkIsBackupAvailable", throwable))
                        .subscribe(
                                isBackupAvailable -> getViewState().checkBackupAvailabilitySucceeded(isBackupAvailable),
                                throwable -> {
                                    boolean processed = processGoogleDriveError(throwable);
                                    if (!processed) {
                                        getViewState().failedToCheckBackupAvailability();
                                    }
                                }
                        ));
    }

    public void restore() {
        state = State.RESTORE;
        getViewState().showRestoreLoading(true);
        unsubscribeOnDestroy(
                networkAvailability.checkNetworkAvailability(true)
                        .flatMap(isNetworkAvailable -> cloudInteractor.restore())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess(isRestored -> getViewState().showRestoreLoading(false))
                        .doOnError(throwable -> getViewState().showRestoreLoading(false))
                        .doOnError(throwable -> logger.error("restore", throwable))
                        .subscribe(
                                isRestored -> {
                                    getViewState().restoreSucceeded();
                                    needAppRestart = true;
                                },
                                throwable -> {
                                    boolean processed = processGoogleDriveError(throwable);
                                    if (!processed) {
                                        getViewState().failedToRestore();
                                    }
                                }
                        ));
    }

    public void backup() {
        state = State.BACKUP;
        getViewState().showBackupLoading(true);
        unsubscribeOnDestroy(
                networkAvailability.checkNetworkAvailability(true)
                        .flatMap(isNetworkAvailable -> cloudInteractor.backup())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess(isRestored -> getViewState().showBackupLoading(false))
                        .doOnError(throwable -> getViewState().showBackupLoading(false))
                        .doOnError(throwable -> logger.error("backup", throwable))
                        .subscribe(
                                isRestored -> getViewState().backupSucceeded(),
                                throwable -> {
                                    boolean processed = processGoogleDriveError(throwable);
                                    if (!processed) {
                                        getViewState().failedToBackup();
                                    }
                                }
                        ));
    }

    private boolean processGoogleDriveError(Throwable throwable) {
        if (throwable instanceof GooglePlayServicesAvailabilityIOException) {
            int connectionStatusCode = ((GooglePlayServicesAvailabilityIOException) throwable).getConnectionStatusCode();
            getViewState().showPlayServicesErrorDialog(connectionStatusCode);
            return true;
        } else if (throwable instanceof UserRecoverableAuthIOException) {
            Intent intent = ((UserRecoverableAuthIOException) throwable).getIntent();
            getViewState().requestAuthorization(intent);
            return true;
        } else if (throwable instanceof NetworkUnavailableException) {
            getViewState().connectionUnavailable();
            return true;
        } else if (throwable instanceof AccountNameNotSpecifiedException) {
            bindAccount();
            return true;
        } else if (throwable instanceof BackupUnavailableException) {
            getViewState().noBackupFound();
            return true;
        } else if (throwable instanceof SecurityException) {
            getViewState().securityError();
            return true;
        }
        return false;
    }

    private enum State {BIND_ACCOUNT, AUTHORIZE, RESTORE, BACKUP}
}
