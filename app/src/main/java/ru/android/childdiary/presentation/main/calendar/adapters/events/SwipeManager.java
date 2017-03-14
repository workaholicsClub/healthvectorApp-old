package ru.android.childdiary.presentation.main.calendar.adapters.events;

import android.support.v7.widget.RecyclerView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;

import lombok.Setter;
import lombok.val;
import ru.android.childdiary.R;

public class SwipeManager extends SwipeItemRecyclerMangerImpl {
    private final FabController fabController;

    public SwipeManager(RecyclerView.Adapter adapter, FabController fabController) {
        super(adapter);
        this.fabController = fabController;
    }

    public void bindViewHolder(EventViewHolder viewHolder, int position) {
        super.bindView(viewHolder.itemView, position);
        SwipeLayout swipeLayout = viewHolder.swipeLayout;
        val key = getSwipeLayoutId(position) + 1;
        if (swipeLayout.getTag(key) == null) {
            SwipeListener swipeListener = new SwipeListener(position);
            swipeLayout.addSwipeListener(swipeListener);
            swipeLayout.setTag(key, swipeListener);
        } else {
            SwipeListener swipeListener = (SwipeListener) swipeLayout.getTag(key);
            swipeListener.setPosition(position);
        }
    }

    @Override
    public int getSwipeLayoutId(int position) {
        return R.id.swipeLayout;
    }

    private class SwipeListener extends SimpleSwipeListener {
        @Setter
        private int position;

        public SwipeListener(int position) {
            this.position = position;
        }

        @Override
        public void onStartOpen(SwipeLayout layout) {
            super.onStartOpen(layout);
            if (fabController != null) {
                fabController.hideFab();
            }
        }

        @Override
        public void onOpen(SwipeLayout layout) {
            super.onOpen(layout);
            if (fabController != null) {
                fabController.hideFab();
            }
        }

        @Override
        public void onStartClose(SwipeLayout layout) {
            super.onStartClose(layout);
            if (fabController != null) {
                fabController.showFab();
            }
        }

        @Override
        public void onClose(SwipeLayout layout) {
            super.onClose(layout);
            if (fabController != null) {
                fabController.showFab();
            }
        }
    }
}
