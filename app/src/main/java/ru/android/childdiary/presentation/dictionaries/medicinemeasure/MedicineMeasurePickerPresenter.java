package ru.android.childdiary.presentation.dictionaries.medicinemeasure;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import lombok.Getter;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.dictionaries.medicinemeasure.MedicineMeasureInteractor;
import ru.android.childdiary.domain.interactors.dictionaries.medicinemeasure.data.MedicineMeasure;
import ru.android.childdiary.presentation.dictionaries.core.BasePickerPresenter;
import ru.android.childdiary.utils.strings.StringUtils;

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
