package ru.android.childdiary.presentation.events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import butterknife.BindView;
import butterknife.OnClick;
import icepick.State;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.calendar.data.standard.SleepEvent;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.dialogs.TimeDialogArguments;
import ru.android.childdiary.presentation.core.fields.dialogs.TimeDialogFragment;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDateView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDurationView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldNoteView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldNotifyTimeView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldTimeView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldTitleView;
import ru.android.childdiary.presentation.core.fields.widgets.TimerView;
import ru.android.childdiary.presentation.events.core.EventDetailActivity;
import ru.android.childdiary.presentation.events.core.EventDetailView;
import ru.android.childdiary.services.TimerServiceConnection;
import ru.android.childdiary.services.TimerServiceListener;
import ru.android.childdiary.utils.ObjectUtils;
import ru.android.childdiary.utils.strings.EventUtils;
import ru.android.childdiary.utils.strings.TimeUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public class SleepEventDetailActivity extends EventDetailActivity<EventDetailView<SleepEvent>, SleepEvent>
        implements EventDetailView<SleepEvent>, TimerServiceListener {
    private static final String TAG_TIME_PICKER_START = "TIME_PICKER_START";
    private static final String TAG_DATE_PICKER_START = "DATE_PICKER_START";
    private static final String TAG_TIME_PICKER_FINISH = "TIME_PICKER_FINISH";
    private static final String TAG_DATE_PICKER_FINISH = "DATE_PICKER_FINISH";
    private static final String TAG_DURATION_DIALOG = "TAG_DURATION_DIALOG";
    private static final String TAG_NOTIFY_TIME_DIALOG = "TAG_NOTIFY_TIME_DIALOG";

    @InjectPresenter
    SleepEventDetailPresenter presenter;

    @BindView(R.id.startTitleView)
    FieldTitleView startTitleView;

    @BindView(R.id.startDateView)
    FieldDateView startDateView;

    @BindView(R.id.startTimeView)
    FieldTimeView startTimeView;

    @BindView(R.id.finishTitleView)
    FieldTitleView finishTitleView;

    @BindView(R.id.finishDateView)
    FieldDateView finishDateView;

    @BindView(R.id.finishTimeView)
    FieldTimeView finishTimeView;

    @BindView(R.id.durationView)
    FieldDurationView durationView;

    @BindView(R.id.notifyTimeView)
    FieldNotifyTimeView notifyTimeView;

    @BindView(R.id.noteView)
    FieldNoteView noteView;

    @BindView(R.id.timerView)
    TimerView timerView;

    @State
    int defaultNotifyTime;

    private TimerServiceConnection timerServiceConnection = new TimerServiceConnection(this, this);

    public static Intent getIntent(Context context, @Nullable MasterEvent masterEvent,
                                   @NonNull SleepEvent defaultEvent) {
        return new Intent(context, SleepEventDetailActivity.class)
                .putExtra(ExtraConstants.EXTRA_MASTER_EVENT, masterEvent)
                .putExtra(ExtraConstants.EXTRA_DEFAULT_EVENT, defaultEvent);
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

        startDateView.setFieldDialogListener(v -> showDatePicker(TAG_DATE_PICKER_START, startDateView.getValue(),
                null, finishDateView.getValue()));
        startTimeView.setFieldDialogListener(v -> showTimePicker(TAG_TIME_PICKER_START, startTimeView.getValue()));
        finishDateView.setFieldDialogListener(v -> showDatePicker(TAG_DATE_PICKER_FINISH, finishDateView.getValue(),
                startDateView.getValue(), null));
        finishTimeView.setFieldDialogListener(v -> showTimePicker(TAG_TIME_PICKER_FINISH, finishTimeView.getValue()));
        durationView.setFieldDialogListener(v -> {
            TimeDialogFragment dialogFragment = new TimeDialogFragment();
            dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_DURATION_DIALOG,
                    TimeDialogArguments.builder()
                            .sex(getSex())
                            .minutes(durationView.getValueInt())
                            .showDays(durationView.getValueInt() >= TimeUtils.MINUTES_IN_DAY)
                            .showHours(true)
                            .showMinutes(true)
                            .title(getString(R.string.duration))
                            .build());
        });
        notifyTimeView.setFieldDialogListener(v -> {
            TimeDialogFragment dialogFragment = new TimeDialogFragment();
            dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_NOTIFY_TIME_DIALOG,
                    TimeDialogArguments.builder()
                            .sex(getSex())
                            .minutes(notifyTimeView.getValueInt())
                            .showDays(true)
                            .showHours(true)
                            .showMinutes(true)
                            .title(getString(R.string.notify_time_dialog_title))
                            .build());
        });
        setupEditTextView(noteView);
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
        timerView.setSex(getSex());
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
        boolean timerWasStarted = EventUtils.isTimerStarted(event);
        if (timerWasStarted) {
            // выключаем таймер
            event = event.toBuilder()
                    .isTimerStarted(false)
                    .finishDateTime(now.isAfter(event.getDateTime()) ? now : null)
                    .build();
            presenter.updateEvent(event, false);
        } else {
            // включаем таймер
            if (event.getFinishDateTime() == null && event.getId() != null) {
                // обновляем то же событие
                event = event.toBuilder()
                        .isTimerStarted(true)
                        .dateTime(now)
                        .finishDateTime(null)
                        .build();
                presenter.updateEvent(event, false);
            } else {
                // добавляем новое событие
                String note = noteView.getText();
                Integer notifyTime = notifyTimeView.getValue();
                if (event.getId() != null) {
                    note = getDefaultNote();
                    notifyTime = getDefaultNotifyTimeInMinutes();

                    presenter.updateEventSilently(event);
                }
                event = event.toBuilder()
                        .isTimerStarted(true)
                        .dateTime(now)
                        .finishDateTime(null)
                        .note(note)
                        .notifyTimeInMinutes(notifyTime)
                        .build();
                presenter.addEvent(event, false);
            }
        }
    }

    @Override
    public void onTimerTick(@NonNull SleepEvent event) {
        if (sameEvent(event)) {
            updateTimer(event);
        }
    }

    @Override
    public SleepEventDetailPresenter getPresenter() {
        return presenter;
    }

    @Override
    @LayoutRes
    protected int getContentLayoutResourceId() {
        return R.layout.activity_event_detail_sleep;
    }

    @Override
    public void setupEventDetail(@NonNull SleepEvent event) {
        WidgetsUtils.setDateTime(event.getDateTime(), startDateView, startTimeView);
        WidgetsUtils.setDateTime(event.getFinishDateTime(), finishDateView, finishTimeView);
        notifyTimeView.setValue(event.getNotifyTimeInMinutes());
        int visibility = EventUtils.isTimerStarted(event) ? View.GONE : View.VISIBLE;
        notifyTimeView.setVisibility(notifyTimeViewVisible() ? visibility : View.GONE);
        noteView.setText(event.getNote());
        updateDuration();
        updateTimer(event);
    }

    @Override
    protected SleepEvent buildEvent(SleepEvent event) {
        SleepEvent.SleepEventBuilder builder = event == null
                ? SleepEvent.builder()
                : event.toBuilder();

        DateTime startDateTime = WidgetsUtils.getDateTime(startDateView, startTimeView);
        DateTime finishDateTime = WidgetsUtils.getDateTime(finishDateView, finishTimeView);
        if (finishDateTime == null) {
            LocalDate finishDate = finishDateView.getValue();
            LocalTime finishTime = finishTimeView.getValue();
            if (finishDate == null && finishTime != null && startDateTime != null) {
                finishDateTime = startDateTime.withTime(finishTime);
                if (finishDateTime.isBefore(startDateTime)) {
                    finishDateTime = finishDateTime.plusDays(1);
                }
            } else if (finishDate != null && finishTime == null && startDateTime != null) {
                finishDateTime = finishDate.toDateTime(startDateTime.toLocalTime());
            }
        }

        if (!ObjectUtils.equalsToMinutes(builder.build().getDateTime(), startDateTime)) {
            builder.dateTime(startDateTime);
        }

        if (!ObjectUtils.equalsToMinutes(builder.build().getFinishDateTime(), finishDateTime)) {
            builder.finishDateTime(finishDateTime);
        }

        builder.notifyTimeInMinutes(notifyTimeView.getValue())
                .note(noteView.getText());

        return builder.build();
    }

    @Override
    public void showEventDetail(@NonNull SleepEvent event) {
        // если таймер был остановлен из шторки, а пользователь успел отредактировать Примечание
        SleepEvent savedEvent = buildEvent();
        String note = noteView.getText();

        super.showEventDetail(event);

        if (ObjectUtils.equals(savedEvent.getMasterEventId(), event.getMasterEventId())
                && !ObjectUtils.contentEquals(savedEvent.getNote(), event.getNote())) {
            noteView.setText(note);
        }
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
                DateTime start = WidgetsUtils.getDateTime(startDateView, startTimeView);
                DateTime finish = start == null ? null : start.plusMinutes(minutes);
                WidgetsUtils.setDateTime(finish, finishDateView, finishTimeView);
                durationView.setValue(minutes);
                break;
            case TAG_NOTIFY_TIME_DIALOG:
                notifyTimeView.setValue(minutes);
                break;
        }
    }

    private void updateDuration() {
        DateTime start = WidgetsUtils.getDateTime(startDateView, startTimeView);
        DateTime finish = WidgetsUtils.getDateTime(finishDateView, finishTimeView);
        Integer minutes = TimeUtils.durationInMinutes(start, finish);
        durationView.setValue(minutes);
        int visibility = isTimerStarted() ? View.GONE : View.VISIBLE;
        finishTitleView.setVisibility(visibility);
        finishDateView.setVisibility(visibility);
        finishTimeView.setVisibility(visibility);
        durationView.setVisibility(visibility);
        notifyTimeView.setVisibility(notifyTimeViewVisible() ? visibility : View.GONE);
    }

    private void updateTimer(@NonNull SleepEvent event) {
        boolean isTimerStarted = EventUtils.isTimerStarted(event);
        timerView.setOn(isTimerStarted);
        if (isTimerStarted) {
            String text = TimeUtils.timerString(this, event.getDateTime(), DateTime.now());
            timerView.setText(text);
        }
    }

    private void updateIfNeeded() {
        if (isTimerStarted()) {
            presenter.updateEvent(buildEvent(), false);
        }
    }
}
