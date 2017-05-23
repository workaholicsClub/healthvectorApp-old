package ru.android.childdiary.presentation.medical.partitions.core;

import android.support.annotation.NonNull;

import java.util.List;

import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.presentation.medical.filter.adapters.Chips;

public abstract class BaseMedicalDataPresenter<V extends BaseMedicalDataView> extends BasePresenter<V> {
    public abstract void requestFilterDialog();

    public abstract void setFilter(@NonNull List<Chips> chips);
}
