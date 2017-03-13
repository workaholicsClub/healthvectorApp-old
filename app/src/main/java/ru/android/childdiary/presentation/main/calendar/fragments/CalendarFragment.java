package ru.android.childdiary.presentation.main.calendar.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.joda.time.LocalDate;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseMvpFragment;
import ru.android.childdiary.presentation.events.DiaperEventDetailActivity;
import ru.android.childdiary.presentation.events.FeedEventDetailActivity;
import ru.android.childdiary.presentation.events.OtherEventDetailActivity;
import ru.android.childdiary.presentation.events.PumpEventDetailActivity;
import ru.android.childdiary.presentation.events.SleepEventDetailActivity;
import ru.android.childdiary.presentation.main.calendar.CalendarPresenter;
import ru.android.childdiary.presentation.main.calendar.CalendarView;
import ru.android.childdiary.presentation.main.calendar.adapters.CalendarViewAdapter;
import ru.android.childdiary.presentation.main.calendar.adapters.EventActionListener;
import ru.android.childdiary.presentation.main.calendar.adapters.EventsAdapter;
import ru.android.childdiary.utils.DateUtils;
import ru.android.childdiary.utils.StringUtils;

public abstract class CalendarFragment<Adapter extends CalendarViewAdapter> extends BaseMvpFragment<CalendarPresenter> implements CalendarView,
        AdapterView.OnItemClickListener, CalendarViewAdapter.OnSelectedDateChanged, EventActionListener {
    @InjectPresenter
    CalendarPresenter presenter;

    @BindView(R.id.textView)
    TextView textView;

    @Nullable
    @BindView(R.id.calendarTitle)
    TextView calendarTitle;

    @Nullable
    @BindView(R.id.gridViewCalendar)
    GridView gridViewCalendar;

    @BindView(R.id.textViewSelectedDate)
    TextView textViewSelectedDate;

    @BindView(R.id.recyclerViewEvents)
    RecyclerView recyclerViewEvents;

    private Adapter calendarAdapter;
    private EventsAdapter eventsAdapter;
    private Sex sex;

    @Override
    @Nullable
    protected Sex getSex() {
        return sex;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResourceId(), container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        calendarAdapter = getCalendarViewAdapter();
        updateTitle(calendarAdapter);

        if (gridViewCalendar != null) {
            gridViewCalendar.setOnItemClickListener(this);
            gridViewCalendar.setAdapter(calendarAdapter);
            gridViewCalendar.setLayoutParams(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getGridViewHeight()));
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewEvents.setLayoutManager(layoutManager);
        eventsAdapter = new EventsAdapter(getContext(), this);
        recyclerViewEvents.setAdapter(eventsAdapter);

        ViewCompat.setNestedScrollingEnabled(recyclerViewEvents, false);
    }

    @LayoutRes
    protected abstract int getLayoutResourceId();

    protected abstract Adapter getCalendarViewAdapter();

    protected abstract int getGridViewHeight();

    protected abstract String getCalendarTitleText(Adapter adapter);

    private void updateTitle(Adapter adapter) {
        Context context = getContext();
        LocalDate selectedDate = adapter.getSelectedDate();
        int day = selectedDate.getDayOfMonth();
        String monthName = DateUtils.monthGenitiveName(context, selectedDate.getMonthOfYear());
        String text = context.getString(R.string.calendar_selected_date_format, day, monthName);
        textViewSelectedDate.setText(text);
        if (calendarTitle != null) {
            calendarTitle.setText(getCalendarTitleText(calendarAdapter));
        }
    }

    @Optional
    @OnClick(R.id.left)
    void moveLeft() {
        calendarAdapter.moveLeft();
    }

    @Optional
    @OnClick(R.id.right)
    void moveRight() {
        calendarAdapter.moveRight();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (calendarAdapter.getItem(position) instanceof LocalDate) {
            LocalDate date = (LocalDate) calendarAdapter.getItem(position);
            calendarAdapter.setSelectedDate(date);
        }
    }

    @Override
    public final void onSelectedDateChanged() {
        presenter.switchDate(calendarAdapter.getSelectedDate());
    }

    @Override
    public void setActive(@NonNull Child child) {
        logger.debug("setActive: " + child);
        textView.setText(child == Child.NULL ? "no active child" : child.getName());
        sex = child.getSex();
        calendarAdapter.setSex(sex);
        eventsAdapter.setSex(sex);
    }

    @Override
    public void setSelected(@NonNull LocalDate date) {
        logger.debug("setSelected: " + date);
        calendarAdapter.setSelectedDate(date, false);
        updateTitle(calendarAdapter);
    }

    @Override
    public void showEvents(@NonNull List<MasterEvent> events) {
        logger.debug("showEvents: " + StringUtils.eventsList(events));
        eventsAdapter.setEvents(events);
    }

    @Override
    public void navigateToEventEdit(@NonNull MasterEvent event) {
        switch (event.getEventType()) {
            case SLEEP: {
                Intent intent = SleepEventDetailActivity.getIntent(getContext(), event.getMasterEventId());
                startActivity(intent);
                break;
            }
            case FEED: {
                Intent intent = FeedEventDetailActivity.getIntent(getContext(), event.getMasterEventId());
                startActivity(intent);
                break;
            }
            case PUMP: {
                Intent intent = PumpEventDetailActivity.getIntent(getContext(), event.getMasterEventId());
                startActivity(intent);
                break;
            }
            case DIAPER: {
                Intent intent = DiaperEventDetailActivity.getIntent(getContext(), event.getMasterEventId());
                startActivity(intent);
                break;
            }
            case OTHER: {
                Intent intent = OtherEventDetailActivity.getIntent(getContext(), event.getMasterEventId());
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void delete(MasterEvent event) {
        presenter.delete(event);
    }

    @Override
    public void move(MasterEvent event) {
    }

    @Override
    public void edit(MasterEvent event) {
        presenter.edit(event);
    }

    @Override
    public void done(MasterEvent event) {
        presenter.done(event);
    }
}
