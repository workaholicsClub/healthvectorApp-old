package ru.android.childdiary.presentation.core.swipe;

import android.support.annotation.NonNull;

import com.daimajia.swipe.SwipeLayout;

public class SwipeDeleteAction<T, L extends ItemActionListener<T>> extends SwipeAction<T, L> {
    public SwipeDeleteAction(@NonNull T item, @NonNull L listener) {
        super(item, listener);
    }

    @Override
    public void onClose(SwipeLayout layout) {
        layout.removeSwipeListener(this);
        listener.delete(item);
    }
}
