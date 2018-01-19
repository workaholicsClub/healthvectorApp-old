package ru.android.childdiary.presentation.events.core;

import android.app.Fragment;
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
import android.view.ViewGroup;
import android.widget.Button;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.calendar.data.core.LinearGroupFieldType;
import ru.android.childdiary.domain.calendar.data.core.LinearGroupItem;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.calendar.data.standard.SleepEvent;
import ru.android.childdiary.domain.core.data.ContentObject;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.dialogs.TimeDialogFragment;
import ru.android.childdiary.presentation.core.widgets.CustomDatePickerDialog;
import ru.android.childdiary.presentation.profile.dialogs.TimePickerDialogArguments;
import ru.android.childdiary.presentation.profile.dialogs.TimePickerDialogFragment;
import ru.android.childdiary.utils.strings.EventUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public abstract class EventDetailActivity<V extends EventDetailView<T>, T extends MasterEvent & ContentObject<T> & LinearGroupItem<T>>
        extends BaseMvpActivity
        implements EventDetailView<T>, TimeDialogFragment.Listener,
        DatePickerDialog.OnDateSetListener, TimePickerDialogFragment.Listener {
    private static final String TAG_PROGRESS_DIALOG_DELETING_EVENTS = "TAG_PROGRESS_DIALOG_DELETING_EVENTS";
    private static final String TAG_PROGRESS_DIALOG_UPDATING_EVENTS = "TAG_PROGRESS_DIALOG_UPDATING_EVENTS";

    @BindView(R.id.buttonAdd)
    protected Button buttonAdd;

    private ViewGroup detailsView;

    @Nullable
    private T event;
    private T defaultEvent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        MasterEvent masterEvent = (MasterEvent) getIntent().getSerializableExtra(ExtraConstants.EXTRA_MASTER_EVENT);
        //noinspection unchecked
        defaultEvent = (T) getIntent().getSerializableExtra(ExtraConstants.EXTRA_DEFAULT_EVENT);

        if (masterEvent == null) {
            setupNewEventUi();
            changeThemeIfNeeded(defaultEvent.getChild());
            setupEventDetail(defaultEvent);
            buttonAdd.setVisibility(VISIBLE);
            buttonAdd.setOnClickListener(v -> getPresenter().addEvent(buildEvent(), true));
        } else {
            changeThemeIfNeeded(masterEvent.getChild());
            if (masterEvent.getClass() == defaultEvent.getClass()) {
                //noinspection unchecked
                setupEventDetail((T) masterEvent);
            }
            buttonAdd.setVisibility(GONE);
            if (savedInstanceState == null) {
                getPresenter().requestEventDetails(masterEvent);
            }
        }

        logger.debug("master event: " + masterEvent);
        logger.debug("default event: " + defaultEvent);
    }

    @Override
    protected void setContentViewBeforeBind() {
        LayoutInflater inflater = LayoutInflater.from(this);
        detailsView = ButterKnife.findById(this, R.id.detailsView);
        inflater.inflate(getContentLayoutResourceId(), detailsView);
    }

    protected void setupNewEventUi() {
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //noinspection unchecked
        T event = (T) savedInstanceState.getSerializable(ExtraConstants.EXTRA_EVENT);
        if (event != null) {
            setupEventDetail(event); // в таймере сна дергается время
            logger.debug("restore event: " + event);
        } else {
            logger.error("event is null");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        T event = buildEvent();
        outState.putSerializable(ExtraConstants.EXTRA_EVENT, event);
        logger.debug("save event: " + event);
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
        hideKeyboardAndClearFocus();
    }

    protected final T buildEvent() {
        return buildEvent(event == null ? defaultEvent : event);
    }

    @Override
    public void showEventDetail(@NonNull T event) {
        logger.debug("show event: " + event);
        this.event = event;

        changeThemeIfNeeded(event.getChild());
        setupEventDetail(event);

        invalidateOptionsMenu();
        if (getToolbar() != null) {
            getToolbar().setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.toolbar_action_overflow));
        }
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
    public void eventDone(boolean done) {
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
    public void confirmDeleteOneEvent(@NonNull MasterEvent event) {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.delete_event_confirmation_dialog_title)
                .setPositiveButton(R.string.delete,
                        (dialog, which) -> getPresenter().deleteOneEvent(event))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void askDeleteOneEventOrLinerGroup(@NonNull MasterEvent event) {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.ask_delete_one_event_or_linear_group)
                .setPositiveButton(R.string.delete_one_event,
                        (dialog, which) -> getPresenter().deleteOneEvent(event))
                .setNegativeButton(R.string.delete_linear_group,
                        (dialog, which) -> getPresenter().deleteLinearGroup(event))
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
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof DatePickerDialog) {
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
        TimePickerDialogFragment dialogFragment = new TimePickerDialogFragment();
        dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), tag,
                TimePickerDialogArguments.builder()
                        .sex(getSex())
                        .title(getString(R.string.time))
                        .time(time)
                        .build());
    }

    @Override
    public void onTimePick(String tag, @NonNull LocalTime time) {
        setTime(tag, time);
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
        removeToolbarMargin();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete, menu);
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
            case R.id.menu_delete:
                getPresenter().delete(event);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveChangesOrExit() {
        T editedEvent = buildEvent();
        if (event == null && editedEvent.isContentEqual(defaultEvent)) {
            finish();
            return;
        }
        if (event != null && editedEvent.isContentEqual(event)) {
            processContentEquals(event, editedEvent);
            return;
        }
        if (event == null) {
            // вставка
            processAddEvent(editedEvent);
        } else {
            // обновление
            if (event.getLinearGroup() == null) {
                // нет линейной группы, обновляем событие
                processOneEventUpdate(editedEvent);
            } else {
                // есть линейная группа
                List<LinearGroupFieldType> fields = editedEvent.getChangedFields(event);
                if (fields.isEmpty()) {
                    // изменены поля, которые не должны влиять на всю линейную группу
                    processOneEventUpdate(editedEvent);
                } else {
                    // изменены поля, которые могут повлиять на всю линейную группу
                    processLinearGroupUpdate(editedEvent, fields);
                }
            }
        }
    }

    private void processContentEquals(@NonNull T event, @NonNull T editedEvent) {
        if (EventUtils.isDone(editedEvent) == EventUtils.isDone(event)) {
            finish();
        } else {
            getPresenter().updateEvent(editedEvent, true);
        }
    }

    private void processAddEvent(T editedEvent) {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.save_changes_dialog_title)
                .setPositiveButton(R.string.save,
                        (dialog, which) -> getPresenter().addEvent(editedEvent, true))
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> finish())
                .show();
    }

    private void processOneEventUpdate(T editedEvent) {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.save_changes_dialog_title)
                .setPositiveButton(R.string.save,
                        (dialog, which) -> getPresenter().updateEvent(editedEvent, true))
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> finish())
                .show();
    }

    private void processLinearGroupUpdate(T editedEvent, List<LinearGroupFieldType> fields) {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.ask_update_linear_group)
                .setPositiveButton(R.string.update_one_event,
                        (dialog, which) -> getPresenter().updateEvent(editedEvent, true))
                .setNegativeButton(R.string.update_linear_group,
                        (dialog, which) -> getPresenter().updateLinearGroup(editedEvent, fields, true))
                .setNeutralButton(R.string.cancel,
                        (dialog, which) -> finish())
                .show();
    }

    protected final boolean notifyTimeViewVisible() {
        return defaultEvent != null && defaultEvent.getNotifyTimeInMinutes() != null;
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
        return EventUtils.sameEvent(this.event, event);
    }

    protected final boolean isTimerStarted() {
        return event instanceof SleepEvent && EventUtils.isTimerStarted((SleepEvent) event);
    }
}
