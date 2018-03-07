package ru.android.healthvector.presentation.dictionaries.medicinemeasure;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import lombok.Getter;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.dictionaries.medicinemeasure.MedicineMeasureInteractor;
import ru.android.healthvector.domain.dictionaries.medicinemeasure.data.MedicineMeasure;
import ru.android.healthvector.presentation.dictionaries.core.BasePickerPresenter;
import ru.android.healthvector.utils.strings.StringUtils;

@InjectViewState
public class MedicineMeasurePickerPresenter extends BasePickerPresenter<MedicineMeasure, MedicineMeasurePickerView> {
    @Getter
    @Inject
    MedicineMeasureInteractor interactor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected boolean filter(@NonNull MedicineMeasure item, @Nullable String filter) {
        return StringUtils.contains(item.getName(), filter, true);
    }
}
