package ru.android.childdiary.presentation.splash;

import com.arellomobile.mvp.InjectViewState;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.utils.StringUtils;

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
                                .doOnNext(childList -> logger.debug("data loaded: " + StringUtils.toString(childList)))
                                .flatMap(childInteractor::getActiveChild)
                                .doOnNext(child -> logger.debug("active child: " + child)),
                        (zero, child) -> child)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(child -> startApp(), this::onUnexpectedError));
    }

    private void startApp() {
        logger.debug("startApp");
        getViewState().startApp();
    }
}
