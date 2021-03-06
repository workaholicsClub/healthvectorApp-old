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

import butterknife.BindView;
import ru.android.healthvector.R;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.calendar.data.core.MasterEvent;
import ru.android.healthvector.domain.calendar.data.standard.PumpEvent;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.core.fields.dialogs.TimeDialogArguments;
import ru.android.healthvector.presentation.core.fields.dialogs.TimeDialogFragment;
import ru.android.healthvector.presentation.core.fields.widgets.FieldAmountMlPumpView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldBreastView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldDateView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldNoteView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldNotifyTimeView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldTimeView;
import ru.android.healthvector.presentation.events.core.EventDetailActivity;
import ru.android.healthvector.presentation.events.core.EventDetailView;
import ru.android.healthvector.utils.ui.ResourcesUtils;
import ru.android.healthvector.utils.ui.WidgetsUtils;

public class PumpEventDetailActivity extends EventDetailActivity<EventDetailView<PumpEvent>, PumpEvent>
        implements EventDetailView<PumpEvent> {
    private static final String TAG_TIME_PICKER = "TIME_PICKER";
    private static final String TAG_DATE_PICKER = "DATE_PICKER";
    private static final String TAG_NOTIFY_TIME_DIALOG = "TAG_NOTIFY_TIME_DIALOG";

    @InjectPresenter
    PumpEventDetailPresenter presenter;

    @BindView(R.id.dateView)
    FieldDateView dateView;

    @BindView(R.id.timeView)
    FieldTimeView timeView;

    @BindView(R.id.breastView)
    FieldBreastView breastView;

    @BindView(R.id.amountMlPumpView)
    FieldAmountMlPumpView amountMlPumpView;

    @BindView(R.id.notifyTimeView)
    FieldNotifyTimeView notifyTimeView;

    @BindView(R.id.noteView)
    FieldNoteView noteView;

    public static Intent getIntent(Context context,
                                   @Nullable MasterEvent masterEvent,
                                   @NonNull PumpEvent defaultEvent) {
        return new Intent(context, PumpEventDetailActivity.class)
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
        setupEditTextView(amountMlPumpView);
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
        setupToolbarLogo(ResourcesUtils.getPumpEventLogoRes(getSex()));
        setupToolbarTitle(R.string.event_pump);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        setupToolbarLogo(ResourcesUtils.getPumpEventLogoRes(getSex()));
        breastView.setSex(getSex());
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
    public void setupEventDetail(@NonNull PumpEvent event) {
        WidgetsUtils.setDateTime(event.getDateTime(), dateView, timeView);
        breastView.setSelected(event.getBreast());
        amountMlPumpView.setAmountMlLeft(event.getLeftAmountMl());
        amountMlPumpView.setAmountMlRight(event.getRightAmountMl());
        notifyTimeView.setValue(event.getNotifyTimeInMinutes());
        notifyTimeView.setVisibility(notifyTimeViewVisible() ? View.VISIBLE : View.GONE);
        noteView.setText(event.getNote());
    }

    @Override
    protected PumpEvent buildEvent(PumpEvent event) {
        PumpEvent.PumpEventBuilder builder = event == null
                ? PumpEvent.builder()
                : event.toBuilder();

        DateTime dateTime = WidgetsUtils.getDateTime(dateView, timeView);

        builder.dateTime(dateTime)
                .breast(breastView.getSelected())
                .leftAmountMl(amountMlPumpView.getAmountMlLeft())
                .rightAmountMl(amountMlPumpView.getAmountMlRight())
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
        notifyTimeView.setValue(minutes);
    }
}
