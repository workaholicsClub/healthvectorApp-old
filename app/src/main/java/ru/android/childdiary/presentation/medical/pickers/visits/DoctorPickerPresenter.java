package ru.android.childdiary.presentation.medical.pickers.visits;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.DoctorVisitInteractor;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.presentation.medical.pickers.core.BasePickerPresenter;

@InjectViewState
public class DoctorPickerPresenter extends BasePickerPresenter<Doctor, DoctorPickerView> {
    @Inject
    DoctorVisitInteractor doctorVisitInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected Observable<List<Doctor>> createLoader() {
        return doctorVisitInteractor.getDoctors();
    }

    @Override
    protected boolean filter(@NonNull Doctor item, @Nullable String filter) {
        return item.getName() != null
                && (TextUtils.isEmpty(filter)
                || item.getName().toLowerCase().contains(filter.toLowerCase()));
    }
}
