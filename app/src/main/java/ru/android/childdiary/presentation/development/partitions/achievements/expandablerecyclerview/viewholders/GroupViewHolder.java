package ru.android.childdiary.presentation.development.partitions.achievements.expandablerecyclerview.viewholders;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import lombok.Setter;
import ru.android.childdiary.presentation.development.partitions.achievements.expandablerecyclerview.listeners.OnGroupClickListener;
import ru.android.childdiary.presentation.development.partitions.achievements.expandablerecyclerview.models.ExpandableGroup;

/**
 * ViewHolder for the {@link ExpandableGroup#title} in a {@link ExpandableGroup}.
 * <p>
 * The current implementation does now allow for sub {@link View} of the parent view to trigger
 * a collapse/expand. <b>Only</b> click events on the parent {@link View} will trigger a collapse or
 * expand.
 */
public abstract class GroupViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
    @Nullable
    @Setter
    private OnGroupClickListener onGroupClickListener;

    public GroupViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (onGroupClickListener != null) {
            onGroupClickListener.onGroupClick(getAdapterPosition());
        }
    }

    public void expand() {
    }

    public void collapse() {
    }
}
