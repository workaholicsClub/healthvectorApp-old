package ru.android.childdiary.presentation.medical.filter.visits;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.medical.DoctorVisitInteractor;
import ru.android.childdiary.domain.medical.requests.GetDoctorVisitsFilter;
import ru.android.childdiary.presentation.core.BasePresenter;

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
