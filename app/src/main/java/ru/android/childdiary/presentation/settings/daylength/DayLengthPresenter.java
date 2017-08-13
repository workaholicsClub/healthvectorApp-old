package ru.android.childdiary.presentation.settings.daylength;

import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import org.joda.time.LocalTime;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.core.settings.SettingsInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class DayLengthPresenter extends BasePresenter<DayLengthView> {
    @Inject
    SettingsInteractor settingsInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(settingsInteractor.getStartTime()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showStartTime, this::onUnexpectedError));

        unsubscribeOnDestroy(settingsInteractor.getFinishTime()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showFinishTime, this::onUnexpectedError));
    }

    public void save(@Nullable LocalTime startTime, @Nullable LocalTime finishTime) {
        if (startTime == null || finishTime == null) {
            logger.error("save: start time = " + startTime + ", finish time = " + finishTime);
            getViewState().exit(false);
            return;
        }
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        settingsInteractor.getStartTimeOnce()
                                .map(oldStartTime -> oldStartTime.isEqual(startTime)),
                        settingsInteractor.getFinishTimeOnce()
                                .map(oldFinishTime -> oldFinishTime.isEqual(finishTime)),
                        (startTimeNotChanged, finishTimeNotChanged) -> startTimeNotChanged && finishTimeNotChanged)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(notChanged -> {
                            if (notChanged) {
                                getViewState().exit(false);
                            } else {
                                getViewState().confirmSave(startTime, finishTime);
                            }
                        }, this::onUnexpectedError));
    }

    public void forceSave(@Nullable LocalTime startTime, @Nullable LocalTime finishTime) {
        if (startTime == null || finishTime == null) {
            logger.error("forceSave: start time = " + startTime + ", finish time = " + finishTime);
            getViewState().exit(false);
            return;
        }
        unsubscribeOnDestroy(settingsInteractor.setStartTime(startTime, finishTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::exit, this::onUnexpectedError));
    }
}
