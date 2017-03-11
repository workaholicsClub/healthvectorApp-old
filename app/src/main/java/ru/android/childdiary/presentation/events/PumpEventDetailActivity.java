package ru.android.childdiary.presentation.events;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.events.core.EventDetailActivity;
import ru.android.childdiary.presentation.events.widgets.EventDetailBreastView;
import ru.android.childdiary.presentation.events.widgets.EventDetailDateView;
import ru.android.childdiary.presentation.events.widgets.EventDetailNotificationTimeView;
import ru.android.childdiary.presentation.events.widgets.EventDetailTimeView;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class PumpEventDetailActivity extends EventDetailActivity<PumpEvent> {
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
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar.setLogo(ResourcesUtils.getPumpEventLogoRes(sex));
        getSupportActionBar().setTitle(R.string.event_pump);
    }

    @Override
    public void showEventDetail(@NonNull PumpEvent event) {
    }

    @Override
    @LayoutRes
    protected int getContentLayoutResourceId() {
        return R.layout.activity_event_detail_pump;
    }

    @Override
    protected void addEvent() {
        presenter.addPumpEvent();
    }

    @Override
    protected void updateEvent() {
    }
}
