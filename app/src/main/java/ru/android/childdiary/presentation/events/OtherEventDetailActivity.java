package ru.android.childdiary.presentation.events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.events.core.EventDetailActivity;
import ru.android.childdiary.presentation.events.core.EventDetailView;
import ru.android.childdiary.presentation.events.dialogs.TimeDialog;
import ru.android.childdiary.presentation.events.widgets.EventDetailDateView;
import ru.android.childdiary.presentation.events.widgets.EventDetailEditableTitleView;
import ru.android.childdiary.presentation.events.widgets.EventDetailNotifyTimeView;
import ru.android.childdiary.presentation.events.widgets.EventDetailTimeView;
import ru.android.childdiary.presentation.events.widgets.EventDetailTitleView;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class OtherEventDetailActivity extends EventDetailActivity<EventDetailView<OtherEvent>, OtherEvent> implements EventDetailView<OtherEvent> {
    private static final String TAG_TIME_PICKER_START = "TIME_PICKER_START";
    private static final String TAG_DATE_PICKER_START = "DATE_PICKER_START";
    private static final String TAG_TIME_PICKER_FINISH = "TIME_PICKER_FINISH";
    private static final String TAG_DATE_PICKER_FINISH = "DATE_PICKER_FINISH";
    private static final String TAG_NOTIFY_TIME_DIALOG = "TAG_NOTIFY_TIME_DIALOG";

    @InjectPresenter
    OtherEventDetailPresenter presenter;

    @BindView(R.id.editableTitleView)
    EventDetailEditableTitleView editableTitleView;

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

    @BindView(R.id.notifyTimeView)
    EventDetailNotifyTimeView notifyTimeView;

    public static Intent getIntent(Context context, @Nullable MasterEvent masterEvent, boolean readOnly) {
        Intent intent = new Intent(context, OtherEventDetailActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_MASTER_EVENT, masterEvent);
        intent.putExtra(ExtraConstants.EXTRA_READ_ONLY, readOnly);
        return intent;
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupEditTextView(editableTitleView);
        startTitleView.setTitle(R.string.other_event_start);
        finishTitleView.setTitle(R.string.other_event_finish);

        startDateView.setEventDetailDialogListener(v -> showDatePicker(TAG_DATE_PICKER_START, startDateView.getValue(),
                null, finishDateView.getValue()));
        startTimeView.setEventDetailDialogListener(v -> showTimePicker(TAG_TIME_PICKER_START, startTimeView.getValue(),
                null, getStartTimeMaxValue()));
        finishDateView.setEventDetailDialogListener(v -> showDatePicker(TAG_DATE_PICKER_FINISH, finishDateView.getValue(),
                startDateView.getValue(), null));
        finishTimeView.setEventDetailDialogListener(v -> showTimePicker(TAG_TIME_PICKER_FINISH, finishTimeView.getValue(),
                getFinishTimeMinValue(), null));
        notifyTimeView.setEventDetailDialogListener(v -> presenter.requestTimeDialog(TAG_NOTIFY_TIME_DIALOG,
                TimeDialog.Parameters.builder()
                        .minutes(notifyTimeView.getValueInt())
                        .showDays(true)
                        .showHours(true)
                        .showMinutes(true)
                        .title(getString(R.string.notify_time_dialog_title))
                        .build()));
    }

    private LocalTime getStartTimeMaxValue() {
        LocalDate startDate = startDateView.getValue();
        LocalDate finishDate = finishDateView.getValue();
        if (startDate.equals(finishDate)) {
            return finishTimeView.getValue();
        }
        return null;
    }

    private LocalTime getFinishTimeMinValue() {
        LocalDate startDate = startDateView.getValue();
        LocalDate finishDate = finishDateView.getValue();
        if (startDate.equals(finishDate)) {
            return startTimeView.getValue();
        }
        return null;
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarLogo(ResourcesUtils.getOtherEventLogoRes(sex));
        setupToolbarTitle(R.string.event_other);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        setupToolbarLogo(ResourcesUtils.getOtherEventLogoRes(sex));
    }

    @Override
    public OtherEventDetailPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected EventType getEventType() {
        return EventType.OTHER;
    }

    @Override
    @LayoutRes
    protected int getContentLayoutResourceId() {
        return R.layout.activity_event_detail_other;
    }

    @Override
    public void showEventDetail(@NonNull OtherEvent event) {
        super.showEventDetail(event);
        editableTitleView.setTitle(event.getTitle());
        setDateTime(event.getDateTime(), startDateView, startTimeView);
        setDateTime(event.getFinishDateTime(), finishDateView, finishTimeView);
        notifyTimeView.setValue(event.getNotifyTimeInMinutes());
        editTextNote.setText(event.getNote());
    }

    @Override
    protected OtherEvent buildEvent(OtherEvent event) {
        OtherEvent.OtherEventBuilder builder = event == null
                ? OtherEvent.builder().eventType(EventType.OTHER)
                : event.toBuilder();

        DateTime startDateTime = getDateTime(startDateView, startTimeView);
        DateTime finishDateTime = getDateTime(finishDateView, finishTimeView);

        builder
                .title(editableTitleView.getTitle())
                .dateTime(startDateTime)
                .finishDateTime(finishDateTime)
                .notifyTimeInMinutes(notifyTimeView.getValue())
                .note(editTextNote.getText().toString());

        return builder.build();
    }

    @Override
    protected void setDate(String tag, LocalDate date) {
        switch (tag) {
            case TAG_DATE_PICKER_START:
                startDateView.setValue(date);
                break;
            case TAG_DATE_PICKER_FINISH:
                finishDateView.setValue(date);
                break;
        }
    }

    @Override
    protected void setTime(String tag, LocalTime time) {
        switch (tag) {
            case TAG_TIME_PICKER_START:
                startTimeView.setValue(time);
                break;
            case TAG_TIME_PICKER_FINISH:
                finishTimeView.setValue(time);
                break;
        }
    }

    @Override
    public void onSetTime(String tag, int minutes) {
        notifyTimeView.setValue(minutes);
    }
}
