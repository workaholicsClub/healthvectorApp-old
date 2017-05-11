package ru.android.childdiary.presentation.core.events;

import android.support.annotation.NonNull;

import java.io.Serializable;

public abstract class BaseEditItemPresenter<V extends BaseEditItemView<T>, T extends Serializable> extends BaseItemPresenter<V, T> {
    public abstract void update(@NonNull T item);

    public abstract void delete(@NonNull T item);
}
