package ru.android.healthvector.presentation.calendar.adapters.filter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;

public class EventFilterAdapter extends BaseRecyclerViewAdapter<EventType, EventFilterViewHolder>
        implements ItemSelectedListener {
    @Getter
    private final Set<EventType> selectedItems = new HashSet<>();

    public EventFilterAdapter(Context context, @NonNull Set<EventType> selectedItems) {
        super(context);
        this.selectedItems.addAll(selectedItems);
    }

    @Override
    protected EventFilterViewHolder createUserViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.event_filter_item, parent, false);
        return new EventFilterViewHolder(v, this);
    }

    @Override
    protected final void bindUserViewHolder(EventFilterViewHolder viewHolder, int position) {
        super.bindUserViewHolder(viewHolder, position);
        boolean selected = selectedItems.contains(items.get(position));
        viewHolder.setSelected(selected);
    }

    @Override
    public boolean areItemsTheSame(EventType oldItem, EventType newItem) {
        return oldItem == newItem;
    }

    @Override
    public void onItemSelected(@NonNull EventType eventType, boolean selected) {
        if (selected) {
            selectedItems.add(eventType);
        } else {
            selectedItems.remove(eventType);
        }
    }

    @Override
    public boolean paintDividers() {
        return false;
    }

    @Override
    public boolean useFooter() {
        return false;
    }
}
