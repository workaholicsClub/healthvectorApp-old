package ru.android.childdiary.presentation.splash;

import com.arellomobile.mvp.InjectViewState;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
//            getViewState().navigateToProfileEdit(null);

            final int CHILD_COUNT = 4;
            final Random RANDOM = new Random();

            LocalDateTime now = LocalDateTime.now();

            List<Child> result = new ArrayList<>();
            for (int i = 0; i < CHILD_COUNT; ++i) {
                now = now.minusDays(i);
                now = now.minusHours(i);

                Child child = Child.builder()
                        .id(i)
                        .name("Test" + i)
                        .birthDate(now.toLocalDate())
                        .birthTime(new LocalTime(now.getHourOfDay(), now.getMinuteOfHour()))
                        .sex(i % 2 == 0 ? Sex.MALE : Sex.FEMALE)
                        .imageFileName(null)
                        .height(RANDOM.nextDouble())
                        .weight(RANDOM.nextDouble())
                        .build();

                result.add(child);
            }

            getViewState().navigateToMain(result.get(0), result);
            getViewState().finish();
        }
    }
}
