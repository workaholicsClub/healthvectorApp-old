package ru.android.healthvector.presentation.settings.notifications.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import ru.android.healthvector.R;
import ru.android.healthvector.domain.calendar.data.core.EventNotification;
import ru.android.healthvector.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;

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
        return oldItem.getEventType() == newItem.getEventType();
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
