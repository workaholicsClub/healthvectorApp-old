package ru.android.childdiary.presentation.calendar.partitions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.joda.time.LocalDate;

import java.util.Set;

import butterknife.BindView;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.calendar.data.DoctorVisitEvent;
import ru.android.childdiary.domain.calendar.data.ExerciseEvent;
import ru.android.childdiary.domain.calendar.data.MedicineTakingEvent;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.calendar.data.standard.DiaperEvent;
import ru.android.childdiary.domain.calendar.data.standard.FeedEvent;
import ru.android.childdiary.domain.calendar.data.standard.OtherEvent;
import ru.android.childdiary.domain.calendar.data.standard.PumpEvent;
import ru.android.childdiary.domain.calendar.data.standard.SleepEvent;
import ru.android.childdiary.presentation.calendar.adapters.calendar.CalendarViewAdapter;
import ru.android.childdiary.presentation.calendar.adapters.events.EventActionListener;
import ru.android.childdiary.presentation.calendar.adapters.events.EventAdapter;
import ru.android.childdiary.presentation.calendar.dialogs.EventFilterDialogArguments;
import ru.android.childdiary.presentation.calendar.dialogs.EventFilterDialogFragment;
import ru.android.childdiary.presentation.calendar.dialogs.MoveEventDialogArguments;
import ru.android.childdiary.presentation.calendar.dialogs.MoveEventDialogFragment;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.events.DiaperEventDetailActivity;
import ru.android.childdiary.presentation.events.DoctorVisitEventDetailActivity;
import ru.android.childdiary.presentation.events.ExerciseEventDetailActivity;
import ru.android.childdiary.presentation.events.FeedEventDetailActivity;
import ru.android.childdiary.presentation.events.MedicineTakingEventDetailActivity;
import ru.android.childdiary.presentation.events.OtherEventDetailActivity;
import ru.android.childdiary.presentation.events.PumpEventDetailActivity;
import ru.android.childdiary.presentation.events.SleepEventDetailActivity;
import ru.android.childdiary.services.TimerServiceConnection;
import ru.android.childdiary.services.TimerServiceListener;
import ru.android.childdiary.utils.strings.DateUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public abstract class BaseCalendarFragment<Adapter extends CalendarViewAdapter>
        extends AppPartitionFragment
        implements BaseCalendarView, AdapterView.OnItemClickListener,
        CalendarViewAdapter.OnSelectedDateChanged, EventActionListener, TimerServiceListener,
        MoveEventDialogFragment.Listener, EventFilterDialogFragment.Listener {
    private static final String TAG_PROGRESS_DIALOG_DELETING_EVENTS = "TAG_PROGRESS_DIALOG_DELETING_EVENTS";
    private static final String TAG_PROGRESS_DIALOG_UPDATING_EVENTS = "TAG_PROGRESS_DIALOG_UPDATING_EVENTS";
    private static final String TAG_MOVE_EVENT_DIALOG = "TAG_MOVE_EVENT_DIALOG";
    private static final String TAG_EVENT_FILTER_DIALOG = "TAG_EVENT_FILTER_DIALOG";

    private static final int REQUEST_UPDATE_EVENT = 1;

    @InjectPresenter
    BaseCalendarPresenter presenter;

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
    @Nullable
    private FabController fabController;

    private TimerServiceConnection timerServiceConnection = new TimerServiceConnection(getContext(), this);

    @Override
    protected void setupUi() {
        if (calendarHeader != null) {
            calendarTitle = calendarHeader.findViewById(R.id.title);
            calendarHeader.findViewById(R.id.left).setOnClickListener(v -> moveLeft());
            calendarHeader.findViewById(R.id.right).setOnClickListener(v -> moveRight());
            calendarHeader.findViewById(R.id.today).setOnClickListener(v -> moveToday());
        }

        eventsTitle = eventsHeader.findViewById(R.id.title);
        if (showEventsHeaderNavigationButtons()) {
            eventsHeader.findViewById(R.id.left).setOnClickListener(v -> moveLeft());
            eventsHeader.findViewById(R.id.right).setOnClickListener(v -> moveRight());
            eventsHeader.findViewById(R.id.today).setOnClickListener(v -> moveToday());
        } else {
            eventsHeader.findViewById(R.id.left).setVisibility(View.GONE);
            eventsHeader.findViewById(R.id.right).setVisibility(View.GONE);
            eventsHeader.findViewById(R.id.today).setVisibility(View.GONE);
        }

        calendarAdapter = createCalendarViewAdapter();
        calendarAdapter.setSelectedDate(getSelectedDate(), false);
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
        recyclerViewEvents.setVisibility(View.GONE);

        ViewCompat.setNestedScrollingEnabled(recyclerViewEvents, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_UPDATE_EVENT && resultCode == Activity.RESULT_OK) {
            if (fabController != null) {
                fabController.hideBarWithoutAnimation();
            }
        }
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        calendarAdapter.setSex(getSex());
        eventAdapter.setSex(getSex());
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
        eventAdapter.updatePartially(event);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fabController = null;
    }

    protected abstract Adapter createCalendarViewAdapter();

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
    public void showFilter() {
        presenter.requestFilterDialog();
    }

    @Override
    public void showCalendarState(CalendarState calendarState) {
        logger.debug("showCalendarState: " + calendarState);

        showChild(calendarState.getChild());

        calendarAdapter.setSelectedDate(calendarState.getDate(), false);
        updateCalendarTitle();
        updateEventsTitle(calendarState.getDate());

        eventAdapter.setItems(calendarState.getEvents());
        eventAdapter.setFabController(calendarState.getChild().getId() == null ? null : fabController, true);
        recyclerViewEvents.setVisibility(calendarState.getEvents().isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void navigateToDiaperEvent(@NonNull MasterEvent event, @NonNull DiaperEvent defaultEvent) {
        Intent intent = DiaperEventDetailActivity.getIntent(getContext(), event, defaultEvent);
        startActivityForResult(intent, REQUEST_UPDATE_EVENT);
    }

    @Override
    public void navigateToFeedEvent(@NonNull MasterEvent event, @NonNull FeedEvent defaultEvent) {
        Intent intent = FeedEventDetailActivity.getIntent(getContext(), event, defaultEvent);
        startActivityForResult(intent, REQUEST_UPDATE_EVENT);
    }

    @Override
    public void navigateToOtherEvent(@NonNull MasterEvent event, @NonNull OtherEvent defaultEvent) {
        Intent intent = OtherEventDetailActivity.getIntent(getContext(), event, defaultEvent);
        startActivityForResult(intent, REQUEST_UPDATE_EVENT);
    }

    @Override
    public void navigateToPumpEvent(@NonNull MasterEvent event, @NonNull PumpEvent defaultEvent) {
        Intent intent = PumpEventDetailActivity.getIntent(getContext(), event, defaultEvent);
        startActivityForResult(intent, REQUEST_UPDATE_EVENT);
    }

    @Override
    public void navigateToSleepEvent(@NonNull MasterEvent event, @NonNull SleepEvent defaultEvent) {
        Intent intent = SleepEventDetailActivity.getIntent(getContext(), event, defaultEvent);
        startActivityForResult(intent, REQUEST_UPDATE_EVENT);
    }

    @Override
    public void navigateToDoctorVisitEvent(@NonNull MasterEvent event, @NonNull DoctorVisitEvent defaultEvent) {
        Intent intent = DoctorVisitEventDetailActivity.getIntent(getContext(), event, defaultEvent, false);
        startActivityForResult(intent, REQUEST_UPDATE_EVENT);
    }

    @Override
    public void navigateToMedicineTakingEvent(@NonNull MasterEvent event, @NonNull MedicineTakingEvent defaultEvent) {
        Intent intent = MedicineTakingEventDetailActivity.getIntent(getContext(), event, defaultEvent, false);
        startActivityForResult(intent, REQUEST_UPDATE_EVENT);
    }

    @Override
    public void navigateToExerciseEvent(@NonNull MasterEvent event, @NonNull ExerciseEvent defaultEvent) {
        Intent intent = ExerciseEventDetailActivity.getIntent(getContext(), event, defaultEvent, false);
        startActivityForResult(intent, REQUEST_UPDATE_EVENT);
    }

    @Override
    public void confirmDeleteOneEvent(@NonNull MasterEvent event) {
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.delete_event_confirmation_dialog_title)
                .setPositiveButton(R.string.delete,
                        (dialog, which) -> presenter.deleteOneEvent(event))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void askDeleteOneEventOrLinerGroup(@NonNull MasterEvent event) {
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.ask_delete_one_event_or_linear_group)
                .setPositiveButton(R.string.delete_one_event,
                        (dialog, which) -> presenter.deleteOneEvent(event))
                .setNegativeButton(R.string.delete_linear_group,
                        (dialog, which) -> presenter.deleteLinearGroup(event))
                .show();
    }

    @Override
    public void showNeedToFillNoteOrPhoto(@NonNull MasterEvent event) {
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.need_to_fill_not_or_photo)
                .setPositiveButton(R.string.add, (dialog, which) -> presenter.requestEventDetail(event))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void showDeletingEvents(boolean loading) {
        if (loading) {
            showProgress(TAG_PROGRESS_DIALOG_DELETING_EVENTS,
                    getString(R.string.please_wait),
                    getString(R.string.events_deleting));
        } else {
            hideProgress(TAG_PROGRESS_DIALOG_DELETING_EVENTS);
        }
    }

    @Override
    public void showUpdatingEvents(boolean loading) {
        if (loading) {
            showProgress(TAG_PROGRESS_DIALOG_UPDATING_EVENTS,
                    getString(R.string.please_wait),
                    getString(R.string.events_updating));
        } else {
            hideProgress(TAG_PROGRESS_DIALOG_UPDATING_EVENTS);
        }
    }

    @Override
    public void showFilterDialog(@NonNull Set<EventType> eventTypes) {
        EventFilterDialogFragment dialogFragment = new EventFilterDialogFragment();
        dialogFragment.showAllowingStateLoss(getChildFragmentManager(), TAG_EVENT_FILTER_DIALOG, EventFilterDialogArguments.builder()
                .sex(getSex())
                .selectedItems(eventTypes)
                .build());
    }

    @Override
    public void noChildSpecified() {
        showToast(getString(R.string.intention_add_child_profile));
    }

    @Override
    public void delete(MasterEvent event) {
        presenter.delete(event);
    }

    @Override
    public void edit(MasterEvent event) {
        presenter.requestEventDetail(event);
    }

    @Override
    public void move(MasterEvent event) {
        MoveEventDialogFragment dialogFragment = new MoveEventDialogFragment();
        dialogFragment.showAllowingStateLoss(getChildFragmentManager(), TAG_MOVE_EVENT_DIALOG,
                MoveEventDialogArguments.builder()
                        .sex(getSex())
                        .event(event)
                        .build());
    }

    @Override
    public void onMoveEventClick(String tag, @NonNull MasterEvent event, int minutes) {
        presenter.moveOneEvent(event, minutes);
    }

    @Override
    public void onMoveLinearGroupClick(String tag, @NonNull MasterEvent event, int minutes) {
        presenter.moveLinearGroup(event, minutes);
    }

    @Override
    public void onSetFilter(String tag, @NonNull Set<EventType> selectedItems) {
        presenter.setFilter(selectedItems);
    }

    @Override
    public void done(MasterEvent event) {
        presenter.done(event);
    }
}
