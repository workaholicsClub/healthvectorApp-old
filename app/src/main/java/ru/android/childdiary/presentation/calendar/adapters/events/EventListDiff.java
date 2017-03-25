package ru.android.childdiary.presentation.calendar.adapters.events;

import android.support.v7.util.DiffUtil;

import java.util.List;

import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.utils.EventHelper;

public class EventListDiff extends DiffUtil.Callback {
    private final List<MasterEvent> oldEvents;
    private final List<MasterEvent> newEvents;

    public EventListDiff(List<MasterEvent> oldEvents, List<MasterEvent> newEvents) {
        this.oldEvents = oldEvents;
        this.newEvents = newEvents;
    }

    @Override
    public int getOldListSize() {
        return oldEvents.size();
    }

    @Override
    public int getNewListSize() {
        return newEvents.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        MasterEvent oldEvent = oldEvents.get(oldItemPosition);
        MasterEvent newEvent = newEvents.get(newItemPosition);
        return EventHelper.sameEvent(oldEvent, newEvent);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        MasterEvent oldEvent = oldEvents.get(oldItemPosition);
        MasterEvent newEvent = newEvents.get(newItemPosition);
        return oldEvent.equals(newEvent);
    }
}
