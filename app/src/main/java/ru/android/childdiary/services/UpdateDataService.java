package ru.android.childdiary.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.app.ChildDiaryApplication;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.core.TryCountExceededException;
import ru.android.childdiary.domain.interactors.exercises.Exercise;
import ru.android.childdiary.domain.interactors.exercises.ExerciseInteractor;
import ru.android.childdiary.utils.log.LogSystem;

public class UpdateDataService extends Service {
    private final Logger logger = LoggerFactory.getLogger(toString());

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    ExerciseInteractor exerciseInteractor;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        logger.debug("onStartCommand: " + intent);

        handleIntent(intent);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        logger.debug("onBind: " + intent);
        return null;
    }

    @Override
    public void onCreate() {
        logger.debug("onCreate");

        ApplicationComponent component = ChildDiaryApplication.getApplicationComponent();
        component.inject(this);
    }

    @Override
    public void onDestroy() {
        logger.debug("onDestroy");

        compositeDisposable.dispose();
    }

    private void handleIntent(@Nullable Intent intent) {
        unsubscribeOnDestroy(exerciseInteractor.updateExercisesIfNeeded()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResult, this::onUnexpectedError));
    }

    private void handleResult(@NonNull List<Exercise> exercises) {
        logger.debug("data received");
        stopSelf();
    }

    private void onUnexpectedError(Throwable e) {
        if (e instanceof TryCountExceededException) {
            logger.debug("data not received");
        } else {
            LogSystem.report(logger, "unexpected error", e);
        }

        stopSelf();
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
