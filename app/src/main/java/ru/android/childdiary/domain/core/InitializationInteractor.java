package ru.android.childdiary.domain.core;

import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.services.UpdateDataService;

public class InitializationInteractor {
    private final Context context;

    @Inject
    public InitializationInteractor(Context context) {
        this.context = context;
    }

    public Observable<Boolean> startUpdateDataService() {
        return Observable.fromCallable(() -> {
            context.startService(new Intent(context, UpdateDataService.class));
            return true;
        });
    }
}
