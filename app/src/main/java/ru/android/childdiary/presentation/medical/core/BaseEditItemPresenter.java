package ru.android.childdiary.presentation.medical.core;

import android.support.annotation.NonNull;

import java.io.Serializable;

public abstract class BaseEditItemPresenter<V extends BaseEditItemView<T>, T extends Serializable> extends BaseItemPresenter<V, T> {
    public abstract void update(@NonNull T item);

    public abstract void delete(@NonNull T item);
}
