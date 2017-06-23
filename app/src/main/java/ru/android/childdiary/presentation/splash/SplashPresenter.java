package ru.android.childdiary.presentation.splash;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.BuildConfig;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.cloud.CloudInteractor;
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
    CloudInteractor cloudInteractor;

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
                cloudInteractor.getAccountNameOnce()
                        .doOnNext(accountName -> logger.debug("account name: " + accountName)),
                cloudInteractor.getIsCloudShownOnce()
                        .doOnNext(isCloudShown -> logger.debug("is cloud shown: " + isCloudShown)),
                (zero, isUpdateServiceStarted, child, accountName, isCloudShown) -> Parameters.builder()
                        .sex(child.getSex())
                        .showCloud(TextUtils.isEmpty(accountName) && !isCloudShown)
                        .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onFinished, this::onUnexpectedError));
    }

    private void onFinished(@NonNull Parameters parameters) {
        if (parameters.isShowCloud() || BuildConfig.SHOW_CLOUD_ON_EACH_START) {
            cloudInteractor.setIsCloudShown(true);
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
        boolean showCloud;
    }
}
