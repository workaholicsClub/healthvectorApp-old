package ru.android.childdiary.presentation.events;

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
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.dialogs.TimeDialogArguments;
import ru.android.childdiary.presentation.core.fields.dialogs.TimeDialogFragment;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDateView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDiaperStateView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldNoteView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldNotifyTimeView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldTimeView;
import ru.android.childdiary.presentation.events.core.EventDetailActivity;
import ru.android.childdiary.presentation.events.core.EventDetailView;
import ru.android.childdiary.utils.ObjectUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public class DiaperEventDetailActivity extends EventDetailActivity<EventDetailView<DiaperEvent>, DiaperEvent> implements EventDetailView<DiaperEvent> {
    private static final String TAG_TIME_PICKER = "TIME_PICKER";
    private static final String TAG_DATE_PICKER = "DATE_PICKER";
    private static final String TAG_NOTIFY_TIME_DIALOG = "TAG_NOTIFY_TIME_DIALOG";

    @InjectPresenter
    DiaperEventDetailPresenter presenter;

    @BindView(R.id.dateView)
    FieldDateView dateView;

    @BindView(R.id.timeView)
    FieldTimeView timeView;

    @BindView(R.id.diaperStateView)
    FieldDiaperStateView diaperStateView;

    @BindView(R.id.notifyTimeView)
    FieldNotifyTimeView notifyTimeView;

    @BindView(R.id.noteView)
    FieldNoteView noteView;

    public static Intent getIntent(Context context, @Nullable MasterEvent masterEvent,
                                   @NonNull DiaperEvent defaultEvent) {
        Intent intent = new Intent(context, DiaperEventDetailActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_MASTER_EVENT, masterEvent);
        intent.putExtra(ExtraConstants.EXTRA_DEFAULT_EVENT, defaultEvent);
        return intent;
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
        setupToolbarLogo(ResourcesUtils.getDiaperEventLogoRes(getSex()));
        setupToolbarTitle(R.string.event_diaper);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        setupToolbarLogo(ResourcesUtils.getDiaperEventLogoRes(getSex()));
        diaperStateView.setSex(getSex());
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
    public void setupEventDetail(@NonNull DiaperEvent event) {
        WidgetsUtils.setDateTime(event.getDateTime(), dateView, timeView);
        diaperStateView.setSelected(event.getDiaperState());
        notifyTimeView.setValue(event.getNotifyTimeInMinutes());
        notifyTimeView.setVisibility(notifyTimeViewVisible() ? View.VISIBLE : View.GONE);
        noteView.setText(event.getNote());
    }

    @Override
    protected DiaperEvent buildEvent(@Nullable DiaperEvent event) {
        DiaperEvent.DiaperEventBuilder builder = event == null
                ? DiaperEvent.builder()
                : event.toBuilder();

        DateTime dateTime = WidgetsUtils.getDateTime(dateView, timeView);

        builder.dateTime(dateTime)
                .diaperState(diaperStateView.getSelected())
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

    @Override
    protected boolean contentEquals(DiaperEvent event1, DiaperEvent event2) {
        return ObjectUtils.contentEquals(event1, event2);
    }
}
