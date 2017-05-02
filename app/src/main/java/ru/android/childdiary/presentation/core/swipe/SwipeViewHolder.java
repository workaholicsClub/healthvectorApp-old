package ru.android.childdiary.presentation.core.swipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.daimajia.swipe.SwipeLayout;

import butterknife.ButterKnife;
import lombok.Getter;
import ru.android.childdiary.data.types.Sex;

public abstract class SwipeViewHolder<T,
        SL extends SwipeActionListener<? extends SwipeViewHolder<T, SL, IL>>,
        IL extends ItemActionListener<T>>
        extends RecyclerView.ViewHolder {
    @Getter
    protected T item;
    protected IL itemActionListener;
    protected SL swipeActionListener;

    public SwipeViewHolder(View itemView, @NonNull IL itemActionListener, @NonNull SL swipeActionListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.itemActionListener = itemActionListener;
        this.swipeActionListener = swipeActionListener;
    }

    public void bind(Context context, Sex sex, T item) {
        this.item = item;
    }

    public void updatePartially(Context context, T item) {
    }

    public abstract SwipeLayout getSwipeLayout();
}
