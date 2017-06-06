package ru.android.childdiary.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ru.android.childdiary.app.ChildDiaryApplication;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.exercises.Exercise;
import ru.android.childdiary.domain.interactors.exercises.ExerciseInteractor;
import ru.android.childdiary.utils.StringUtils;
import ru.android.childdiary.utils.log.LogSystem;

public class UpdateDataService extends IntentService {
    private final Logger logger = LoggerFactory.getLogger(toString());

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    ExerciseInteractor exerciseInteractor;

    public UpdateDataService() {
        super("UpdateDataService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logger.debug("onCreate");

        ApplicationComponent component = ChildDiaryApplication.getApplicationComponent();
        component.inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logger.debug("onDestroy");

        compositeDisposable.dispose();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        logger.debug("onHandleIntent: " + intent);

        unsubscribeOnDestroy(exerciseInteractor.updateExercisesIfNeeded()
                .subscribe(this::handleResult, this::onUnexpectedError));
    }

    private void handleResult(@NonNull List<Exercise> exercises) {
        logger.debug("received result: " + StringUtils.toString(exercises));
    }

    private void onUnexpectedError(Throwable e) {
        LogSystem.report(logger, "unexpected error", e);
    }

    private void unsubscribe(Disposable subscription) {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    private Disposable unsubscribeOnDestroy(@NonNull Disposable disposable) {
        compositeDisposable.add(disposable);
        return disposable;
    }
}
