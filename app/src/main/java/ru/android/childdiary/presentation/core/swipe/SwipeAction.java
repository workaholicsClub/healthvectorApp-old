package ru.android.childdiary.presentation.core.swipe;

import android.support.annotation.NonNull;

import com.daimajia.swipe.SimpleSwipeListener;

public class SwipeAction<T, L extends ItemActionListener<T>> extends SimpleSwipeListener {
    protected final T item;
    protected final L listener;

    public SwipeAction(@NonNull T item, @NonNull L listener) {
        this.item = item;
        this.listener = listener;
    }
}
