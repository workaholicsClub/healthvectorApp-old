package ru.android.childdiary.presentation.main.calendar.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;

import java.util.Collections;
import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;

public class EventsAdapter extends RecyclerSwipeAdapter<EventViewHolder> implements EventActionAdapterListener {
    private final SwipeItemRecyclerMangerImpl swipeManager = new SwipeItemRecyclerMangerImpl(this);

    private final Context context;
    private final LayoutInflater inflater;

    private Sex sex;
    private List<MasterEvent> events = Collections.emptyList();
    private EventActionListener listener;

    public EventsAdapter(Context context, EventActionListener listener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.events = Collections.unmodifiableList(events);
        this.listener = listener;
    }

    public void setSex(@Nullable Sex sex) {
        if (this.sex != sex) {
            this.sex = sex;
            notifyDataSetChanged();
        }
    }

    public void setEvents(@NonNull List<MasterEvent> events) {
        this.events = Collections.unmodifiableList(events);
        notifyDataSetChanged();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.event_item, parent, false);
        EventViewHolder viewHolder = new EventViewHolder(v, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder viewHolder, int position) {
        viewHolder.bind(context, position, sex, events.get(position));
        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipeLayout;
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public void delete(EventViewHolder viewHolder) {
        mItemManger.closeAllItems();
        if (listener != null) {
            listener.delete(viewHolder.getEvent());
        }
    }

    @Override
    public void move(EventViewHolder viewHolder) {
        mItemManger.closeAllItems();
        if (listener != null) {
            listener.move(viewHolder.getEvent());
        }
    }

    @Override
    public void edit(EventViewHolder viewHolder) {
        mItemManger.closeAllItems();
        if (listener != null) {
            listener.edit(viewHolder.getEvent());
        }
    }

    @Override
    public void done(EventViewHolder viewHolder) {
        mItemManger.closeAllItems();
        if (listener != null) {
            listener.done(viewHolder.getEvent());
        }
    }
}
