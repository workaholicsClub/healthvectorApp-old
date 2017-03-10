package ru.android.childdiary.presentation.main.calendar.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;

public class EventsAdapter extends RecyclerView.Adapter<EventViewHolder> {
    private final Context context;
    private final LayoutInflater inflater;
    private Sex sex;
    private List<MasterEvent> events;

    public EventsAdapter(Context context, @NonNull List<MasterEvent> events) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.events = Collections.unmodifiableList(events);
    }

    public void setSex(@Nullable Sex sex) {
        if (this.sex != sex) {
            this.sex = sex;
            notifyDataSetChanged();
        }
    }

    public void setEvents(@NonNull List<MasterEvent> events) {
        // TODO: здесь можно рассчитать разницу
        this.events = Collections.unmodifiableList(events);
        notifyDataSetChanged();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.event_item, parent, false);
        EventViewHolder viewHolder = new EventViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder viewHolder, int position) {
        viewHolder.bind(context, position, sex, events.get(position));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
