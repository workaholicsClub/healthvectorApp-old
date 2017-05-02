package ru.android.childdiary.presentation.core.swipe;

public interface SwipeActionListener<VH extends SwipeViewHolder> {
    void delete(VH viewHolder);
}
