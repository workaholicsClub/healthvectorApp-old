package ru.android.childdiary.presentation.core.adapters.swipe;

import android.support.annotation.NonNull;

public class SwipeDeleteAction<T, IL extends ItemActionListener<T>> extends SwipeAction<T, IL> {
    public SwipeDeleteAction(@NonNull T item, @NonNull IL listener) {
        super(item, listener);
    }

    @Override
    protected void doAction(IL listener, T item) {
        listener.delete(item);
    }
}
