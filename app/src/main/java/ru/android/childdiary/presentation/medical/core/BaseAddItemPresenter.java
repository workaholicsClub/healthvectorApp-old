package ru.android.childdiary.presentation.medical.core;

import android.support.annotation.NonNull;

import java.io.Serializable;

public abstract class BaseAddItemPresenter<V extends BaseAddItemView<T>, T extends Serializable> extends BaseItemPresenter<V, T> {
    public abstract void add(@NonNull T item);
}
