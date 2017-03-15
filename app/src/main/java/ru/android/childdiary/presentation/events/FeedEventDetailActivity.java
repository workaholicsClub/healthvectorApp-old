package ru.android.childdiary.presentation.events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.List;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Breast;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.FoodMeasure;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
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

public class FeedEventDetailActivity extends EventDetailActivity<FeedEventDetailView, FeedEvent> implements FeedEventDetailView,
        EventDetailFeedTypeView.OnValueChanged {
    private static final String TAG_TIME_PICKER = "TIME_PICKER";
    private static final String TAG_DATE_PICKER = "DATE_PICKER";

    @InjectPresenter
    FeedEventDetailPresenter presenter;

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

    public static Intent getIntent(Context context, @Nullable MasterEvent masterEvent) {
        Intent intent = new Intent(context, FeedEventDetailActivity.class);
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
        feedTypeView.setOnValueChanged(this);
        feedTypeView.setValue(FeedType.BREAST_MILK);
        breastView.setSelected(Breast.LEFT);
        // TODO amountMl
        // TODO duration
        // TODO amount
        // TODO foodMeasure
        // TODO not time

        dateView.setOnDateClickListener(() -> showDatePicker(TAG_DATE_PICKER, dateView.getDate()));
        timeView.setOnTimeClickListener(() -> showTimePicker(TAG_TIME_PICKER, timeView.getTime()));
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar.setLogo(ResourcesUtils.getFeedEventLogoRes(sex));
        getSupportActionBar().setTitle(R.string.event_feed);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        toolbar.setLogo(ResourcesUtils.getFeedEventLogoRes(sex));
        breastView.setSex(sex);
    }

    @Override
    public FeedEventDetailPresenter getPresenter() {
        return presenter;
    }

    @Override
    @LayoutRes
    protected int getContentLayoutResourceId() {
        return R.layout.activity_event_detail_feed;
    }

    @Override
    public void showDate(@NonNull LocalDate date) {
        setDateTime(date.toDateTime(LocalTime.now()), dateView, timeView);
    }

    @Override
    public void showEventDetail(@NonNull FeedEvent event) {
        super.showEventDetail(event);
        setDateTime(event.getDateTime(), dateView, timeView);
        feedTypeView.setValue(event.getFeedType());
        breastView.setSelected(event.getBreast());
        // TODO amountMl
        // TODO duration
        // TODO amount
        // TODO foodMeasure
        // TODO not time
        editTextNote.setText(event.getNote());
    }

    @Override
    protected FeedEvent buildEvent(FeedEvent event) {
        FeedEvent.FeedEventBuilder builder = event == null
                ? FeedEvent.builder().eventType(EventType.FEED)
                : event.toBuilder();

        DateTime dateTime = getDateTime(dateView, timeView);
        builder.dateTime(dateTime);

        builder.feedType(feedTypeView.getValue());

        builder.breast(breastView.getSelected());

        // TODO amountMl
        // TODO duration
        // TODO amount
        // TODO foodMeasure
        // TODO not time

        builder.note(editTextNote.getText().toString());

        return builder.build();
    }

    @Override
    public void onValueChanged() {
        FeedType feedType = feedTypeView.getValue();
        switch (feedType) {
            case BREAST_MILK:
                breastView.setVisibility(View.VISIBLE);
                amountMlView.setVisibility(View.GONE);
                durationView.setVisibility(View.VISIBLE);
                amountView.setVisibility(View.GONE);
                foodMeasureView.setVisibility(View.GONE);
                break;
            case PUMPED_MILK:
                breastView.setVisibility(View.GONE);
                amountMlView.setVisibility(View.VISIBLE);
                durationView.setVisibility(View.GONE);
                amountView.setVisibility(View.GONE);
                foodMeasureView.setVisibility(View.GONE);
                break;
            case MILK_FORMULA:
                breastView.setVisibility(View.GONE);
                amountMlView.setVisibility(View.VISIBLE);
                durationView.setVisibility(View.GONE);
                amountView.setVisibility(View.GONE);
                foodMeasureView.setVisibility(View.GONE);
                break;
            case FOOD:
                breastView.setVisibility(View.GONE);
                amountMlView.setVisibility(View.GONE);
                durationView.setVisibility(View.GONE);
                amountView.setVisibility(View.VISIBLE);
                foodMeasureView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void setDate(String tag, LocalDate date) {
        dateView.setDate(date);
    }

    @Override
    protected void setTime(String tag, LocalTime time) {
        timeView.setTime(time);
    }

    @Override
    public void showFoodMeasureList(@NonNull List<FoodMeasure> foodMeasureList) {
        foodMeasureView.updateAdapter(foodMeasureList);
    }

    @Override
    public void onBackPressed() {
        boolean processed = feedTypeView.dismissPopupWindow();
        if (processed) {
            return;
        }

        processed = foodMeasureView.dismissPopupWindow();
        if (processed) {
            return;
        }

        super.onBackPressed();
    }
}
