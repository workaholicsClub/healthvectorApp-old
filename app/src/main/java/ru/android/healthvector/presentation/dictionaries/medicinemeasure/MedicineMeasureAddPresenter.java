package ru.android.healthvector.presentation.dictionaries.medicinemeasure;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import lombok.Getter;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.dictionaries.core.validation.DictionaryFieldType;
import ru.android.healthvector.domain.dictionaries.medicinemeasure.MedicineMeasureInteractor;
import ru.android.healthvector.domain.dictionaries.medicinemeasure.data.MedicineMeasure;
import ru.android.healthvector.presentation.dictionaries.core.BaseAddPresenter;

@InjectViewState
public class MedicineMeasureAddPresenter extends BaseAddPresenter<MedicineMeasure, MedicineMeasureAddView> {
    @Getter
    @Inject
    MedicineMeasureInteractor interactor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected DictionaryFieldType getNameFieldType() {
        return DictionaryFieldType.MEDICINE_MEASURE_NAME;
    }
}
