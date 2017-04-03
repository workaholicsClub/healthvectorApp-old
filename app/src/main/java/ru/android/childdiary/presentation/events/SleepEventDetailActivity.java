package ru.android.childdiary.presentation.events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.events.core.EventDetailActivity;
import ru.android.childdiary.presentation.events.core.EventDetailView;
import ru.android.childdiary.presentation.events.dialogs.TimeDialog;
import ru.android.childdiary.presentation.events.widgets.EventDetailDateView;
import ru.android.childdiary.presentation.events.widgets.EventDetailDurationView;
import ru.android.childdiary.presentation.events.widgets.EventDetailNotifyTimeView;
import ru.android.childdiary.presentation.events.widgets.EventDetailTimeView;
import ru.android.childdiary.presentation.events.widgets.EventDetailTitleView;
import ru.android.childdiary.services.TimerServiceConnection;
import ru.android.childdiary.services.TimerServiceListener;
import ru.android.childdiary.utils.EventHelper;
import ru.android.childdiary.utils.ObjectUtils;
import ru.android.childdiary.utils.TimeUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public class SleepEventDetailActivity extends EventDetailActivity<EventDetailView<SleepEvent>, SleepEvent> implements EventDetailView<SleepEvent>,
        TimerServiceListener {
    private static final String TAG_TIME_PICKER_START = "TIME_PICKER_START";
    private static final String TAG_DATE_PICKER_START = "DATE_PICKER_START";
    private static final String TAG_TIME_PICKER_FINISH = "TIME_PICKER_FINISH";
    private static final String TAG_DATE_PICKER_FINISH = "DATE_PICKER_FINISH";
    private static final String TAG_DURATION_DIALOG = "TAG_DURATION_DIALOG";
    private static final String TAG_NOTIFY_TIME_DIALOG = "TAG_NOTIFY_TIME_DIALOG";

    @InjectPresenter
    SleepEventDetailPresenter presenter;

    @BindView(R.id.startTitleView)
    EventDetailTitleView startTitleView;

    @BindView(R.id.startDateView)
    EventDetailDateView startDateView;

    @BindView(R.id.startTimeView)
    EventDetailTimeView startTimeView;

    @BindView(R.id.finishTitleView)
    EventDetailTitleView finishTitleView;

    @BindView(R.id.finishDateView)
    EventDetailDateView finishDateView;

    @BindView(R.id.finishTimeView)
    EventDetailTimeView finishTimeView;

    @BindView(R.id.durationView)
    EventDetailDurationView durationView;

    @BindView(R.id.notifyTimeView)
    EventDetailNotifyTimeView notifyTimeView;

    @BindView(R.id.buttonTimer)
    Button buttonTimer;

    private boolean notifyTimeViewVisible;
    private TimerServiceConnection timerServiceConnection = new TimerServiceConnection(this, this);

    public static Intent getIntent(Context context, @Nullable MasterEvent masterEvent) {
        Intent intent = new Intent(context, SleepEventDetailActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_MASTER_EVENT, masterEvent);
        return intent;
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startTitleView.setTitle(R.string.asleep);
        finishTitleView.setTitle(R.string.awoke);

        startDateView.setEventDetailDialogListener(v -> showDatePicker(TAG_DATE_PICKER_START, startDateView.getValue(),
                null, finishDateView.getValue()));
        startTimeView.setEventDetailDialogListener(v -> showTimePicker(TAG_TIME_PICKER_START, startTimeView.getValue()));
        finishDateView.setEventDetailDialogListener(v -> showDatePicker(TAG_DATE_PICKER_FINISH, finishDateView.getValue(),
                startDateView.getValue(), null));
        finishTimeView.setEventDetailDialogListener(v -> showTimePicker(TAG_TIME_PICKER_FINISH, finishTimeView.getValue()));
        durationView.setEventDetailDialogListener(v -> presenter.requestTimeDialog(TAG_DURATION_DIALOG,
                TimeDialog.Parameters.builder()
                        .minutes(durationView.getValueInt())
                        .showDays(durationView.getValueInt() >= TimeUtils.MINUTES_IN_DAY)
                        .showHours(true)
                        .showMinutes(true)
                        .title(getString(R.string.duration))
                        .build()));
        notifyTimeView.setEventDetailDialogListener(v -> presenter.requestTimeDialog(TAG_NOTIFY_TIME_DIALOG,
                TimeDialog.Parameters.builder()
                        .minutes(notifyTimeView.getValueInt())
                        .showDays(true)
                        .showHours(true)
                        .showMinutes(true)
                        .title(getString(R.string.notify_time_dialog_title))
                        .build()));
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarLogo(ResourcesUtils.getSleepEventLogoRes(getSex()));
        setupToolbarTitle(R.string.event_sleep);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        setupToolbarLogo(ResourcesUtils.getSleepEventLogoRes(getSex()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        timerServiceConnection = new TimerServiceConnection(this, this);
        timerServiceConnection.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerServiceConnection.close();
    }

    @OnClick(R.id.buttonTimer)
    void onTimerClick() {
        DateTime now = DateTime.now();
        SleepEvent event = buildEvent();
        boolean timerWasStarted = EventHelper.isTimerStarted(event);
        if (timerWasStarted) {
            // выключаем таймер
            event = event.toBuilder()
                    .isTimerStarted(false)
                    .finishDateTime(now.isAfter(event.getDateTime()) ? now : null)
                    .build();
            presenter.updateEvent(event, false);
        } else {
            // включаем таймер
            event = event.toBuilder()
                    .isTimerStarted(true)
                    .dateTime(now)
                    .finishDateTime(null)
                    .build();
            presenter.addEvent(event, false);
        }
    }

    @Override
    public void onTimerTick(@NonNull SleepEvent event) {
        if (EventHelper.sameEvent(this.event, event)) {
            updateTimer(event);
        }
    }

    @Override
    public SleepEventDetailPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected EventType getEventType() {
        return EventType.SLEEP;
    }

    @Override
    @LayoutRes
    protected int getContentLayoutResourceId() {
        return R.layout.activity_event_detail_sleep;
    }

    @Override
    public void setupEventDetail(@NonNull SleepEvent event) {
        setDateTime(event.getDateTime(), startDateView, startTimeView);
        setDateTime(event.getFinishDateTime(), finishDateView, finishTimeView);
        notifyTimeView.setValue(event.getNotifyTimeInMinutes());
        noteView.setText(event.getNote());
        updateDuration();
        updateTimer(event);
    }

    @Override
    public void showNotifyTimeView(boolean visible) {
        notifyTimeViewVisible = visible;
        int visibility = EventHelper.isTimerStarted(event) ? View.GONE : View.VISIBLE;
        notifyTimeView.setVisibility(visible ? visibility : View.GONE);
    }

    @Override
    protected SleepEvent buildEvent(SleepEvent event) {
        SleepEvent.SleepEventBuilder builder = event == null
                ? SleepEvent.builder()
                : event.toBuilder();

        DateTime startDateTime = getDateTime(startDateView, startTimeView);
        DateTime finishDateTime = getDateTime(finishDateView, finishTimeView);

        builder.dateTime(startDateTime)
                .finishDateTime(finishDateTime)
                .notifyTimeInMinutes(notifyTimeView.getValue())
                .note(noteView.getText());

        return builder.build();
    }

    @Override
    protected void setDate(String tag, LocalDate date) {
        switch (tag) {
            case TAG_DATE_PICKER_START:
                startDateView.setValue(date);
                updateDuration();
                updateIfNeeded();
                break;
            case TAG_DATE_PICKER_FINISH:
                finishDateView.setValue(date);
                updateDuration();
                break;
        }
    }

    @Override
    protected void setTime(String tag, LocalTime time) {
        switch (tag) {
            case TAG_TIME_PICKER_START:
                startTimeView.setValue(time);
                updateDuration();
                updateIfNeeded();
                break;
            case TAG_TIME_PICKER_FINISH:
                finishTimeView.setValue(time);
                updateDuration();
                break;
        }
    }

    @Override
    public void onSetTime(String tag, int minutes) {
        switch (tag) {
            case TAG_DURATION_DIALOG:
                DateTime start = getDateTime(startDateView, startTimeView);
                DateTime finish = start.plusMinutes(minutes);
                setDateTime(finish, finishDateView, finishTimeView);
                durationView.setValue(minutes);
                break;
            case TAG_NOTIFY_TIME_DIALOG:
                notifyTimeView.setValue(minutes);
                break;
        }
    }

    private void updateDuration() {
        DateTime start = getDateTime(startDateView, startTimeView);
        DateTime finish = getDateTime(finishDateView, finishTimeView);
        Integer minutes = TimeUtils.durationInMinutes(start, finish);
        durationView.setValue(minutes);
        int visibility = EventHelper.isTimerStarted(event) ? View.GONE : View.VISIBLE;
        finishDateView.setVisibility(visibility);
        finishTimeView.setVisibility(visibility);
        durationView.setVisibility(visibility);
        notifyTimeView.setVisibility(notifyTimeViewVisible ? visibility : View.GONE);
    }

    private void updateTimer(@NonNull SleepEvent event) {
        if (EventHelper.isTimerStarted(event)) {
            String text = TimeUtils.timerString(this, event.getDateTime(), DateTime.now());
            buttonTimer.setText(text);
            WidgetsUtils.setupTimer(this, buttonTimer, getSex(), true);
        } else {
            WidgetsUtils.setupTimer(this, buttonTimer, getSex(), false);
        }
    }

    private void updateIfNeeded() {
        if (EventHelper.isTimerStarted(event)) {
            presenter.updateEvent(buildEvent(), false);
        }
    }

    @Override
    protected boolean contentEquals(SleepEvent event1, SleepEvent event2) {
        return ObjectUtils.contentEquals(event1, event2);
    }
}
