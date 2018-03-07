package ru.android.healthvector.presentation.dictionaries.medicines;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import lombok.Getter;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.dictionaries.medicines.MedicineInteractor;
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;
import ru.android.healthvector.presentation.dictionaries.core.BasePickerPresenter;
import ru.android.healthvector.utils.strings.StringUtils;

@InjectViewState
public class MedicinePickerPresenter extends BasePickerPresenter<Medicine, MedicinePickerView> {
    @Getter
    @Inject
    MedicineInteractor interactor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected boolean filter(@NonNull Medicine item, @Nullable String filter) {
        return StringUtils.contains(item.getName(), filter, true);
    }
}
