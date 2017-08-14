package ru.android.childdiary.domain.core;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.services.ServiceController;

public class InitializationInteractor {
    private final ServiceController serviceController;

    @Inject
    public InitializationInteractor(ServiceController serviceController) {
        this.serviceController = serviceController;
    }

    public Observable<Boolean> startUpdateDataService() {
        return Observable.fromCallable(() -> {
            serviceController.updateData();
            return true;
        });
    }
}
