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
import android.view.KeyEvent;
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
import lombok.Getter;
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
import ru.android.childdiary.presentation.main.calendar.adapters.calendar.CalendarViewAdapter;
import ru.android.childdiary.presentation.main.calendar.adapters.events.EventActionListener;
import ru.android.childdiary.presentation.main.calendar.adapters.events.EventAdapter;
import ru.android.childdiary.presentation.main.calendar.adapters.events.FabController;
import ru.android.childdiary.presentation.main.widgets.FabToolbar;
import ru.android.childdiary.utils.DateUtils;
import ru.android.childdiary.utils.StringUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public abstract class CalendarFragment<Adapter extends CalendarViewAdapter> extends BaseMvpFragment<CalendarPresenter> implements CalendarView,
        AdapterView.OnItemClickListener, CalendarViewAdapter.OnSelectedDateChanged, EventActionListener, FabController {
    private static final int REQUEST_ADD_EVENT = 1;

    @InjectPresenter
    CalendarPresenter presenter;

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

    @BindView(R.id.fabToolbar)
    FabToolbar fabToolbar;

    private Adapter calendarAdapter;
    @Getter
    private EventAdapter eventAdapter;
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
        updateCalendarTitle();

        if (gridViewCalendar != null) {
            gridViewCalendar.setOnItemClickListener(this);
            gridViewCalendar.setAdapter(calendarAdapter);
            gridViewCalendar.setLayoutParams(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getGridViewHeight()));
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewEvents.setLayoutManager(layoutManager);
        eventAdapter = new EventAdapter(getContext(), this, this);
        recyclerViewEvents.setAdapter(eventAdapter);

        ViewCompat.setNestedScrollingEnabled(recyclerViewEvents, false);

        fabToolbar.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                boolean processed = fabToolbar.hideBar();
                if (processed) {
                    return true;
                }
            }
            return false;
        });
    }

    @LayoutRes
    protected abstract int getLayoutResourceId();

    protected abstract Adapter getCalendarViewAdapter();

    protected abstract int getGridViewHeight();

    protected abstract String getCalendarTitleText(Adapter adapter);

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
        textViewSelectedDate.setText(text);
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

    @OnClick(R.id.addDiaperEvent)
    void onAddDiaperEventClick() {
        presenter.addDiaperEvent();
    }

    @OnClick(R.id.addSleepEvent)
    void onAddSleepEventClick() {
        presenter.addSleepEvent();
    }

    @OnClick(R.id.addFeedEvent)
    void onAddFeedEventClick() {
        presenter.addFeedEvent();
    }

    @OnClick(R.id.addPumpEvent)
    void onAddPumpEventClick() {
        presenter.addPumpEventClick();
    }

    @OnClick(R.id.addOtherEvent)
    void onAddOtherEventClick() {
        presenter.addOtherEventClick();
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
        fabToolbar.setColor(ThemeUtils.getColorAccent(getContext(), sex));
        eventAdapter.getSwipeManager().setFabController(child == Child.NULL ? null : this);
        if (child == Child.NULL) {
            hideFabBar();
        } else {
            eventAdapter.getSwipeManager().update();
        }
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
    public void navigateToDiaperEventAdd() {
        Intent intent = DiaperEventDetailActivity.getIntent(getContext(), null);
        startActivityForResult(intent, REQUEST_ADD_EVENT);
    }

    @Override
    public void navigateToFeedEventAdd() {
        Intent intent = FeedEventDetailActivity.getIntent(getContext(), null);
        startActivityForResult(intent, REQUEST_ADD_EVENT);
    }

    @Override
    public void navigateToOtherEventAdd() {
        Intent intent = OtherEventDetailActivity.getIntent(getContext(), null);
        startActivityForResult(intent, REQUEST_ADD_EVENT);
    }

    @Override
    public void navigateToPumpEventAdd() {
        Intent intent = PumpEventDetailActivity.getIntent(getContext(), null);
        startActivityForResult(intent, REQUEST_ADD_EVENT);
    }

    @Override
    public void navigateToSleepEventAdd() {
        Intent intent = SleepEventDetailActivity.getIntent(getContext(), null);
        startActivityForResult(intent, REQUEST_ADD_EVENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_EVENT) {
            fabToolbar.hideBarWithoutAnimation();
        }
    }

    @Override
    public void navigateToDiaperEventEdit(@NonNull MasterEvent event) {
        Intent intent = DiaperEventDetailActivity.getIntent(getContext(), event);
        startActivity(intent);
    }

    @Override
    public void navigateToFeedEventEdit(@NonNull MasterEvent event) {
        Intent intent = FeedEventDetailActivity.getIntent(getContext(), event);
        startActivity(intent);
    }

    @Override
    public void navigateToOtherEventEdit(@NonNull MasterEvent event) {
        Intent intent = OtherEventDetailActivity.getIntent(getContext(), event);
        startActivity(intent);
    }

    @Override
    public void navigateToPumpEventEdit(@NonNull MasterEvent event) {
        Intent intent = PumpEventDetailActivity.getIntent(getContext(), event);
        startActivity(intent);
    }

    @Override
    public void navigateToSleepEventEdit(@NonNull MasterEvent event) {
        Intent intent = SleepEventDetailActivity.getIntent(getContext(), event);
        startActivity(intent);
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

    @Override
    public void showFab() {
        fabToolbar.showFab();
    }

    @Override
    public void hideFabBar() {
        fabToolbar.hideFabBar();
    }
}
