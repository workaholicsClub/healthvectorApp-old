package ru.android.childdiary.presentation.pickers.doctors;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.DoctorVisitInteractor;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.presentation.pickers.core.BasePickerPresenter;
import ru.android.childdiary.utils.strings.StringUtils;

@InjectViewState
public class DoctorPickerPresenter extends BasePickerPresenter<Doctor, DoctorPickerView> {
    @Inject
    DoctorVisitInteractor doctorVisitInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected Observable<List<Doctor>> getAllItemsLoader() {
        return doctorVisitInteractor.getDoctors();
    }

    @Override
    protected boolean filter(@NonNull Doctor item, @Nullable String filter) {
        return StringUtils.contains(item.getName(), filter, true);
    }

    @Override
    protected Observable<Doctor> deleteItemLoader(@NonNull Doctor item) {
        return doctorVisitInteractor.deleteDoctor(item);
    }
}
