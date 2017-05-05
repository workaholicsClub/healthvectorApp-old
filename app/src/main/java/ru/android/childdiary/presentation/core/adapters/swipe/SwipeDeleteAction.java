package ru.android.childdiary.presentation.core.adapters.swipe;

import android.support.annotation.NonNull;

import com.daimajia.swipe.SwipeLayout;

public class SwipeDeleteAction<T, IL extends ItemActionListener<T>> extends SwipeAction<T, IL> {
    public SwipeDeleteAction(@NonNull T item, @NonNull IL listener) {
        super(item, listener);
    }

    @Override
    public void onClose(SwipeLayout layout) {
        layout.removeSwipeListener(this);
        listener.delete(item);
    }
}
