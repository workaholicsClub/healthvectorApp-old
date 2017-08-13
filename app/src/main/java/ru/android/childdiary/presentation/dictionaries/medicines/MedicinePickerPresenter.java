package ru.android.childdiary.presentation.dictionaries.medicines;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import lombok.Getter;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.dictionaries.medicines.MedicineInteractor;
import ru.android.childdiary.domain.dictionaries.medicines.data.Medicine;
import ru.android.childdiary.presentation.dictionaries.core.BasePickerPresenter;
import ru.android.childdiary.utils.strings.StringUtils;

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
