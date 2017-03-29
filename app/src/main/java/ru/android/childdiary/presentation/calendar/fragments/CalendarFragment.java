package ru.android.childdiary.presentation.calendar.fragments;

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
import butterknife.ButterKnife;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.calendar.CalendarPresenter;
import ru.android.childdiary.presentation.calendar.CalendarView;
import ru.android.childdiary.presentation.calendar.adapters.calendar.CalendarViewAdapter;
import ru.android.childdiary.presentation.calendar.adapters.events.EventActionListener;
import ru.android.childdiary.presentation.calendar.adapters.events.EventAdapter;
import ru.android.childdiary.presentation.calendar.adapters.events.FabController;
import ru.android.childdiary.presentation.core.BaseMvpFragment;
import ru.android.childdiary.presentation.events.DiaperEventDetailActivity;
import ru.android.childdiary.presentation.events.FeedEventDetailActivity;
import ru.android.childdiary.presentation.events.OtherEventDetailActivity;
import ru.android.childdiary.presentation.events.PumpEventDetailActivity;
import ru.android.childdiary.presentation.events.SleepEventDetailActivity;
import ru.android.childdiary.services.TimerServiceConnection;
import ru.android.childdiary.services.TimerServiceListener;
import ru.android.childdiary.utils.DateUtils;
import ru.android.childdiary.utils.StringUtils;

public abstract class CalendarFragment<Adapter extends CalendarViewAdapter> extends BaseMvpFragment<CalendarPresenter> implements CalendarView,
        AdapterView.OnItemClickListener, CalendarViewAdapter.OnSelectedDateChanged, EventActionListener, TimerServiceListener {
    @InjectPresenter
    CalendarPresenter presenter;

    @Nullable
    @BindView(R.id.calendarHeader)
    View calendarHeader;
    TextView calendarTitle;

    @Nullable
    @BindView(R.id.gridViewCalendar)
    GridView gridViewCalendar;

    @BindView(R.id.eventsHeader)
    View eventsHeader;
    TextView eventsTitle;

    @BindView(R.id.recyclerViewEvents)
    RecyclerView recyclerViewEvents;

    private Adapter calendarAdapter;
    @Getter
    private EventAdapter eventAdapter;
    private FabController fabController;
    private Sex sex;
    private TimerServiceConnection timerServiceConnection = new TimerServiceConnection(getContext(), this);

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

        if (calendarHeader != null) {
            calendarTitle = ButterKnife.findById(calendarHeader, R.id.title);
            calendarHeader.findViewById(R.id.left).setOnClickListener(v -> moveLeft());
            calendarHeader.findViewById(R.id.right).setOnClickListener(v -> moveRight());
            calendarHeader.findViewById(R.id.today).setOnClickListener(v -> moveToday());
        }

        eventsTitle = ButterKnife.findById(eventsHeader, R.id.title);
        if (showEventsHeaderNavigationButtons()) {
            eventsHeader.findViewById(R.id.left).setOnClickListener(v -> moveLeft());
            eventsHeader.findViewById(R.id.right).setOnClickListener(v -> moveRight());
            eventsHeader.findViewById(R.id.today).setOnClickListener(v -> moveToday());
        } else {
            eventsHeader.findViewById(R.id.left).setVisibility(View.GONE);
            eventsHeader.findViewById(R.id.right).setVisibility(View.GONE);
            eventsHeader.findViewById(R.id.today).setVisibility(View.GONE);
        }

        calendarAdapter = getCalendarViewAdapter();
        updateCalendarTitle();

        if (gridViewCalendar != null) {
            gridViewCalendar.setOnItemClickListener(this);
            gridViewCalendar.setAdapter(calendarAdapter);
            gridViewCalendar.setLayoutParams(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getGridViewHeight()));
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewEvents.setLayoutManager(layoutManager);
        eventAdapter = new EventAdapter(getContext(), this, fabController);
        recyclerViewEvents.setAdapter(eventAdapter);

        ViewCompat.setNestedScrollingEnabled(recyclerViewEvents, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FabController) {
            fabController = (FabController) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        timerServiceConnection = new TimerServiceConnection(getContext(), this);
        timerServiceConnection.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        timerServiceConnection.close();
    }

    @Override
    public void onTimerTick(@NonNull SleepEvent event) {
        eventAdapter.updateEvent(event);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fabController = null;
    }

    @LayoutRes
    protected abstract int getLayoutResourceId();

    protected abstract Adapter getCalendarViewAdapter();

    protected abstract int getGridViewHeight();

    protected abstract String getCalendarTitleText(Adapter adapter);

    protected abstract boolean showEventsHeaderNavigationButtons();

    private void updateCalendarTitle() {
        if (calendarTitle != null) {
            calendarTitle.setText(getCalendarTitleText(calendarAdapter));
        }
    }

    private void updateEventsTitle(LocalDate selectedDate) {
        Context context = getContext();
        int day = selectedDate.getDayOfMonth();
        String monthName = DateUtils.monthGenitiveName(context, selectedDate.getMonthOfYear());
        String text = context.getString(R.string.calendar_selected_date_format, day, monthName);
        eventsTitle.setText(text);
    }

    void moveLeft() {
        calendarAdapter.moveLeft();
    }

    void moveToday() {
        calendarAdapter.moveToday();
    }

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
        sex = child.getSex();
        calendarAdapter.setSex(sex);
        eventAdapter.setSex(sex);
        eventAdapter.getSwipeManager().setFabController(child.getId() == null ? null : fabController);
    }

    @Override
    public void setSelected(@NonNull LocalDate date) {
        logger.debug("setSelected: " + date);
        calendarAdapter.setSelectedDate(date, false);
        updateCalendarTitle();
    }

    @Override
    public void showEvents(@NonNull LocalDate date, @NonNull List<MasterEvent> events) {
        logger.debug("showEvents: " + StringUtils.eventsList(events));
        updateEventsTitle(date);
        eventAdapter.setEvents(events);
    }

    @Override
    public void navigateToDiaperEvent(@NonNull MasterEvent event, boolean readOnly) {
        Intent intent = DiaperEventDetailActivity.getIntent(getContext(), event, readOnly);
        startActivity(intent);
    }

    @Override
    public void navigateToFeedEvent(@NonNull MasterEvent event, boolean readOnly) {
        Intent intent = FeedEventDetailActivity.getIntent(getContext(), event, readOnly);
        startActivity(intent);
    }

    @Override
    public void navigateToOtherEvent(@NonNull MasterEvent event, boolean readOnly) {
        Intent intent = OtherEventDetailActivity.getIntent(getContext(), event, readOnly);
        startActivity(intent);
    }

    @Override
    public void navigateToPumpEvent(@NonNull MasterEvent event, boolean readOnly) {
        Intent intent = PumpEventDetailActivity.getIntent(getContext(), event, readOnly);
        startActivity(intent);
    }

    @Override
    public void navigateToSleepEvent(@NonNull MasterEvent event, boolean readOnly) {
        Intent intent = SleepEventDetailActivity.getIntent(getContext(), event, readOnly);
        startActivity(intent);
    }

    @Override
    public void done(MasterEvent event) {
        presenter.done(event);
    }

    @Override
    public void move(MasterEvent event) {
    }

    @Override
    public void edit(MasterEvent event) {
        presenter.requestEventDetail(event, false);
    }

    @Override
    public void review(MasterEvent event) {
        presenter.requestEventDetail(event, true);
    }

    @Override
    public void delete(MasterEvent event) {
        presenter.delete(event);
    }
}
