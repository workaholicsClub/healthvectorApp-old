package ru.android.childdiary.presentation.splash;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class SplashPresenter extends BasePresenter<SplashView> {
    private static final long SPLASH_TIME_IN_MILLISECONDS = 1500;

    @Inject
    ChildInteractor childInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(
                Observable.combineLatest(
                        Observable
                                .timer(SPLASH_TIME_IN_MILLISECONDS, TimeUnit.MILLISECONDS)
                                .doOnNext(zero -> logger.debug("timer finished")),
                        childInteractor
                                .getAll()
                                .doOnNext(childList -> logger.debug("data loaded")),
                        (zero, childList) -> childList)
                        .doOnNext(childList -> logger.debug("combine latest"))
                        .flatMap(childList -> childInteractor.getActiveChild(childList))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(child -> {
                            if (child == Child.NULL) {
                                startAppWithDefaultTheme();
                            } else {
                                startApp(child);
                            }
                        }, this::onUnexpectedError));
    }

    private void startApp(@NonNull Child child) {
        logger.debug("startApp");
        getViewState().startApp(child);
    }

    private void startAppWithDefaultTheme() {
        logger.debug("startAppWithDefaultTheme");
        getViewState().startApp(null);
    }
}
