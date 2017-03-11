package ru.android.childdiary.presentation.events;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.events.core.EventDetailActivity;
import ru.android.childdiary.presentation.events.widgets.EventDetailAmountMlView;
import ru.android.childdiary.presentation.events.widgets.EventDetailAmountView;
import ru.android.childdiary.presentation.events.widgets.EventDetailBreastView;
import ru.android.childdiary.presentation.events.widgets.EventDetailDateView;
import ru.android.childdiary.presentation.events.widgets.EventDetailDurationView;
import ru.android.childdiary.presentation.events.widgets.EventDetailFeedTypeView;
import ru.android.childdiary.presentation.events.widgets.EventDetailFoodMeasureView;
import ru.android.childdiary.presentation.events.widgets.EventDetailNotificationTimeView;
import ru.android.childdiary.presentation.events.widgets.EventDetailTimeView;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class FeedEventDetailActivity extends EventDetailActivity<FeedEvent> {
    @BindView(R.id.feedTypeView)
    EventDetailFeedTypeView feedTypeView;

    @BindView(R.id.dateView)
    EventDetailDateView dateView;

    @BindView(R.id.timeView)
    EventDetailTimeView timeView;

    @BindView(R.id.amountMlView)
    EventDetailAmountMlView amountMlView;

    @BindView(R.id.breastView)
    EventDetailBreastView breastView;

    @BindView(R.id.durationView)
    EventDetailDurationView durationView;

    @BindView(R.id.amountView)
    EventDetailAmountView amountView;

    @BindView(R.id.foodMeasureView)
    EventDetailFoodMeasureView foodMeasureView;

    @BindView(R.id.notificationTimeView)
    EventDetailNotificationTimeView notificationTimeView;

    public static Intent getIntent(Context context, @Nullable Long masterEventId) {
        Intent intent = new Intent(context, FeedEventDetailActivity.class);
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
        toolbar.setLogo(ResourcesUtils.getFeedEventLogoRes(sex));
        getSupportActionBar().setTitle(R.string.event_feed);
    }

    @Override
    public void showEventDetail(@NonNull FeedEvent event) {
    }

    @Override
    @LayoutRes
    protected int getContentLayoutResourceId() {
        return R.layout.activity_event_detail_feed;
    }

    @Override
    protected void addEvent() {
        presenter.addFeedEvent();
    }

    @Override
    protected void updateEvent() {
    }
}
