package ru.android.healthvector.presentation.events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.List;

import butterknife.BindView;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.FeedType;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.calendar.data.core.MasterEvent;
import ru.android.healthvector.domain.calendar.data.standard.FeedEvent;
import ru.android.healthvector.domain.dictionaries.food.data.Food;
import ru.android.healthvector.domain.dictionaries.foodmeasure.data.FoodMeasure;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.core.fields.dialogs.TimeDialogArguments;
import ru.android.healthvector.presentation.core.fields.dialogs.TimeDialogFragment;
import ru.android.healthvector.presentation.core.fields.dialogs.add.food.AddFoodDialogArguments;
import ru.android.healthvector.presentation.core.fields.dialogs.add.food.AddFoodDialogFragment;
import ru.android.healthvector.presentation.core.fields.dialogs.add.foodmeasure.AddFoodMeasureDialogArguments;
import ru.android.healthvector.presentation.core.fields.dialogs.add.foodmeasure.AddFoodMeasureDialogFragment;
import ru.android.healthvector.presentation.core.fields.widgets.FieldAmountMlView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldAmountView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldBreastFeedDurationView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldBreastView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldDateView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldFeedTypeView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldFoodMeasureView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldFoodView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldNoteView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldNotifyTimeView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldSpinnerView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldTimeView;
import ru.android.healthvector.presentation.events.core.EventDetailActivity;
import ru.android.healthvector.utils.ui.ResourcesUtils;
import ru.android.healthvector.utils.ui.WidgetsUtils;

public class FeedEventDetailActivity extends EventDetailActivity<FeedEventDetailView, FeedEvent>
        implements FeedEventDetailView, FieldSpinnerView.FieldSpinnerListener,
        AddFoodMeasureDialogFragment.Listener, AddFoodDialogFragment.Listener {
    private static final String TAG_TIME_PICKER = "TIME_PICKER";
    private static final String TAG_DATE_PICKER = "DATE_PICKER";
    private static final String TAG_FOOD_MEASURE_DIALOG = "FOOD_MEASURE_DIALOG";
    private static final String TAG_FOOD_DIALOG = "FOOD_DIALOG";
    private static final String TAG_LEFT_DURATION_DIALOG = "TAG_LEFT_DURATION_DIALOG";
    private static final String TAG_RIGHT_DURATION_DIALOG = "TAG_RIGHT_DURATION_DIALOG";
    private static final String TAG_NOTIFY_TIME_DIALOG = "TAG_NOTIFY_TIME_DIALOG";

    @InjectPresenter
    FeedEventDetailPresenter presenter;

    @BindView(R.id.dateView)
    FieldDateView dateView;

    @BindView(R.id.timeView)
    FieldTimeView timeView;

    @BindView(R.id.feedTypeView)
    FieldFeedTypeView feedTypeView;

    @BindView(R.id.foodMeasureView)
    FieldFoodMeasureView foodMeasureView;

    @BindView(R.id.foodView)
    FieldFoodView foodView;

    @BindView(R.id.amountMlView)
    FieldAmountMlView amountMlView;

    @BindView(R.id.amountView)
    FieldAmountView amountView;

    @BindView(R.id.breastView)
    FieldBreastView breastView;

    @BindView(R.id.durationView)
    FieldBreastFeedDurationView durationView;

    @BindView(R.id.notifyTimeView)
    FieldNotifyTimeView notifyTimeView;

    @BindView(R.id.noteView)
    FieldNoteView noteView;

    public static Intent getIntent(Context context,
                                   @Nullable MasterEvent masterEvent,
                                   @NonNull FeedEvent defaultEvent) {
        return new Intent(context, FeedEventDetailActivity.class)
                .putExtra(ExtraConstants.EXTRA_MASTER_EVENT, masterEvent)
                .putExtra(ExtraConstants.EXTRA_DEFAULT_EVENT, defaultEvent);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dateView.setFieldDialogListener(v -> showDatePicker(TAG_DATE_PICKER, dateView.getValue(), null, null));
        timeView.setFieldDialogListener(v -> showTimePicker(TAG_TIME_PICKER, timeView.getValue()));
        //noinspection unchecked
        feedTypeView.setFieldSpinnerListener(this);
        //noinspection unchecked
        foodMeasureView.setFieldSpinnerListener(this);
        //noinspection unchecked
        foodView.setFieldSpinnerListener(this);
        setupEditTextView(amountMlView);
        setupEditTextView(amountView);
        durationView.setFieldDurationListener(new FieldBreastFeedDurationView.FieldDurationListener() {
            @Override
            public void requestLeftValueChange(FieldBreastFeedDurationView view) {
                TimeDialogFragment dialogFragment = new TimeDialogFragment();
                dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_LEFT_DURATION_DIALOG,
                        TimeDialogArguments.builder()
                                .sex(getSex())
                                .minutes(durationView.getDurationLeftInt())
                                .showDays(false)
                                .showHours(false)
                                .showMinutes(true)
                                .title(getString(R.string.breast_left))
                                .build());
            }

            @Override
            public void requestRightValueChange(FieldBreastFeedDurationView view) {
                TimeDialogFragment dialogFragment = new TimeDialogFragment();
                dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_RIGHT_DURATION_DIALOG,
                        TimeDialogArguments.builder()
                                .sex(getSex())
                                .minutes(durationView.getDurationRightInt())
                                .showDays(false)
                                .showHours(false)
                                .showMinutes(true)
                                .title(getString(R.string.breast_right))
                                .build());
            }
        });
        notifyTimeView.setFieldDialogListener(v -> {
            TimeDialogFragment dialogFragment = new TimeDialogFragment();
            dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_NOTIFY_TIME_DIALOG,
                    TimeDialogArguments.builder()
                            .sex(getSex())
                            .minutes(notifyTimeView.getValueInt())
                            .showDays(true)
                            .showHours(true)
                            .showMinutes(true)
                            .title(getString(R.string.notify_time_dialog_title))
                            .build());
        });
        setupEditTextView(noteView);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.event_feed);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        breastView.setSex(getSex());
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
    public void setupEventDetail(@NonNull FeedEvent event) {
        WidgetsUtils.setDateTime(event.getDateTime(), dateView, timeView);
        feedTypeView.setValue(event.getFeedType());
        setupFeedType();
        foodMeasureView.setValue(event.getFoodMeasure());
        foodView.setValue(event.getFood());
        amountMlView.setAmountMl(event.getAmountMl());
        amountView.setAmount(event.getAmount());
        breastView.setSelected(event.getBreast());
        durationView.setLeftDuration(event.getLeftDurationInMinutes());
        durationView.setRightDuration(event.getRightDurationInMinutes());
        notifyTimeView.setValue(event.getNotifyTimeInMinutes());
        notifyTimeView.setVisibility(notifyTimeViewVisible() ? View.VISIBLE : View.GONE);
        noteView.setText(event.getNote());
    }

    @Override
    public void showFoodMeasureList(@NonNull List<FoodMeasure> foodMeasureList) {
        foodMeasureView.updateAdapter(foodMeasureList);
    }

    @Override
    public void showFoodList(@NonNull List<Food> foodList) {
        foodView.updateAdapter(foodList);
    }

    @Override
    protected FeedEvent buildEvent(FeedEvent event) {
        FeedEvent.FeedEventBuilder builder = event == null
                ? FeedEvent.builder()
                : event.toBuilder();

        DateTime dateTime = WidgetsUtils.getDateTime(dateView, timeView);

        builder.dateTime(dateTime)
                .feedType(feedTypeView.getValue())
                .foodMeasure(foodMeasureView.getValue())
                .food(foodView.getValue())
                .amountMl(amountMlView.getAmountMl())
                .amount(amountView.getAmount())
                .breast(breastView.getSelected())
                .leftDurationInMinutes(durationView.getDurationLeft())
                .rightDurationInMinutes(durationView.getDurationRight())
                .notifyTimeInMinutes(notifyTimeView.getValue())
                .note(noteView.getText());

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
    public void onSpinnerItemClick(FieldSpinnerView view, Object item) {
        if (view == feedTypeView) {
            FeedType feedType = (FeedType) item;
            feedTypeView.setValue(feedType);
            setupFeedType();
        } else if (view == foodMeasureView) {
            FoodMeasure foodMeasure = (FoodMeasure) item;
            if (foodMeasure == FoodMeasure.NULL) {
                AddFoodMeasureDialogFragment dialogFragment = new AddFoodMeasureDialogFragment();
                dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_FOOD_MEASURE_DIALOG,
                        AddFoodMeasureDialogArguments.builder()
                                .sex(getSex())
                                .build());
            } else {
                foodMeasureView.setValue(foodMeasure);
            }
        } else if (view == foodView) {
            Food food = (Food) item;
            if (food == Food.NULL) {
                AddFoodDialogFragment dialogFragment = new AddFoodDialogFragment();
                dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_FOOD_DIALOG,
                        AddFoodDialogArguments.builder()
                                .sex(getSex())
                                .build());
            } else {
                foodView.setValue(food);
            }
        }
    }

    @Override
    public void onSetFoodMeasure(String tag, @NonNull FoodMeasure foodMeasure) {
        foodMeasureView.setValue(foodMeasure);
    }

    @Override
    public void onSetFood(String tag, @NonNull Food food) {
        foodView.setValue(food);
    }

    public void setupFeedType() {
        FeedType feedType = feedTypeView.getValue();
        setupToolbarLogo(ResourcesUtils.getFeedEventLogoRes(getSex(), feedType));
        switch (feedType) {
            case BREAST_MILK:
                breastView.setVisibility(View.VISIBLE);
                amountMlView.setVisibility(View.GONE);
                durationView.setVisibility(View.VISIBLE);
                amountView.setVisibility(View.GONE);
                foodMeasureView.setVisibility(View.GONE);
                foodView.setVisibility(View.GONE);
                break;
            case PUMPED_MILK:
                breastView.setVisibility(View.GONE);
                amountMlView.setVisibility(View.VISIBLE);
                durationView.setVisibility(View.GONE);
                amountView.setVisibility(View.GONE);
                foodMeasureView.setVisibility(View.GONE);
                foodView.setVisibility(View.GONE);
                break;
            case MILK_FORMULA:
                breastView.setVisibility(View.GONE);
                amountMlView.setVisibility(View.VISIBLE);
                durationView.setVisibility(View.GONE);
                amountView.setVisibility(View.GONE);
                foodMeasureView.setVisibility(View.GONE);
                foodView.setVisibility(View.GONE);
                break;
            case FOOD:
                breastView.setVisibility(View.GONE);
                amountMlView.setVisibility(View.GONE);
                durationView.setVisibility(View.GONE);
                amountView.setVisibility(View.VISIBLE);
                foodMeasureView.setVisibility(View.VISIBLE);
                foodView.setVisibility(View.VISIBLE);
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

        processed = foodView.dismissPopupWindow();
        if (processed) {
            return;
        }

        super.onBackPressed();
    }
}
