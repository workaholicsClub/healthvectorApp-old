package ru.android.childdiary.presentation.splash;

import com.arellomobile.mvp.InjectViewState;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.app.ChildDiaryApplication;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.presentation.core.navigation.NavigationController;

@InjectViewState
public class SplashPresenter extends BasePresenter<SplashView> {
    @Inject
    NavigationController navigationController;

    public SplashPresenter() {
        ChildDiaryApplication.getApplicationComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(Observable.timer(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(time -> onInitialized(), this::onUnexpectedError));
    }

    private void onInitialized() {
        getViewState().navigateToMain();
        getViewState().finish();
    }
}
