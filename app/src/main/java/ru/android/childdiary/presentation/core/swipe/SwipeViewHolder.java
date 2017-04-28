package ru.android.childdiary.presentation.core.swipe;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.daimajia.swipe.SwipeLayout;

public abstract class SwipeViewHolder extends RecyclerView.ViewHolder {
    public SwipeViewHolder(View itemView) {
        super(itemView);
    }

    public abstract SwipeLayout getSwipeLayout();
}
