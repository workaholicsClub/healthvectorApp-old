package ru.android.healthvector.services;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.core.exceptions.TryCountExceededException;
import ru.android.healthvector.domain.exercises.ExerciseInteractor;
import ru.android.healthvector.domain.exercises.data.Exercise;
import ru.android.healthvector.services.core.BaseService;
import ru.android.healthvector.utils.log.LogSystem;

public class UpdateDataService extends BaseService {
    @Inject
    ExerciseInteractor exerciseInteractor;

    private static Intent getServiceIntent(Context context) {
        return new Intent(context, UpdateDataService.class);
    }

    public static void startService(Context context) {
        Intent intent = getServiceIntent(context);
        context.startService(intent);
    }

    @Nullable
    @Override
    protected IBinder getBinder() {
        return null;
    }

    @Override
    protected void onCreate(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void handleIntent(@Nullable Intent intent) {
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
}
