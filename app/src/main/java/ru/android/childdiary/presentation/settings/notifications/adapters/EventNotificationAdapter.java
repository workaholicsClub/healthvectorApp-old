package ru.android.childdiary.presentation.settings.notifications.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.calendar.data.core.EventNotification;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;

public class EventNotificationAdapter extends BaseRecyclerViewAdapter<EventNotification, EventNotificationViewHolder> {
    @NonNull
    private ItemClickedListener listener;

    public EventNotificationAdapter(Context context, @NonNull ItemClickedListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected EventNotificationViewHolder createUserViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.notification_item, parent, false);
        return new EventNotificationViewHolder(v, listener);
    }

    @Override
    public boolean areItemsTheSame(EventNotification oldItem, EventNotification newItem) {
        EventType oldEventType = oldItem == null ? null : oldItem.getEventType();
        EventType newEventType = newItem == null ? null : newItem.getEventType();
        return oldEventType == newEventType;
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
