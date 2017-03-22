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
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.events.core.EventDetailActivity;
import ru.android.childdiary.presentation.events.core.EventDetailView;
import ru.android.childdiary.presentation.events.dialogs.TimeDialog;
import ru.android.childdiary.presentation.events.widgets.EventDetailDateView;
import ru.android.childdiary.presentation.events.widgets.EventDetailDiaperStateView;
import ru.android.childdiary.presentation.events.widgets.EventDetailNotifyTimeView;
import ru.android.childdiary.presentation.events.widgets.EventDetailTimeView;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class DiaperEventDetailActivity extends EventDetailActivity<EventDetailView<DiaperEvent>, DiaperEvent> implements EventDetailView<DiaperEvent> {
    private static final String TAG_TIME_PICKER = "TIME_PICKER";
    private static final String TAG_DATE_PICKER = "DATE_PICKER";
    private static final String TAG_NOTIFY_TIME_DIALOG = "TAG_NOTIFY_TIME_DIALOG";

    @InjectPresenter
    DiaperEventDetailPresenter presenter;

    @BindView(R.id.dateView)
    EventDetailDateView dateView;

    @BindView(R.id.timeView)
    EventDetailTimeView timeView;

    @BindView(R.id.diaperStateView)
    EventDetailDiaperStateView diaperStateView;

    @BindView(R.id.notifyTimeView)
    EventDetailNotifyTimeView notifyTimeView;

    public static Intent getIntent(Context context, @Nullable MasterEvent masterEvent, boolean readOnly) {
        Intent intent = new Intent(context, DiaperEventDetailActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_MASTER_EVENT, masterEvent);
        intent.putExtra(ExtraConstants.EXTRA_READ_ONLY, readOnly);
        return intent;
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dateView.setEventDetailDialogListener(v -> showDatePicker(TAG_DATE_PICKER, dateView.getValue()));
        timeView.setEventDetailDialogListener(v -> showTimePicker(TAG_TIME_PICKER, timeView.getValue()));
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
        setupToolbarLogo(ResourcesUtils.getDiaperEventLogoRes(sex));
        setupToolbarTitle(R.string.event_diaper);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        setupToolbarLogo(ResourcesUtils.getDiaperEventLogoRes(sex));
        diaperStateView.setSex(sex);
    }

    @Override
    public DiaperEventDetailPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected EventType getEventType() {
        return EventType.DIAPER;
    }

    @Override
    @LayoutRes
    protected int getContentLayoutResourceId() {
        return R.layout.activity_event_detail_diaper;
    }

    @Override
    public void showEventDetail(@NonNull DiaperEvent event) {
        super.showEventDetail(event);
        setDateTime(event.getDateTime(), dateView, timeView);
        diaperStateView.setSelected(event.getDiaperState());
        notifyTimeView.setValue(event.getNotifyTimeInMinutes());
        editTextNote.setText(event.getNote());
    }

    @Override
    protected DiaperEvent buildEvent(@Nullable DiaperEvent event) {
        DiaperEvent.DiaperEventBuilder builder = event == null
                ? DiaperEvent.builder().eventType(EventType.DIAPER)
                : event.toBuilder();

        DateTime dateTime = getDateTime(dateView, timeView);

        builder.dateTime(dateTime)
                .diaperState(diaperStateView.getSelected())
                .notifyTimeInMinutes(notifyTimeView.getValue())
                .note(editTextNote.getText().toString());

        return builder.build();
    }

    @Override
    protected void setDate(String tag, LocalDate date) {
        dateView.setValue(date);
    }

    @Override
    protected void setTime(String tag, LocalTime time) {
        timeView.setValue(time);
    }

    @Override
    public void onSetTime(String tag, int minutes) {
        notifyTimeView.setValue(minutes);
    }
}
