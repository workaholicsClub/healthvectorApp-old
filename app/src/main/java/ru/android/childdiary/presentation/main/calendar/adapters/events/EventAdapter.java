package ru.android.childdiary.presentation.main.calendar.adapters.events;

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

public class EventAdapter extends RecyclerSwipeAdapter<EventViewHolder> implements EventViewHolder.SwipeActionListener {
    private final SwipeItemRecyclerMangerImpl swipeManager = mItemManger;

    private final Context context;
    private final LayoutInflater inflater;

    private Sex sex;
    private List<MasterEvent> events = Collections.emptyList();
    private EventActionListener listener;

    public EventAdapter(Context context, EventActionListener listener) {
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
        swipeManager.bindView(viewHolder.itemView, position);
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
        viewHolder.swipeLayout.addSwipeListener(new SwipeDeleteAction(listener, viewHolder.getEvent()));
        swipeManager.closeAllItems();
    }

    @Override
    public void move(EventViewHolder viewHolder) {
        viewHolder.swipeLayout.addSwipeListener(new SwipeMoveAction(listener, viewHolder.getEvent()));
        swipeManager.closeAllItems();
    }

    @Override
    public void edit(EventViewHolder viewHolder) {
        viewHolder.swipeLayout.addSwipeListener(new SwipeEditAction(listener, viewHolder.getEvent()));
        swipeManager.closeAllItems();
    }

    @Override
    public void done(EventViewHolder viewHolder) {
        viewHolder.swipeLayout.addSwipeListener(new SwipeDoneAction(listener, viewHolder.getEvent()));
        swipeManager.closeAllItems();
    }
}
