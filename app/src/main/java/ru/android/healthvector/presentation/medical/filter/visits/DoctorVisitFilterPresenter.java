package ru.android.healthvector.presentation.medical.filter.visits;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.medical.DoctorVisitInteractor;
import ru.android.healthvector.domain.medical.requests.GetDoctorVisitsFilter;
import ru.android.healthvector.presentation.core.BasePresenter;

@InjectViewState
public class DoctorVisitFilterPresenter extends BasePresenter<DoctorVisitFilterView> {
    @Inject
    DoctorVisitInteractor doctorVisitInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void setFilter(@NonNull GetDoctorVisitsFilter filter) {
        unsubscribeOnDestroy(doctorVisitInteractor.setSelectedFilterValueObservable(filter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(f -> {
                }, this::onUnexpectedError));
    }
}
