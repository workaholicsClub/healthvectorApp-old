package ru.android.childdiary.presentation.calendar.adapters.events;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.presentation.core.swipe.FabController;
import ru.android.childdiary.presentation.core.swipe.SwipeManager;
import ru.android.childdiary.utils.EventHelper;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> implements EventViewHolder.SwipeActionListener {
    private final Context context;
    private final LayoutInflater inflater;
    private final EventActionListener eventActionListener;
    @Getter
    private final SwipeManager swipeManager;

    private Sex sex;
    private List<MasterEvent> events = Collections.emptyList();

    public EventAdapter(Context context, @NonNull EventActionListener eventActionListener, @Nullable FabController fabController) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.eventActionListener = eventActionListener;
        this.swipeManager = new SwipeManager(fabController);
        this.events = new ArrayList<>(events);
    }

    public void setSex(@Nullable Sex sex) {
        if (this.sex != sex) {
            this.sex = sex;
            notifyDataSetChanged();
        }
    }

    public void setEvents(@NonNull List<MasterEvent> events) {
        swipeManager.closeAllItems();
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new EventListDiff(this.events, events), false);
        diffResult.dispatchUpdatesTo(this);
        this.events = new ArrayList<>(events);
    }

    public void updateEvent(@NonNull MasterEvent event) {
        for (int i = 0; i < events.size(); ++i) {
            if (EventHelper.sameEvent(events.get(i), event)) {
                events.set(i, event);
                notifyItemChanged(i, new Object());
            }
        }
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.event_item, parent, false);
        EventViewHolder viewHolder = new EventViewHolder(v, eventActionListener, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder viewHolder, int position) {
        viewHolder.bind(context, sex, events.get(position));
        swipeManager.bindViewHolder(viewHolder, position);
    }

    @Override
    public void onBindViewHolder(EventViewHolder viewHolder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(viewHolder, position);
        } else {
            viewHolder.updateDescription(context, events.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public void done(EventViewHolder viewHolder) {
        viewHolder.swipeLayout.addSwipeListener(new SwipeDoneAction(eventActionListener, viewHolder.getEvent()));
        swipeManager.closeAllItems();
    }

    @Override
    public void move(EventViewHolder viewHolder) {
        viewHolder.swipeLayout.addSwipeListener(new SwipeMoveAction(eventActionListener, viewHolder.getEvent()));
        swipeManager.closeAllItems();
    }

    @Override
    public void delete(EventViewHolder viewHolder) {
        viewHolder.swipeLayout.addSwipeListener(new SwipeDeleteAction(eventActionListener, viewHolder.getEvent()));
        swipeManager.closeAllItems();
    }
}
