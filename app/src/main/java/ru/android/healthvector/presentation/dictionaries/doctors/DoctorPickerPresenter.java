package ru.android.healthvector.presentation.dictionaries.doctors;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import lombok.Getter;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.dictionaries.doctors.DoctorInteractor;
import ru.android.healthvector.domain.dictionaries.doctors.data.Doctor;
import ru.android.healthvector.presentation.dictionaries.core.BasePickerPresenter;
import ru.android.healthvector.utils.strings.StringUtils;

@InjectViewState
public class DoctorPickerPresenter extends BasePickerPresenter<Doctor, DoctorPickerView> {
    @Getter
    @Inject
    DoctorInteractor interactor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected boolean filter(@NonNull Doctor item, @Nullable String filter) {
        return StringUtils.contains(item.getName(), filter, true);
    }
}
