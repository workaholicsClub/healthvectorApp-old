package ru.android.childdiary.presentation.core.adapters.swipe;

import android.support.annotation.Nullable;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import lombok.val;

final class SwipeManager {
    private static final int INVALID_POSITION = -1;
    private final Set<WeakReference<SwipeLayout>> shownLayouts = new HashSet<>();
    private final Set<Integer> openedOrOpeningPositions = new HashSet<>();
    private FabController fabController;
    private int openPosition = INVALID_POSITION;

    public SwipeManager(@Nullable FabController fabController) {
        this.fabController = fabController;
    }

    public void bindViewHolder(SwipeViewHolder viewHolder, int position) {
        SwipeLayout swipeLayout = viewHolder.getSwipeLayout();
        ValueBox valueBox;
        if (swipeLayout.getTag() == null) {
            valueBox = new ValueBox();
            valueBox.swipeListener = new SwipeListener();
            valueBox.onLayoutListener = new OnLayoutListener();
            swipeLayout.addSwipeListener(valueBox.swipeListener);
            swipeLayout.addOnLayoutListener(valueBox.onLayoutListener);
            shownLayouts.add(new WeakReference<>(swipeLayout));
            swipeLayout.setTag(valueBox);
        } else {
            valueBox = (ValueBox) swipeLayout.getTag();
        }
        valueBox.position = position;
        valueBox.swipeListener.position = position;
        valueBox.onLayoutListener.position = position;
    }

    public void closeAllExcept(SwipeLayout layout) {
        val iterator = shownLayouts.iterator();
        while (iterator.hasNext()) {
            SwipeLayout swipeLayout = iterator.next().get();
            if (swipeLayout == null) {
                iterator.remove();
            } else if (swipeLayout != layout) {
                swipeLayout.close();
            }
        }
    }

    public void closeAllItems() {
        openPosition = INVALID_POSITION;
        openedOrOpeningPositions.clear();
        val iterator = shownLayouts.iterator();
        while (iterator.hasNext()) {
            SwipeLayout swipeLayout = iterator.next().get();
            if (swipeLayout == null) {
                iterator.remove();
            } else {
                swipeLayout.close();
            }
        }
        showFabIfPossible();
    }

    public void setFabController(FabController fabController) {
        if (this.fabController != null && fabController == null) {
            this.fabController.hideFabBar();
        }
        this.fabController = fabController;
        updateFabState();
    }

    public void updateFabState() {
        if (hasOpenedItems()) {
            hideFab();
        } else {
            showFabIfPossible();
        }
    }

    private boolean hasOpenedItems() {
        return !openedOrOpeningPositions.isEmpty();
    }

    private void showFabIfPossible() {
        if (openedOrOpeningPositions.isEmpty()) {
            if (fabController != null) {
                fabController.showFab();
            }
        }
    }

    private void hideFab() {
        if (fabController != null) {
            fabController.hideFabBar();
        }
    }

    private static class ValueBox {
        OnLayoutListener onLayoutListener;
        SwipeListener swipeListener;
        int position;
    }

    private class OnLayoutListener implements SwipeLayout.OnLayout {
        int position;

        @Override
        public void onLayout(SwipeLayout v) {
            if (openPosition == position) {
                v.open(false, false);
            } else {
                v.close(false, false);
            }
        }
    }

    private class SwipeListener extends SimpleSwipeListener {
        int position;

        @Override
        public void onStartOpen(SwipeLayout layout) {
            closeAllExcept(layout);
            openedOrOpeningPositions.add(position);
            hideFab();
        }

        @Override
        public void onOpen(SwipeLayout layout) {
            closeAllExcept(layout);
            openPosition = position;
            openedOrOpeningPositions.add(position);
            hideFab();
        }

        @Override
        public void onClose(SwipeLayout layout) {
            openPosition = INVALID_POSITION;
            openedOrOpeningPositions.remove(position);
            showFabIfPossible();
        }
    }
}
