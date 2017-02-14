package ru.android.childdiary.presentation.splash;

import com.arellomobile.mvp.InjectViewState;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class SplashPresenter extends BasePresenter<SplashView> {
    private static final long SPLASH_TIME_IN_SECONDS = 3;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(Observable.timer(SPLASH_TIME_IN_SECONDS, TimeUnit.SECONDS)
                .doOnNext(time -> logger.debug("timer finished"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(time -> onInitialized(), this::onUnexpectedError));
    }

    private void onInitialized() {
        logger.debug("onInitialized");
        getViewState().navigateToMain();
        getViewState().finish();
    }
}
