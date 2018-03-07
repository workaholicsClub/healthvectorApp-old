package ru.android.healthvector.presentation.splash;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Builder;
import lombok.Value;
import ru.android.healthvector.data.db.exceptions.DowngradeDatabaseException;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.child.ChildInteractor;
import ru.android.healthvector.domain.core.InitializationInteractor;
import ru.android.healthvector.domain.core.settings.SettingsInteractor;
import ru.android.healthvector.presentation.core.BasePresenter;

@InjectViewState
public class SplashPresenter extends BasePresenter<SplashView> {
    private static final long SPLASH_TIME_IN_MILLISECONDS = 1500;

    @Inject
    InitializationInteractor initializationInteractor;

    @Inject
    ChildInteractor childInteractor;

    @Inject
    SettingsInteractor settingsInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        try {
            unsubscribeOnDestroy(Observable.combineLatest(
                    Observable.timer(SPLASH_TIME_IN_MILLISECONDS, TimeUnit.MILLISECONDS)
                            .doOnNext(zero -> logger.debug("timer finished")),
                    initializationInteractor.startUpdateDataService(),
                    childInteractor.getActiveChildOnce()
                            .doOnNext(child -> logger.debug("active child: " + child)),
                    settingsInteractor.getIsCloudShownOnce()
                            .doOnNext(isCloudShown -> logger.debug("is cloud shown: " + isCloudShown)),
                    settingsInteractor.getIsAppIntroShownOnce()
                            .doOnNext(isAppIntroShown -> logger.debug("is app intro shown: " + isAppIntroShown)),
                    (zero, isUpdateServiceStarted, child, isCloudShown, isAppIntroShown) -> Parameters.builder()
                            .sex(child.getSex())
                            .showAppIntro(!isAppIntroShown)
                            .showCloud(!isCloudShown)
                            .build())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onFinished, this::onUnexpectedError));
        } catch (DowngradeDatabaseException e) {
            onUnexpectedError(e);
            // ВАЖНО: при появлении новых версий БД необходимо обрабатывать исключение из onDowngrade,
            // показывая пользователю сообщение о необходимости обновления приложения из Google Play Market;
            // ситуация с onDowngrade становится возможной, т.к. пользователь может загрузить бэкап
            // из облачного хранилища с более новой версией БД из приложения старой версии
        }
    }

    private void onFinished(@NonNull Parameters parameters) {
        if (parameters.isShowAppIntro()) {
            // called on ui thread
            settingsInteractor.setIsAppIntroShown(true);
            getViewState().navigateToAppIntro(parameters.getSex());
        } else if (parameters.isShowCloud()) {
            // called on ui thread
            settingsInteractor.setIsCloudShown(true);
            getViewState().navigateToCloud(parameters.getSex());
        } else {
            getViewState().navigateToMain(parameters.getSex());
        }
    }

    @Value
    @Builder
    private static class Parameters {
        @Nullable
        Sex sex;
        boolean showAppIntro;
        boolean showCloud;
    }
}
