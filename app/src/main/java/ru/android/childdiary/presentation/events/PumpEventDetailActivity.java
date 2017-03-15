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
import ru.android.childdiary.data.types.Breast;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.events.core.EventDetailActivity;
import ru.android.childdiary.presentation.events.core.EventDetailView;
import ru.android.childdiary.presentation.events.widgets.EventDetailBreastView;
import ru.android.childdiary.presentation.events.widgets.EventDetailDateView;
import ru.android.childdiary.presentation.events.widgets.EventDetailNotifyTimeView;
import ru.android.childdiary.presentation.events.widgets.EventDetailTimeView;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class PumpEventDetailActivity extends EventDetailActivity<EventDetailView<PumpEvent>, PumpEvent> implements EventDetailView<PumpEvent> {
    private static final String TAG_TIME_PICKER = "TIME_PICKER";
    private static final String TAG_DATE_PICKER = "DATE_PICKER";

    @InjectPresenter
    PumpEventDetailPresenter presenter;

    @BindView(R.id.dateView)
    EventDetailDateView dateView;

    @BindView(R.id.timeView)
    EventDetailTimeView timeView;

    @BindView(R.id.breastView)
    EventDetailBreastView breastView;

    @BindView(R.id.notifyTimeView)
    EventDetailNotifyTimeView notifyTimeView;

    public static Intent getIntent(Context context, @Nullable MasterEvent masterEvent) {
        Intent intent = new Intent(context, PumpEventDetailActivity.class);
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
        breastView.setSelected(Breast.LEFT);
        // TODO duration

        dateView.setEventDetailDialogListener(v -> showDatePicker(TAG_DATE_PICKER, dateView.getValue()));
        timeView.setEventDetailDialogListener(v -> showTimePicker(TAG_TIME_PICKER, timeView.getValue()));
        notifyTimeView.setEventDetailDialogListener(v -> presenter.requestNotifyTimeDialog());
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar.setLogo(ResourcesUtils.getPumpEventLogoRes(sex));
        getSupportActionBar().setTitle(R.string.event_pump);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        toolbar.setLogo(ResourcesUtils.getPumpEventLogoRes(sex));
        breastView.setSex(sex);
    }

    @Override
    public PumpEventDetailPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected EventType getEventType() {
        return EventType.PUMP;
    }

    @Override
    @LayoutRes
    protected int getContentLayoutResourceId() {
        return R.layout.activity_event_detail_pump;
    }

    @Override
    public void showDate(@NonNull LocalDate date) {
        setDateTime(date.toDateTime(LocalTime.now()), dateView, timeView);
    }

    @Override
    public void showEventDetail(@NonNull PumpEvent event) {
        super.showEventDetail(event);
        setDateTime(event.getDateTime(), dateView, timeView);
        breastView.setSelected(event.getBreast());
        // TODO duration
        notifyTimeView.setValue(event.getNotifyTimeInMinutes());
        editTextNote.setText(event.getNote());
    }

    @Override
    public void showDefaultNotifyTime(int minutes) {
        notifyTimeView.setValue(minutes);
    }

    @Override
    protected PumpEvent buildEvent(PumpEvent event) {
        PumpEvent.PumpEventBuilder builder = event == null
                ? PumpEvent.builder().eventType(EventType.PUMP)
                : event.toBuilder();

        DateTime dateTime = getDateTime(dateView, timeView);
        builder.dateTime(dateTime);

        builder.breast(breastView.getSelected());

        // TODO duration

        builder.notifyTimeInMinutes(notifyTimeView.getValue());

        builder.note(editTextNote.getText().toString());

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
}
