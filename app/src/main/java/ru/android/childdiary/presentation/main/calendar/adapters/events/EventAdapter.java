package ru.android.childdiary.presentation.main.calendar.adapters.events;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface;
import com.daimajia.swipe.util.Attributes;

import java.util.Collections;
import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> implements EventViewHolder.SwipeActionListener, SwipeItemMangerInterface {
    private final Context context;
    private final LayoutInflater inflater;
    private final SwipeManager swipeManager;
    private final EventActionListener actionListener;

    private Sex sex;
    private List<MasterEvent> events = Collections.emptyList();

    public EventAdapter(Context context, EventActionListener actionListener, FabController fabController) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.actionListener = actionListener;
        this.swipeManager = new SwipeManager(this, fabController);
        this.events = Collections.unmodifiableList(events);
    }

    public void setSex(@Nullable Sex sex) {
        if (this.sex != sex) {
            this.sex = sex;
            notifyDataSetChanged();
        }
    }

    public void setEvents(@NonNull List<MasterEvent> events) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new EventListDiff(this.events, events), false);
        diffResult.dispatchUpdatesTo(this);
        this.events = Collections.unmodifiableList(events);
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
        swipeManager.bindViewHolder(viewHolder, position);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public void delete(EventViewHolder viewHolder) {
        viewHolder.swipeLayout.addSwipeListener(new SwipeDeleteAction(actionListener, viewHolder.getEvent()));
        swipeManager.closeAllItems();
    }

    @Override
    public void move(EventViewHolder viewHolder) {
        viewHolder.swipeLayout.addSwipeListener(new SwipeMoveAction(actionListener, viewHolder.getEvent()));
        swipeManager.closeAllItems();
    }

    @Override
    public void edit(EventViewHolder viewHolder) {
        viewHolder.swipeLayout.addSwipeListener(new SwipeEditAction(actionListener, viewHolder.getEvent()));
        swipeManager.closeAllItems();
    }

    @Override
    public void done(EventViewHolder viewHolder) {
        viewHolder.swipeLayout.addSwipeListener(new SwipeDoneAction(actionListener, viewHolder.getEvent()));
        swipeManager.closeAllItems();
    }

    @Override
    public void openItem(int position) {
    }

    @Override
    public void closeItem(int position) {
    }

    @Override
    public void closeAllExcept(SwipeLayout layout) {
    }

    @Override
    public void closeAllItems() {
    }

    @Override
    public List<Integer> getOpenItems() {
        return null;
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return null;
    }

    @Override
    public void removeShownLayouts(SwipeLayout layout) {
    }

    @Override
    public boolean isOpen(int position) {
        return false;
    }

    @Override
    public Attributes.Mode getMode() {
        return null;
    }

    @Override
    public void setMode(Attributes.Mode mode) {
    }
}
