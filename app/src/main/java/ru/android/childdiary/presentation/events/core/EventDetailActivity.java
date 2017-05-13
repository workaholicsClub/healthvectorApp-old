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

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.dialogs.TimeDialogFragment;
import ru.android.childdiary.presentation.core.fields.widgets.FieldEditTextView;
import ru.android.childdiary.presentation.core.widgets.CustomDatePickerDialog;
import ru.android.childdiary.presentation.core.widgets.CustomTimePickerDialog;
import ru.android.childdiary.utils.EventHelper;
import ru.android.childdiary.utils.ObjectUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public abstract class EventDetailActivity<V extends EventDetailView<T>, T extends MasterEvent> extends BaseMvpActivity implements
        EventDetailView<T>, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, TimeDialogFragment.Listener {
    @BindView(R.id.buttonAdd)
    protected Button buttonAdd;

    @BindView(R.id.rootView)
    protected View rootView;

    private ViewGroup detailsView;

    private T defaultEvent;
    @Nullable
    private T event;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        MasterEvent masterEvent = (MasterEvent) getIntent().getSerializableExtra(ExtraConstants.EXTRA_MASTER_EVENT);
        //noinspection unchecked
        defaultEvent = (T) getIntent().getSerializableExtra(ExtraConstants.EXTRA_DEFAULT_EVENT);

        if (masterEvent == null) {
            buttonAdd.setVisibility(VISIBLE);
            buttonAdd.setOnClickListener(v -> getPresenter().addEvent(buildEvent(), true));
        } else {
            buttonAdd.setVisibility(GONE);
            if (savedInstanceState == null) {
                getPresenter().requestEventDetails(masterEvent);
            }
        }

        changeThemeIfNeeded(defaultEvent.getChild());
        setupEventDetail(defaultEvent);

        logger.debug("master event: " + masterEvent);
        logger.debug("default event: " + defaultEvent);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //noinspection unchecked
        T event = (T) savedInstanceState.getSerializable(ExtraConstants.EXTRA_EVENT);
        setupEventDetail(event); // в таймере сна дергается время
        logger.debug("restore event: " + event);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        T event = buildEvent();
        outState.putSerializable(ExtraConstants.EXTRA_EVENT, event);
        logger.debug("save event: " + event);
    }

    @Override
    protected void setContentViewBeforeBind() {
        LayoutInflater inflater = LayoutInflater.from(this);
        detailsView = ButterKnife.findById(this, R.id.detailsView);
        View contentView = inflater.inflate(getContentLayoutResourceId(), detailsView);
    }

    protected void setupEditTextView(FieldEditTextView view) {
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

    @Override
    protected void onPause() {
        super.onPause();
        hideKeyboardAndClearFocus(rootView.findFocus());
    }

    protected final T buildEvent() {
        return buildEvent(event == null ? defaultEvent : event);
    }

    @Override
    public final void showEventDetail(@NonNull T event) {
        logger.debug("show event: " + event);
        this.event = event;

        changeThemeIfNeeded(event.getChild());
        setupEventDetail(event);

        invalidateOptionsMenu();
        getToolbar().setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.toolbar_action_overflow));
        buttonAdd.setVisibility(GONE);
        buttonAdd.setOnClickListener(null);
    }

    protected abstract EventDetailPresenter<V, T> getPresenter();

    @LayoutRes
    protected abstract int getContentLayoutResourceId();

    protected abstract void setupEventDetail(@NonNull T event);

    protected abstract T buildEvent(@Nullable T event);

    @Override
    public void eventAdded(@NonNull T event, boolean afterButtonPressed) {
        setResult(RESULT_OK);
        if (afterButtonPressed) {
            finish();
        }
    }

    @Override
    public void eventUpdated(@NonNull T event, boolean afterButtonPressed) {
        setResult(RESULT_OK);
        if (afterButtonPressed) {
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
    public void validationFailed() {
    }

    @Override
    public void showValidationErrorMessage(String msg) {
        showToast(msg);
    }

    @Override
    public void setButtonAddEnabled(boolean enabled) {
        buttonAdd.setEnabled(enabled);
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
    }

    @Override
    public final void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        LocalTime time = new LocalTime(hourOfDay, minute);
        setTime(view.getTag(), time);
    }

    protected void setTime(String tag, LocalTime time) {
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
        item.setVisible(EventHelper.canBeDone(getPresenter().getEventType()));
        item.setChecked(EventHelper.isDone(event));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveChangesOrExit();
            return true;
        }
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveChangesOrExit() {
        T editedEvent = buildEvent();
        if (event == null && contentEquals(editedEvent, defaultEvent)) {
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

    protected final boolean notifyTimeViewVisible() {
        return defaultEvent != null && ObjectUtils.isPositive(defaultEvent.getNotifyTimeInMinutes());
    }

    @Nullable
    protected final String getDefaultNote() {
        return defaultEvent == null ? null : defaultEvent.getNote();
    }

    @Nullable
    protected final Integer getDefaultNotifyTimeInMinutes() {
        return defaultEvent == null ? null : defaultEvent.getNotifyTimeInMinutes();
    }

    protected final boolean sameEvent(@NonNull SleepEvent event) {
        return EventHelper.sameEvent(this.event, event);
    }

    protected final boolean isTimerStarted() {
        return event instanceof SleepEvent && EventHelper.isTimerStarted((SleepEvent) event);
    }
}
