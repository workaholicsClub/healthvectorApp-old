package ru.android.childdiary.presentation.events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import butterknife.BindView;
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
import ru.android.childdiary.presentation.events.widgets.EventDetailNotifyTimeView;
import ru.android.childdiary.presentation.events.widgets.EventDetailTimeView;
import ru.android.childdiary.presentation.events.widgets.EventDetailTitleView;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class SleepEventDetailActivity extends EventDetailActivity<EventDetailView<SleepEvent>, SleepEvent> implements EventDetailView<SleepEvent> {
    private static final String TAG_TIME_PICKER_START = "TIME_PICKER_START";
    private static final String TAG_DATE_PICKER_START = "DATE_PICKER_START";
    private static final String TAG_TIME_PICKER_FINISH = "TIME_PICKER_FINISH";
    private static final String TAG_DATE_PICKER_FINISH = "DATE_PICKER_FINISH";
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

    @BindView(R.id.notifyTimeView)
    EventDetailNotifyTimeView notifyTimeView;

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

        startDateView.setEventDetailDialogListener(v -> showDatePicker(TAG_DATE_PICKER_START, startDateView.getValue()));
        startTimeView.setEventDetailDialogListener(v -> showTimePicker(TAG_TIME_PICKER_START, startTimeView.getValue()));
        finishDateView.setEventDetailDialogListener(v -> showDatePicker(TAG_DATE_PICKER_FINISH, finishDateView.getValue()));
        finishTimeView.setEventDetailDialogListener(v -> showTimePicker(TAG_TIME_PICKER_FINISH, finishTimeView.getValue()));
        notifyTimeView.setEventDetailDialogListener(v -> presenter.requestTimeDialog(TAG_NOTIFY_TIME_DIALOG,
                TimeDialog.Parameters.builder()
                        .minutes(notifyTimeView.getValueInt())
                        .showDays(true)
                        .showHours(true)
                        .showMinutes(true)
                        .title(getString(R.string.notify_time_dialog_title))
                        .build()));

        if (savedInstanceState == null) {
            setDateTime(DateTime.now(), startDateView, startTimeView);
            setDateTime(DateTime.now(), finishDateView, finishTimeView);
        }
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarLogo(ResourcesUtils.getSleepEventLogoRes(sex));
        setupToolbarTitle(R.string.event_sleep);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        setupToolbarLogo(ResourcesUtils.getSleepEventLogoRes(sex));
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
    public void showDate(@NonNull LocalDate date) {
        setDateTime(date.toDateTime(LocalTime.now()), startDateView, startTimeView);
        setDateTime(date.toDateTime(LocalTime.now()), finishDateView, finishTimeView);
    }

    @Override
    public void showEventDetail(@NonNull SleepEvent event) {
        super.showEventDetail(event);
        setDateTime(event.getDateTime(), startDateView, startTimeView);
        setDateTime(event.getFinishDateTime(), finishDateView, finishTimeView);
        notifyTimeView.setValue(event.getNotifyTimeInMinutes());
        editTextNote.setText(event.getNote());
    }

    @Override
    public void showDefaultNotifyTime(int minutes) {
        notifyTimeView.setValue(minutes);
    }

    @Override
    protected EventType getEventType() {
        return EventType.SLEEP;
    }

    @Override
    protected SleepEvent buildEvent(SleepEvent event) {
        SleepEvent.SleepEventBuilder builder = event == null
                ? SleepEvent.builder().eventType(EventType.SLEEP)
                : event.toBuilder();

        DateTime startDateTime = getDateTime(startDateView, startTimeView);
        builder.dateTime(startDateTime);

        DateTime finishDateTime = getDateTime(finishDateView, finishTimeView);
        builder.finishDateTime(finishDateTime);

        builder.notifyTimeInMinutes(notifyTimeView.getValue());

        builder.note(editTextNote.getText().toString());

        return builder.build();
    }

    @Override
    protected void setDate(String tag, LocalDate date) {
        switch (tag) {
            case TAG_DATE_PICKER_START:
                startDateView.setValue(date);
                break;
            case TAG_DATE_PICKER_FINISH:
                finishDateView.setValue(date);
                break;
        }
    }

    @Override
    protected void setTime(String tag, LocalTime time) {
        switch (tag) {
            case TAG_TIME_PICKER_START:
                startTimeView.setValue(time);
                break;
            case TAG_TIME_PICKER_FINISH:
                finishTimeView.setValue(time);
                break;
        }
    }

    @Override
    public void onSetTime(String tag, int minutes) {
        notifyTimeView.setValue(minutes);
    }
}
