package ru.android.childdiary.presentation.events.core;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;
import ru.android.childdiary.presentation.events.widgets.EventDetailDateView;
import ru.android.childdiary.presentation.events.widgets.EventDetailTimeView;
import ru.android.childdiary.utils.KeyboardUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public abstract class EventDetailActivity<V extends EventDetailView<T>, T extends MasterEvent> extends BaseMvpActivity implements
        EventDetailView<T>, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    @BindView(R.id.editTextNote)
    protected CustomEditText editTextNote;
    protected T event;
    @BindView(R.id.rootView)
    View rootView;
    @BindView(R.id.buttonDone)
    Button buttonDone;
    @BindView(R.id.dummy)
    View dummy;
    private ViewGroup eventDetailsView;
    private MasterEvent masterEvent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        editTextNote.setOnKeyboardHiddenListener(this::hideKeyboardAndClearFocus);

        masterEvent = (MasterEvent) getIntent().getSerializableExtra(ExtraConstants.EXTRA_MASTER_EVENT);
        if (savedInstanceState == null) {
            if (masterEvent == null) {
                getPresenter().requestDate();
                getPresenter().requestDefaultValues(getEventType());
            } else {
                getPresenter().requestEventDetails(masterEvent);
            }
        }
    }

    @Override
    protected void setContentViewBeforeBind() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View contentView = inflater.inflate(getContentLayoutResourceId(), null);
        eventDetailsView = ButterKnife.findById(this, R.id.eventDetailsView);
        eventDetailsView.addView(contentView);
    }

    private void hideKeyboardAndClearFocus(View view) {
        KeyboardUtils.hideKeyboard(this, view);
        view.clearFocus();
        dummy.requestFocus();
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar.setNavigationIcon(R.drawable.toolbar_action_close);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        buttonDone.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(sex, true));
    }

    @OnClick(R.id.buttonDone)
    void onButtonDoneClick() {
        if (masterEvent == null) {
            getPresenter().addEvent(buildEvent(null));
        } else {
            getPresenter().updateEvent(buildEvent(event));
        }
    }

    @Override
    public void showChild(@NonNull Child child) {
        logger.debug("showChild: " + child);
        changeThemeIfNeeded(child);
    }

    @Override
    @CallSuper
    public void showEventDetail(@NonNull T event) {
        this.event = event;
    }

    protected abstract EventDetailPresenter<V, T> getPresenter();

    protected abstract EventType getEventType();

    @LayoutRes
    protected abstract int getContentLayoutResourceId();

    protected abstract T buildEvent(@Nullable T event);

    @Override
    public void eventAdded(@NonNull T event) {
        finish();
    }

    @Override
    public void eventUpdated(@NonNull T event) {
        finish();
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

    protected void showDatePicker(String tag, @Nullable LocalDate date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date.toDate());
        }
        DatePickerDialog dpd = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dpd.vibrate(false);
        WidgetsUtils.setupDatePicker(this, dpd, sex);
        dpd.show(getFragmentManager(), tag);
        hideKeyboardAndClearFocus(rootView.findFocus());
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        LocalDate date = LocalDate.fromCalendarFields(calendar);
        setDate(view.getTag(), date);
    }

    protected void setDate(String tag, LocalDate date) {
    }

    protected void showTimePicker(String tag, @Nullable LocalTime time) {
        if (time == null) {
            time = LocalTime.now();
        }
        TimePickerDialog tpd = TimePickerDialog.newInstance(this,
                time.getHourOfDay(), time.getMinuteOfHour(), DateFormat.is24HourFormat(this));
        tpd.vibrate(false);
        WidgetsUtils.setupTimePicker(this, tpd, sex);
        tpd.show(getFragmentManager(), tag);
        hideKeyboardAndClearFocus(rootView.findFocus());
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        LocalTime time = new LocalTime(hourOfDay, minute);
        setTime(view.getTag(), time);
    }

    protected void setTime(String tag, LocalTime time) {
    }

    protected DateTime getDateTime(EventDetailDateView dateView, EventDetailTimeView timeView) {
        LocalDate date = dateView.getDate();
        LocalTime time = timeView.getTime();
        return new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(),
                time.getHourOfDay(), time.getMinuteOfHour());
    }

    protected void setDateTime(DateTime dateTime, EventDetailDateView dateView, EventDetailTimeView timeView) {
        dateView.setDate(dateTime.toLocalDate());
        timeView.setTime(dateTime.toLocalTime());
    }
}
