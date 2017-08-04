package ru.android.childdiary.presentation.dictionaries.medicinemeasure;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import lombok.Getter;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.dictionaries.core.validation.DictionaryFieldType;
import ru.android.childdiary.domain.interactors.dictionaries.medicinemeasure.MedicineMeasureInteractor;
import ru.android.childdiary.domain.interactors.dictionaries.medicinemeasure.data.MedicineMeasure;
import ru.android.childdiary.presentation.dictionaries.core.BaseAddPresenter;

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
