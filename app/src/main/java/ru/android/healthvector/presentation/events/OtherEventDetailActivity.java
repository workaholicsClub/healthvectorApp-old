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
import icepick.State;
import ru.android.healthvector.R;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.calendar.data.core.MasterEvent;
import ru.android.healthvector.domain.calendar.data.standard.OtherEvent;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.core.fields.dialogs.TimeDialogArguments;
import ru.android.healthvector.presentation.core.fields.dialogs.TimeDialogFragment;
import ru.android.healthvector.presentation.core.fields.widgets.FieldCheckBoxView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldDateView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldEditTextWithImageView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldNoteView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldNotifyTimeView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldTimeView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldTitleView;
import ru.android.healthvector.presentation.events.core.EventDetailActivity;
import ru.android.healthvector.utils.ui.ResourcesUtils;
import ru.android.healthvector.utils.ui.WidgetsUtils;

public class OtherEventDetailActivity extends EventDetailActivity<OtherEventDetailView, OtherEvent>
        implements OtherEventDetailView, FieldCheckBoxView.FieldCheckBoxListener {
    private static final String TAG_TIME_PICKER_START = "TIME_PICKER_START";
    private static final String TAG_DATE_PICKER_START = "DATE_PICKER_START";
    private static final String TAG_TIME_PICKER_FINISH = "TIME_PICKER_FINISH";
    private static final String TAG_DATE_PICKER_FINISH = "DATE_PICKER_FINISH";
    private static final String TAG_NOTIFY_TIME_DIALOG = "TAG_NOTIFY_TIME_DIALOG";

    @InjectPresenter
    OtherEventDetailPresenter presenter;

    @BindView(R.id.otherEventNameView)
    FieldEditTextWithImageView otherEventNameView;

    @BindView(R.id.startTitleView)
    FieldTitleView startTitleView;

    @BindView(R.id.startDateView)
    FieldDateView startDateView;

    @BindView(R.id.startTimeView)
    FieldTimeView startTimeView;

    @BindView(R.id.finishTitleView)
    FieldTitleView finishTitleView;

    @BindView(R.id.finishDateView)
    FieldDateView finishDateView;

    @BindView(R.id.finishTimeView)
    FieldTimeView finishTimeView;

    @BindView(R.id.notifyTimeView)
    FieldNotifyTimeView notifyTimeView;

    @BindView(R.id.noteView)
    FieldNoteView noteView;

    @BindView(R.id.checkBoxView)
    FieldCheckBoxView checkBoxView;

    @State
    boolean isButtonDoneEnabled;

    private boolean isValidationStarted;

    public static Intent getIntent(Context context,
                                   @Nullable MasterEvent masterEvent,
                                   @NonNull OtherEvent defaultEvent) {
        return new Intent(context, OtherEventDetailActivity.class)
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

        setupEditTextView(otherEventNameView);
        startTitleView.setTitle(R.string.other_event_start);
        finishTitleView.setTitle(R.string.other_event_finish);

        startDateView.setFieldDialogListener(v -> showDatePicker(TAG_DATE_PICKER_START, startDateView.getValue(),
                null, finishDateView.getValue()));
        startTimeView.setFieldDialogListener(v -> showTimePicker(TAG_TIME_PICKER_START, startTimeView.getValue()));
        finishDateView.setFieldDialogListener(v -> showDatePicker(TAG_DATE_PICKER_FINISH, finishDateView.getValue(),
                startDateView.getValue(), null));
        finishTimeView.setFieldDialogListener(v -> showTimePicker(TAG_TIME_PICKER_FINISH, finishTimeView.getValue()));
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

        unsubscribeOnDestroy(presenter.listenForDoneButtonUpdate(otherEventNameView.textObservable()));
    }

    @Override
    protected void setupNewEventUi() {
        super.setupNewEventUi();
        checkBoxView.setVisibility(View.GONE);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarLogo(ResourcesUtils.getOtherEventLogoRes(getSex()));
        setupToolbarTitle(R.string.other);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        setupToolbarLogo(ResourcesUtils.getOtherEventLogoRes(getSex()));
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), isButtonDoneEnabled));
    }

    @Override
    public OtherEventDetailPresenter getPresenter() {
        return presenter;
    }

    @Override
    @LayoutRes
    protected int getContentLayoutResourceId() {
        return R.layout.activity_event_detail_other;
    }

    @Override
    public void setupEventDetail(@NonNull OtherEvent event) {
        otherEventNameView.setText(event.getName());
        WidgetsUtils.setDateTime(event.getDateTime(), startDateView, startTimeView);
        WidgetsUtils.setDateTime(event.getFinishDateTime(), finishDateView, finishTimeView);
        notifyTimeView.setValue(event.getNotifyTimeInMinutes());
        notifyTimeView.setVisibility(notifyTimeViewVisible() ? View.VISIBLE : View.GONE);
        noteView.setText(event.getNote());
        checkBoxView.setValue(event.getIsDone());
    }

    @Override
    protected OtherEvent buildEvent(OtherEvent event) {
        OtherEvent.OtherEventBuilder builder = event == null
                ? OtherEvent.builder()
                : event.toBuilder();

        DateTime startDateTime = WidgetsUtils.getDateTime(startDateView, startTimeView);
        DateTime finishDateTime = WidgetsUtils.getDateTime(finishDateView, finishTimeView);
        if (finishDateTime == null) {
            LocalDate finishDate = finishDateView.getValue();
            LocalTime finishTime = finishTimeView.getValue();
            if (finishDate == null && finishTime != null && startDateTime != null) {
                finishDateTime = startDateTime.withTime(finishTime);
                if (finishDateTime.isBefore(startDateTime)) {
                    finishDateTime = finishDateTime.plusDays(1);
                }
            } else if (finishDate != null && finishTime == null && startDateTime != null) {
                finishDateTime = finishDate.toDateTime(startDateTime.toLocalTime());
            }
        }

        builder.name(otherEventNameView.getText())
                .dateTime(startDateTime)
                .finishDateTime(finishDateTime)
                .notifyTimeInMinutes(notifyTimeView.getValue())
                .note(noteView.getText())
                .isDone(checkBoxView.getValue());

        return builder.build();
    }

    @Override
    public void eventDone(boolean done) {
        super.eventDone(done);
        checkBoxView.setValue(done);
    }

    @Override
    public void onChecked() {
        getPresenter().done(buildEvent());
    }

    @Override
    public void setButtonDoneEnabled(boolean enabled) {
        isButtonDoneEnabled = enabled;
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), isButtonDoneEnabled));
    }

    @Override
    public void validationFailed() {
        if (!isValidationStarted) {
            isValidationStarted = true;
            unsubscribeOnDestroy(presenter.listenForFieldsUpdate(otherEventNameView.textObservable()));
        }
    }

    @Override
    public void otherEventNameValidated(boolean valid) {
        otherEventNameView.validated(valid);
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
