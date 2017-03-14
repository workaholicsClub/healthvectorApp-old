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
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.events.core.EventDetailActivity;
import ru.android.childdiary.presentation.events.core.EventDetailView;
import ru.android.childdiary.presentation.events.widgets.EventDetailBreastView;
import ru.android.childdiary.presentation.events.widgets.EventDetailDateView;
import ru.android.childdiary.presentation.events.widgets.EventDetailNotificationTimeView;
import ru.android.childdiary.presentation.events.widgets.EventDetailTimeView;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class PumpEventDetailActivity extends EventDetailActivity<PumpEvent> implements EventDetailView<PumpEvent> {
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

    @BindView(R.id.notificationTimeView)
    EventDetailNotificationTimeView notificationTimeView;

    public static Intent getIntent(Context context, @Nullable Long masterEventId) {
        Intent intent = new Intent(context, PumpEventDetailActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_EVENT_ID, masterEventId);
        return intent;
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dateView.setOnDateClickListener(() -> showDatePicker(TAG_DATE_PICKER, dateView.getDate()));
        timeView.setOnTimeClickListener(() -> showTimePicker(TAG_TIME_PICKER, timeView.getTime()));
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
        breastView.setSex(sex);
    }

    @Override
    public PumpEventDetailPresenter getPresenter() {
        return presenter;
    }

    @Override
    @LayoutRes
    protected int getContentLayoutResourceId() {
        return R.layout.activity_event_detail_pump;
    }

    @Override
    public void showEventDetail(@NonNull PumpEvent event) {
    }

    @Override
    protected PumpEvent buildEvent() {
        return PumpEvent.builder().eventType(EventType.PUMP).dateTime(DateTime.now()).build();
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
