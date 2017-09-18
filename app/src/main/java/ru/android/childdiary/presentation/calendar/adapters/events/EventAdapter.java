package ru.android.childdiary.presentation.calendar.adapters.events;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.childdiary.utils.strings.EventUtils;

public class EventAdapter extends SwipeViewAdapter<
        MasterEvent,
        EventViewHolder,
        EventSwipeActionListener,
        EventActionListener>
        implements EventSwipeActionListener {
    public EventAdapter(Context context, @NonNull EventActionListener itemActionListener, @Nullable FabController fabController) {
        super(context, itemActionListener, fabController);
    }

    @Override
    public boolean areItemsTheSame(MasterEvent oldItem, MasterEvent newItem) {
        return EventUtils.sameEvent(oldItem, newItem);
    }

    @Override
    protected int getUserViewType(int position) {
        return items.get(position).getEventType().ordinal();
    }

    @Override
    protected EventViewHolder createUserViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(v, itemActionListener, this, sex, EventType.values()[viewType]);
    }

    @Override
    public void done(EventViewHolder viewHolder) {
        viewHolder.swipeLayout.addSwipeListener(new SwipeDoneAction(viewHolder.getItem(), itemActionListener));
        closeAllItems();
    }

    @Override
    public void move(EventViewHolder viewHolder) {
        viewHolder.swipeLayout.addSwipeListener(new SwipeMoveAction(viewHolder.getItem(), itemActionListener));
        closeAllItems();
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
