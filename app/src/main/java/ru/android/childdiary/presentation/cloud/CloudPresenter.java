package ru.android.childdiary.presentation.cloud;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.DriveScopes;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.availability.NetworkAvailability;
import ru.android.childdiary.data.availability.PlayServicesAvailability;
import ru.android.childdiary.data.repositories.core.settings.SettingsDataRepository;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class CloudPresenter extends BasePresenter<CloudView> {
    private static final String[] SCOPES = {DriveScopes.DRIVE_APPDATA};

    @Inject
    Context context;

    @Inject
    NetworkAvailability networkAvailability;

    @Inject
    PlayServicesAvailability playServicesAvailability;

    @Inject
    SettingsDataRepository settingsRepository;

    private GoogleAccountCredential credential;
    private PreConditions preConditions;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        credential = GoogleAccountCredential.usingOAuth2(context, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
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
                        settingsRepository.getAccountNameOnce(),
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
            loadBackup();
        }
    }

    public void permissionGranted() {
        String accountName = preConditions.getAccountName();
        if (TextUtils.isEmpty(accountName)) {
            getViewState().chooseAccount(credential);
        } else {
            loadBackup();
        }
    }

    public void permissionDenied() {
        getViewState().navigateToMain();
    }

    private void loadBackup() {
        credential.setSelectedAccountName(preConditions.getAccountName());
        getViewState().showBackupLoading(true);
        unsubscribeOnDestroy(
                Observable.timer(5, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(result -> getViewState().showBackupLoading(false))
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

    public void accountChosen(@Nullable String account) {
        settingsRepository.setAccountName(account);
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
