package ru.android.childdiary.presentation.splash;

import com.arellomobile.mvp.InjectViewState;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.android.childdiary.app.ChildDiaryApplication;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.presentation.core.navigation.NavigationController;

@InjectViewState
public class SplashPresenter extends BasePresenter<SplashView> {
    private static final long SPLASH_TIME_IN_SECONDS = 3;

    @Inject
    NavigationController navigationController;

    public SplashPresenter() {
        ChildDiaryApplication.getApplicationComponent().inject(this);
    }

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
