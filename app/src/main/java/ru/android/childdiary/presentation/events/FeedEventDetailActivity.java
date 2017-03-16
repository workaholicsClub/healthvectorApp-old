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
import ru.android.childdiary.presentation.events.dialogs.TimeDialog;
import ru.android.childdiary.presentation.events.widgets.EventDetailAmountMlView;
import ru.android.childdiary.presentation.events.widgets.EventDetailAmountView;
import ru.android.childdiary.presentation.events.widgets.EventDetailBreastView;
import ru.android.childdiary.presentation.events.widgets.EventDetailDateView;
import ru.android.childdiary.presentation.events.widgets.EventDetailDurationView;
import ru.android.childdiary.presentation.events.widgets.EventDetailFeedTypeView;
import ru.android.childdiary.presentation.events.widgets.EventDetailFoodMeasureView;
import ru.android.childdiary.presentation.events.widgets.EventDetailNotifyTimeView;
import ru.android.childdiary.presentation.events.widgets.EventDetailSpinnerView;
import ru.android.childdiary.presentation.events.widgets.EventDetailTimeView;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class FeedEventDetailActivity extends EventDetailActivity<FeedEventDetailView, FeedEvent> implements FeedEventDetailView,
        EventDetailSpinnerView.EventDetailSpinnerListener {
    private static final String TAG_TIME_PICKER = "TIME_PICKER";
    private static final String TAG_DATE_PICKER = "DATE_PICKER";
    private static final String TAG_FOOD_MEASURE_DIALOG = "FOOD_MEASURE_DIALOG";
    private static final String TAG_LEFT_DURATION_DIALOG = "TAG_LEFT_DURATION_DIALOG";
    private static final String TAG_RIGHT_DURATION_DIALOG = "TAG_RIGHT_DURATION_DIALOG";
    private static final String TAG_NOTIFY_TIME_DIALOG = "TAG_NOTIFY_TIME_DIALOG";

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

    @BindView(R.id.notifyTimeView)
    EventDetailNotifyTimeView notifyTimeView;

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
        setupEditTextView(amountView);
        setupEditTextView(amountMlView);

        setDateTime(DateTime.now(), dateView, timeView);
        feedTypeView.setEventDetailSpinnerListener(this);
        feedTypeView.setValue(FeedType.BREAST_MILK);
        breastView.setSelected(Breast.LEFT);
        foodMeasureView.setEventDetailSpinnerListener(this);

        dateView.setEventDetailDialogListener(v -> showDatePicker(TAG_DATE_PICKER, dateView.getValue()));
        timeView.setEventDetailDialogListener(v -> showTimePicker(TAG_TIME_PICKER, timeView.getValue()));
        durationView.setEventDetailDurationListener(new EventDetailDurationView.EventDetailDurationListener() {
            @Override
            public void requestLeftValueChange(EventDetailDurationView view) {
                presenter.requestTimeDialog(TAG_LEFT_DURATION_DIALOG,
                        TimeDialog.Parameters.builder()
                                .minutes(durationView.getDurationLeftInt())
                                .showDays(false)
                                .showHours(false)
                                .showMinutes(true)
                                .title(getString(R.string.duration_left))
                                .build());
            }

            @Override
            public void requestRightValueChange(EventDetailDurationView view) {
                presenter.requestTimeDialog(TAG_RIGHT_DURATION_DIALOG,
                        TimeDialog.Parameters.builder()
                                .minutes(durationView.getDurationRightInt())
                                .showDays(false)
                                .showHours(false)
                                .showMinutes(true)
                                .title(getString(R.string.duration_right))
                                .build());
            }
        });
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
    protected EventType getEventType() {
        return EventType.FEED;
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
        amountMlView.setAmountMl(event.getAmountMl());
        durationView.setLeftDuration(event.getLeftDurationInMinutes());
        durationView.setRightDuration(event.getRightDurationInMinutes());
        amountView.setAmount(event.getAmount());
        foodMeasureView.setValue(event.getFoodMeasure());
        notifyTimeView.setValue(event.getNotifyTimeInMinutes());
        editTextNote.setText(event.getNote());
    }

    @Override
    public void showDefaultNotifyTime(int minutes) {
        notifyTimeView.setValue(minutes);
    }

    @Override
    public void showDefaultFoodMeasure(@NonNull FoodMeasure foodMeasure) {
        foodMeasureView.setValue(foodMeasure);
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

        builder.amountMl(amountMlView.getAmountMl());

        builder.leftDurationInMinutes(durationView.getDurationLeft());

        builder.rightDurationInMinutes(durationView.getDurationRight());

        builder.amount(amountView.getAmount());

        builder.foodMeasure(foodMeasureView.getValue());

        builder.notifyTimeInMinutes(notifyTimeView.getValue());

        builder.note(editTextNote.getText().toString());

        return builder.build();
    }

    @Override
    public void onValueChanged(EventDetailSpinnerView view) {
        if (view == feedTypeView) {
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
    }

    @Override
    public void onSpinnerItemClick(EventDetailSpinnerView view, Object item) {
        if (view == feedTypeView) {
            FeedType feedType = (FeedType) item;
            feedTypeView.setValue(feedType);
        } else if (view == foodMeasureView) {
            FoodMeasure foodMeasure = (FoodMeasure) item;
            if (foodMeasure == FoodMeasure.NULL) {
                presenter.requestFoodMeasureDialog(TAG_FOOD_MEASURE_DIALOG);
            } else {
                foodMeasureView.setValue(foodMeasure);
            }
        }
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
    public void showFoodMeasureList(@NonNull List<FoodMeasure> foodMeasureList) {
        foodMeasureView.updateAdapter(foodMeasureList);
    }

    @Override
    public void onSetFoodMeasure(String tag, @NonNull FoodMeasure foodMeasure) {
        presenter.addFoodMeasure(foodMeasure);
        foodMeasureView.setValue(foodMeasure);
    }

    @Override
    public void onSetTime(String tag, int minutes) {
        switch (tag) {
            case TAG_LEFT_DURATION_DIALOG:
                durationView.setLeftDuration(minutes);
                break;
            case TAG_RIGHT_DURATION_DIALOG:
                durationView.setRightDuration(minutes);
                break;
            case TAG_NOTIFY_TIME_DIALOG:
                notifyTimeView.setValue(minutes);
                break;
        }
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
