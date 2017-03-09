package ru.android.childdiary.presentation.main.calendar.adapters;

import android.content.Context;
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
    private final Sex sex;
    private final List<MasterEvent> events;

    public EventsAdapter(Context context, Sex sex, List<MasterEvent> events) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.sex = sex;
        this.events = Collections.unmodifiableList(events);
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
