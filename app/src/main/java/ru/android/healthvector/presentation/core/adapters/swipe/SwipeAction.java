package ru.android.healthvector.presentation.core.adapters.swipe;

import android.support.annotation.NonNull;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;

import java.lang.ref.WeakReference;

public abstract class SwipeAction<T, IL extends ItemActionListener<T>> extends SimpleSwipeListener {
    private final T item;
    private final WeakReference<IL> listenerReference;
    private boolean done;

    public SwipeAction(@NonNull T item, @NonNull IL listener) {
        this.item = item;
        this.listenerReference = new WeakReference<>(listener);
    }

    @Override
    public final void onClose(SwipeLayout layout) {
        // don't remove listener here! do action only once
        if (done) {
            return;
        }
        done = true;
        IL listener = listenerReference.get();
        if (listener != null) {
            doAction(listener, item);
        }
    }

    protected abstract void doAction(IL listener, T item);
}
