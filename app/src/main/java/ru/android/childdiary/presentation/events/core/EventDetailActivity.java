package ru.android.childdiary.presentation.events.core;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.widgets.CustomDatePickerDialog;
import ru.android.childdiary.presentation.core.widgets.CustomTimePickerDialog;
import ru.android.childdiary.presentation.events.dialogs.TimeDialog;
import ru.android.childdiary.presentation.events.widgets.EventDetailDateView;
import ru.android.childdiary.presentation.events.widgets.EventDetailEditTextView;
import ru.android.childdiary.presentation.events.widgets.EventDetailNoteView;
import ru.android.childdiary.presentation.events.widgets.EventDetailTimeView;
import ru.android.childdiary.utils.EventHelper;
import ru.android.childdiary.utils.KeyboardUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public abstract class EventDetailActivity<V extends EventDetailView<T>, T extends MasterEvent> extends BaseMvpActivity implements
        EventDetailView<T>, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, TimeDialog.Listener {
    protected T event;
    protected T defaultEvent;

    @BindView(R.id.noteView)
    protected EventDetailNoteView noteView;

    @BindView(R.id.buttonAdd)
    protected Button buttonAdd;

    @BindView(R.id.rootView)
    View rootView;

    @BindView(R.id.dummy)
    View dummy;

    private ViewGroup eventDetailsView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        setupEditTextView(noteView);
        buttonAdd.setVisibility(GONE);
        MasterEvent masterEvent = (MasterEvent) getIntent().getSerializableExtra(ExtraConstants.EXTRA_MASTER_EVENT);
        if (savedInstanceState == null) {
            if (masterEvent == null) {
                getPresenter().requestDefaultEventDetail(getEventType());
            } else {
                getPresenter().requestEventDetails(masterEvent);
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //noinspection unchecked
        T event = (T) savedInstanceState.getSerializable(ExtraConstants.EXTRA_EVENT);
        showEventDetail(event);
        dummy.requestFocus();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ExtraConstants.EXTRA_EVENT, buildEvent());
    }

    @Override
    protected void setContentViewBeforeBind() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View contentView = inflater.inflate(getContentLayoutResourceId(), null);
        eventDetailsView = ButterKnife.findById(this, R.id.eventDetailsView);
        eventDetailsView.addView(contentView);
    }

    public void hideKeyboardAndClearFocus(View view) {
        KeyboardUtils.hideKeyboard(this, view);
        view.clearFocus();
        dummy.requestFocus();
    }

    protected void setupEditTextView(EventDetailEditTextView view) {
        List<Disposable> disposables = view.createSubscriptions(this::hideKeyboardAndClearFocus);
        for (Disposable disposable : disposables) {
            unsubscribeOnDestroy(disposable);
        }
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), true));
    }

    protected final T buildEvent() {
        return buildEvent(event == null ? defaultEvent : event);
    }

    @Override
    public final void showDefaultEventDetail(@NonNull T event) {
        this.event = null;
        defaultEvent = event;
        changeThemeIfNeeded(event.getChild());
        setupEventDetail(event);
        invalidateOptionsMenu();
        buttonAdd.setVisibility(VISIBLE);
        buttonAdd.setOnClickListener(v -> getPresenter().addEvent(buildEvent(), true));
    }

    @Override
    public final void showEventDetail(@NonNull T event) {
        this.event = event;
        defaultEvent = null;
        changeThemeIfNeeded(event.getChild());
        setupEventDetail(event);
        invalidateOptionsMenu();
        getToolbar().setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.toolbar_action_overflow));
        buttonAdd.setVisibility(GONE);
        buttonAdd.setOnClickListener(null);
    }

    protected abstract EventDetailPresenter<V, T> getPresenter();

    protected abstract EventType getEventType();

    @LayoutRes
    protected abstract int getContentLayoutResourceId();

    protected abstract void setupEventDetail(@NonNull T event);

    protected abstract T buildEvent(@Nullable T event);

    @Override
    public void eventAdded(@NonNull T event, boolean afterButtonPressed) {
        if (afterButtonPressed) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void eventUpdated(@NonNull T event, boolean afterButtonPressed) {
        if (afterButtonPressed) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void eventDeleted(@NonNull MasterEvent event) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void eventDone(@NonNull MasterEvent event) {
        showToast(getString(EventHelper.isDone(event) ? R.string.event_is_done : R.string.event_is_not_done));
    }

    @Override
    public void showTimeDialog(String tag, @NonNull Child child, TimeDialog.Parameters parameters) {
        TimeDialog dialog = new TimeDialog();
        dialog.showAllowingStateLoss(getSupportFragmentManager(), tag, child, parameters);
    }

    @Override
    public void validationFailed() {
    }

    @Override
    public void showValidationErrorMessage(String msg) {
        showToast(msg);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof TimePickerDialog) {
            ((TimePickerDialog) fragment).setOnTimeSetListener(this);
        } else if (fragment instanceof DatePickerDialog) {
            ((DatePickerDialog) fragment).setOnDateSetListener(this);
        }
    }

    protected void showDatePicker(String tag, @Nullable LocalDate date,
                                  @Nullable LocalDate minDate, @Nullable LocalDate maxDate) {
        DatePickerDialog dpd = CustomDatePickerDialog.create(this, this, date, getSex(),
                minDate, maxDate);
        dpd.show(getFragmentManager(), tag);
        hideKeyboardAndClearFocus(rootView.findFocus());
    }

    @Override
    public final void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        LocalDate date = LocalDate.fromCalendarFields(calendar);
        setDate(view.getTag(), date);
    }

    protected void setDate(String tag, LocalDate date) {
    }

    protected void showTimePicker(String tag, @Nullable LocalTime time) {
        TimePickerDialog tpd = CustomTimePickerDialog.create(this, this, time, getSex());
        tpd.show(getFragmentManager(), tag);
        hideKeyboardAndClearFocus(rootView.findFocus());
    }

    @Override
    public final void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        LocalTime time = new LocalTime(hourOfDay, minute);
        setTime(view.getTag(), time);
    }

    protected void setTime(String tag, LocalTime time) {
    }

    @Nullable
    protected DateTime getDateTime(EventDetailDateView dateView, EventDetailTimeView timeView) {
        LocalDate date = dateView.getValue();
        LocalTime time = timeView.getValue();
        return date == null || time == null
                ? null
                : new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(),
                time.getHourOfDay(), time.getMinuteOfHour());
    }

    protected void setDateTime(@Nullable DateTime dateTime, EventDetailDateView dateView, EventDetailTimeView timeView) {
        dateView.setValue(dateTime == null ? null : dateTime.toLocalDate());
        timeView.setValue(dateTime == null ? null : dateTime.toLocalTime());
    }

    @Override
    public void onSetTime(String tag, int minutes) {
    }

    @Override
    public void onBackPressed() {
        saveChangesOrExit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (event == null) {
            return super.onCreateOptionsMenu(menu);
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_detail, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (event == null) {
            return super.onPrepareOptionsMenu(menu);
        }
        MenuItem item = menu.findItem(R.id.menu_done);
        item.setVisible(EventHelper.canBeDone(getEventType()));
        item.setChecked(EventHelper.isDone(event));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (event == null) {
            return super.onOptionsItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.menu_done:
                getPresenter().doneEvent(event);
                return true;
            case R.id.menu_move:
                return true;
            case R.id.menu_delete:
                getPresenter().deleteEvent(event);
                return true;
            case android.R.id.home:
                saveChangesOrExit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveChangesOrExit() {
        T editedEvent = buildEvent();
        if (defaultEvent != null && contentEquals(editedEvent, defaultEvent)) {
            finish();
            return;
        }
        if (event != null && contentEquals(editedEvent, event)) {
            finish();
            return;
        }
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.save_changes_dialog_title)
                .setPositiveButton(R.string.save_changes_dialog_positive_button_text,
                        (DialogInterface dialog, int which) -> {
                            if (event == null) {
                                getPresenter().addEvent(editedEvent, true);
                            } else {
                                getPresenter().updateEvent(editedEvent, true);
                            }
                        })
                .setNegativeButton(R.string.cancel, (dialog, which) -> finish())
                .show();
    }

    protected abstract boolean contentEquals(T event1, T event2);
}
