package ru.android.childdiary.presentation.onboarding;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.child.ChildInteractor;
import ru.android.childdiary.domain.core.settings.SettingsInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class AppIntroPresenter extends BasePresenter<AppIntroView> {
    @Inject
    ChildInteractor childInteractor;

    @Inject
    SettingsInteractor settingsInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void skipPressed() {
        next();
    }

    public void donePressed() {
        next();
    }

    private void next() {
        unsubscribeOnDestroy(Observable.combineLatest(
                childInteractor.getActiveChildOnce()
                        .doOnNext(child -> logger.debug("active child: " + child)),
                settingsInteractor.getIsCloudShownOnce()
                        .doOnNext(isCloudShown -> logger.debug("is cloud shown: " + isCloudShown)),
                (child, isCloudShown) -> Parameters.builder()
                        .sex(child.getSex())
                        .showCloud(!isCloudShown)
                        .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onFinished, this::onUnexpectedError));
    }

    private void onFinished(@NonNull Parameters parameters) {
        if (parameters.isShowCloud()) {
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
        boolean showCloud;
    }
}
