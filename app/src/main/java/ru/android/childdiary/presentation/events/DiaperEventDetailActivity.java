package ru.android.childdiary.presentation.events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.DiaperState;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.events.core.EventDetailActivity;
import ru.android.childdiary.presentation.events.core.EventDetailView;
import ru.android.childdiary.presentation.events.widgets.EventDetailDateView;
import ru.android.childdiary.presentation.events.widgets.EventDetailDiaperStateView;
import ru.android.childdiary.presentation.events.widgets.EventDetailNotificationTimeView;
import ru.android.childdiary.presentation.events.widgets.EventDetailTimeView;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class DiaperEventDetailActivity extends EventDetailActivity<EventDetailView<DiaperEvent>, DiaperEvent> implements EventDetailView<DiaperEvent> {
    private static final String TAG_TIME_PICKER = "TIME_PICKER";
    private static final String TAG_DATE_PICKER = "DATE_PICKER";

    @InjectPresenter
    DiaperEventDetailPresenter presenter;

    @BindView(R.id.dateView)
    EventDetailDateView dateView;

    @BindView(R.id.timeView)
    EventDetailTimeView timeView;

    @BindView(R.id.diaperStateView)
    EventDetailDiaperStateView diaperStateView;

    @BindView(R.id.notificationTimeView)
    EventDetailNotificationTimeView notificationTimeView;

    public static Intent getIntent(Context context, @Nullable MasterEvent masterEvent) {
        Intent intent = new Intent(context, DiaperEventDetailActivity.class);
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

        setDateTime(DateTime.now(), dateView, timeView);
        diaperStateView.setSelected(DiaperState.WET);

        dateView.setOnDateClickListener(() -> showDatePicker(TAG_DATE_PICKER, dateView.getDate()));
        timeView.setOnTimeClickListener(() -> showTimePicker(TAG_TIME_PICKER, timeView.getTime()));
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar.setLogo(ResourcesUtils.getDiaperEventLogoRes(sex));
        getSupportActionBar().setTitle(R.string.event_diaper);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        toolbar.setLogo(ResourcesUtils.getDiaperEventLogoRes(sex));
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
    public void showDate(@NonNull LocalDate date) {
        setDateTime(date.toDateTime(LocalTime.now()), dateView, timeView);
    }

    @Override
    public void showEventDetail(@NonNull DiaperEvent event) {
        super.showEventDetail(event);
        setDateTime(event.getDateTime(), dateView, timeView);
        diaperStateView.setSelected(event.getDiaperState());
        notificationTimeView.setMinutes(event.getNotifyTimeInMinutes());
        editTextNote.setText(event.getNote());
    }

    @Override
    public void showDefaultNotifyTime(int minutes) {
        notificationTimeView.setMinutes(minutes);
    }

    @Override
    protected DiaperEvent buildEvent(@Nullable DiaperEvent event) {
        DiaperEvent.DiaperEventBuilder builder = event == null
                ? DiaperEvent.builder().eventType(EventType.DIAPER)
                : event.toBuilder();

        DateTime dateTime = getDateTime(dateView, timeView);
        builder.dateTime(dateTime);

        builder.diaperState(diaperStateView.getSelected());

        builder.notifyTimeInMinutes(notificationTimeView.getMinutes());

        builder.note(editTextNote.getText().toString());

        return builder.build();
    }

    @Override
    protected void setDate(String tag, LocalDate date) {
        dateView.setDate(date);
    }

    @Override
    protected void setTime(String tag, LocalTime time) {
        timeView.setTime(time);
    }
}
