package ru.android.childdiary.presentation.events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.events.core.EventDetailActivity;
import ru.android.childdiary.presentation.events.widgets.EventDetailDateView;
import ru.android.childdiary.presentation.events.widgets.EventDetailNotificationTimeView;
import ru.android.childdiary.presentation.events.widgets.EventDetailTimeView;
import ru.android.childdiary.presentation.events.widgets.EventDetailTitleView;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class SleepEventDetailActivity extends EventDetailActivity<SleepEvent> {
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

    @BindView(R.id.notificationTimeView)
    EventDetailNotificationTimeView notificationTimeView;

    public static Intent getIntent(Context context, @Nullable Long masterEventId) {
        Intent intent = new Intent(context, SleepEventDetailActivity.class);
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

        startTitleView.setTitle(R.string.asleep);
        finishTitleView.setTitle(R.string.awoke);
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar.setLogo(ResourcesUtils.getSleepEventLogoRes(sex));
        getSupportActionBar().setTitle(R.string.event_sleep);
    }

    @Override
    public void showEventDetail(@NonNull SleepEvent event) {
    }

    @Override
    @LayoutRes
    protected int getContentLayoutResourceId() {
        return R.layout.activity_event_detail_sleep;
    }

    @Override
    protected void addEvent() {
        presenter.addSleepEvent();
    }

    @Override
    protected void updateEvent() {
    }
}
