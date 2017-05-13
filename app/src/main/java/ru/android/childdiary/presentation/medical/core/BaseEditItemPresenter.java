package ru.android.childdiary.presentation.medical.core;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.io.Serializable;

public abstract class BaseEditItemPresenter<V extends BaseEditItemView<T>, T extends Serializable> extends BaseItemPresenter<V, T> {
    public abstract void update(@NonNull T item);

    public abstract void delete(@NonNull T item);

    public abstract void deleteOneItem(@NonNull T item);

    public abstract void deleteWithConnectedEvents(@NonNull T item);

    public abstract void complete(@NonNull T item);

    public abstract void completeWithoutDeletion(@NonNull T item, @NonNull DateTime dateTime);

    public abstract void completeAndDeleteFromDate(@NonNull T item, @NonNull DateTime dateTime);
}
