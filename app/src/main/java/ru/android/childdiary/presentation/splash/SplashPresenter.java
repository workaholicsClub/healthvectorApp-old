package ru.android.childdiary.presentation.splash;

import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.repositories.core.settings.SettingsDataRepository;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.domain.interactors.core.InitializationInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class SplashPresenter extends BasePresenter<SplashView> {
    private static final long SPLASH_TIME_IN_MILLISECONDS = 1500;

    @Inject
    ChildInteractor childInteractor;

    @Inject
    InitializationInteractor initializationInteractor;

    @Inject
    SettingsDataRepository settingsRepository;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(Observable.combineLatest(
                Observable.timer(SPLASH_TIME_IN_MILLISECONDS, TimeUnit.MILLISECONDS)
                        .doOnNext(zero -> logger.debug("timer finished")),
                initializationInteractor.startUpdateDataService(),
                childInteractor.getActiveChildOnce()
                        .doOnNext(child -> logger.debug("active child: " + child)),
                settingsRepository.getAccountNameOnce()
                        .doOnNext(accountName -> logger.debug("account name: " + accountName)),
                settingsRepository.getIsCloudShownOnce()
                        .doOnNext(isCloudShown -> logger.debug("is cloud shown: " + isCloudShown)),
                (zero, isUpdateServiceStarted, child, accountName, isCloudShown) -> TextUtils.isEmpty(accountName) && !isCloudShown)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onFinished, this::onUnexpectedError));
    }

    private void onFinished(boolean showCloud) {
        if (showCloud) {
            // TODO: set is cloud shown to true
            getViewState().navigateToCloud();
        } else {
            getViewState().navigateToMain();
        }
    }
}
