package ru.android.childdiary.presentation.development.partitions.antropometry.core;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.antropometry.AntropometryInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

public abstract class AntropometryPresenter<V extends AntropometryView> extends BasePresenter<V> {
    @Inject
    protected AntropometryInteractor antropometryInteractor;

    protected Child child;

    public void init(@NonNull Child child) {
        this.child = child;
    }
}
