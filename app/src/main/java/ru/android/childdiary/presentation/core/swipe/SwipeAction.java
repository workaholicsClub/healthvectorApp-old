package ru.android.childdiary.presentation.core.swipe;

import android.support.annotation.NonNull;

import com.daimajia.swipe.SimpleSwipeListener;

public class SwipeAction<T, IL extends ItemActionListener<T>> extends SimpleSwipeListener {
    protected final T item;
    protected final IL listener;

    public SwipeAction(@NonNull T item, @NonNull IL listener) {
        this.item = item;
        this.listener = listener;
    }
}
