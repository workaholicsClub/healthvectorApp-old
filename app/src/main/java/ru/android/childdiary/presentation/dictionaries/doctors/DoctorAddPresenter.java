package ru.android.childdiary.presentation.dictionaries.doctors;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.DoctorVisitInteractor;
import ru.android.childdiary.domain.interactors.dictionaries.core.MedicalDictionaryInteractor;
import ru.android.childdiary.domain.interactors.dictionaries.doctors.Doctor;
import ru.android.childdiary.presentation.dictionaries.core.BaseAddPresenter;

@InjectViewState
public class DoctorAddPresenter extends BaseAddPresenter<Doctor, DoctorAddView> {
    @Inject
    DoctorVisitInteractor doctorVisitInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void add(@NonNull Doctor item) {
        unsubscribeOnDestroy(doctorVisitInteractor.addDoctor(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::itemAdded, this::onUnexpectedError));
    }

    @Override
    protected Observable<List<Doctor>> getAllItemsLoader() {
        return doctorVisitInteractor.getDoctors();
    }

    @Override
    protected MedicalDictionaryInteractor<Doctor> getInteractor() {
        return doctorVisitInteractor;
    }
}
