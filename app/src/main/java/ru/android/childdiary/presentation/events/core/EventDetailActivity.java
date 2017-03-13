package ru.android.childdiary.presentation.events.core;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;
import ru.android.childdiary.utils.KeyboardUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public abstract class EventDetailActivity<E extends MasterEvent> extends BaseMvpActivity implements EventDetailView<E>,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    @InjectPresenter
    public EventDetailPresenter presenter;

    protected E event;

    @BindView(R.id.rootView)
    protected View rootView;

    @BindView(R.id.editTextNote)
    CustomEditText editTextNote;

    @BindView(R.id.buttonDone)
    Button buttonDone;

    @BindView(R.id.dummy)
    View dummy;

    private ViewGroup eventDetailsView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        editTextNote.setOnKeyboardHiddenListener(this::hideKeyboardAndClearFocus);
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
        if (event == null) {
            addEvent();
        } else {
            updateEvent();
        }
    }

    @Override
    public void eventAdded(@NonNull MasterEvent event) {
        finish();
    }

    @Override
    public void eventUpdated(@NonNull MasterEvent event) {
        finish();
    }

    @LayoutRes
    protected abstract int getContentLayoutResourceId();

    protected abstract void addEvent();

    protected abstract void updateEvent();

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
}
