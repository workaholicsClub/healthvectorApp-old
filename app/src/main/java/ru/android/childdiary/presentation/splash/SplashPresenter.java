package ru.android.childdiary.presentation.splash;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.types.Sex;
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
                                .doOnNext(childList -> logger.debug("data loaded"))
                                .subscribeOn(Schedulers.io()),
                        (zero, childList) -> childList)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onGetChildList, this::onUnexpectedError));
    }

    private void onGetChildList(List<Child> childList) {
        logger.debug("onGetChildList");
        // TODO: брать последний активный профиль из настроек
        Child lastActiveChild = childList.isEmpty() ? null : childList.get(0);
        Sex sex = lastActiveChild == null ? null : lastActiveChild.getSex();
        getViewState().startApp(sex);
    }
}
