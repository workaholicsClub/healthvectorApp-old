package ru.android.childdiary.presentation.splash;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;
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

    private boolean isTimerFinished, isInitialized;

    private List<Child> childList;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(Observable.timer(SPLASH_TIME_IN_MILLISECONDS, TimeUnit.MILLISECONDS)
                .doOnNext(time -> logger.debug("timer finished"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(time -> onTimerFinished(), this::onUnexpectedError));

        unsubscribeOnDestroy(childInteractor.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetChildList, this::onUnexpectedError));
    }

    private void onTimerFinished() {
        logger.debug("onTimerFinished");
        isTimerFinished = true;
        next();
    }

    private void onGetChildList(List<Child> childList) {
        logger.debug("onGetChildList");
        this.childList = childList;
        next();
    }

    private void next() {
        if (childList != null && isTimerFinished) {
            boolean hasChildren = !childList.isEmpty();
            if (hasChildren) {
                // TODO: текущий профиль брать из настроек
                getViewState().navigateToMain(childList.get(0), childList);
                getViewState().finish();
            } else {
                getViewState().navigateToProfileEdit(null);
                getViewState().finish();
            }
        }
    }
}
