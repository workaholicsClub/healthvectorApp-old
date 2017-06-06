package ru.android.childdiary.presentation.splash;

import com.arellomobile.mvp.InjectViewState;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.domain.interactors.exercises.ExerciseInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class SplashPresenter extends BasePresenter<SplashView> {
    private static final long SPLASH_TIME_IN_MILLISECONDS = 1500;

    @Inject
    ChildInteractor childInteractor;

    @Inject
    ExerciseInteractor exerciseInteractor;

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
                childInteractor.getActiveChildOnce()
                        .doOnNext(child -> logger.debug("active child: " + child)),
                (zero, child) -> child)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(child -> getViewState().navigateToMain(), this::onUnexpectedError));
    }
}
