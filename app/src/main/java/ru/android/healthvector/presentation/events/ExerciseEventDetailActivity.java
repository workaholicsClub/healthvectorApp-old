package ru.android.healthvector.presentation.events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import butterknife.BindView;
import ru.android.healthvector.R;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.calendar.data.ExerciseEvent;
import ru.android.healthvector.domain.calendar.data.core.MasterEvent;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.core.fields.dialogs.TimeDialogArguments;
import ru.android.healthvector.presentation.core.fields.dialogs.TimeDialogFragment;
import ru.android.healthvector.presentation.core.fields.widgets.FieldCheckBoxView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldDateView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldDurationView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldEditTextWithImageView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldNoteWithPhotoView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldNotifyTimeView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldTimeView;
import ru.android.healthvector.presentation.core.images.ImagePickerDialogArguments;
import ru.android.healthvector.presentation.core.images.ImagePickerDialogFragment;
import ru.android.healthvector.presentation.core.images.review.ImageReviewActivity;
import ru.android.healthvector.presentation.events.core.PeriodicEventDetailActivity;
import ru.android.healthvector.utils.strings.TimeUtils;
import ru.android.healthvector.utils.ui.ResourcesUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;
import ru.android.healthvector.utils.ui.WidgetsUtils;

public class ExerciseEventDetailActivity
        extends PeriodicEventDetailActivity<ExerciseEventDetailView, ExerciseEvent>
        implements ExerciseEventDetailView, FieldNoteWithPhotoView.PhotoListener,
        ImagePickerDialogFragment.Listener, FieldCheckBoxView.FieldCheckBoxListener {
    private static final String TAG_TIME_PICKER = "TIME_PICKER";
    private static final String TAG_DATE_PICKER = "DATE_PICKER";
    private static final String TAG_IMAGE_PICKER = "IMAGE_PICKER";
    private static final String TAG_DURATION_DIALOG = "TAG_DURATION_DIALOG";
    private static final String TAG_NOTIFY_TIME_DIALOG = "TAG_NOTIFY_TIME_DIALOG";

    private static final int REQUEST_IMAGE_REVIEW = 1;

    @InjectPresenter
    ExerciseEventDetailPresenter presenter;

    @BindView(R.id.exerciseNameView)
    FieldEditTextWithImageView exerciseNameView;

    @BindView(R.id.dateView)
    FieldDateView dateView;

    @BindView(R.id.timeView)
    FieldTimeView timeView;

    @BindView(R.id.durationView)
    FieldDurationView durationView;

    @BindView(R.id.noteWithPhotoView)
    FieldNoteWithPhotoView noteWithPhotoView;

    @BindView(R.id.notifyTimeView)
    FieldNotifyTimeView notifyTimeView;

    @BindView(R.id.checkBoxView)
    FieldCheckBoxView checkBoxView;

    private boolean isValidationStarted;

    public static Intent getIntent(Context context,
                                   @Nullable MasterEvent masterEvent,
                                   @NonNull ExerciseEvent defaultEvent,
                                   boolean isLinearGroupFinished) {
        return new Intent(context, ExerciseEventDetailActivity.class)
                .putExtra(ExtraConstants.EXTRA_MASTER_EVENT, masterEvent)
                .putExtra(ExtraConstants.EXTRA_DEFAULT_EVENT, defaultEvent)
                .putExtra(ExtraConstants.EXTRA_LINEAR_GROUP_FINISHED_EVENT, isLinearGroupFinished ? masterEvent : null);
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
        setupEditTextView(exerciseNameView);
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
        durationView.setFieldDialogListener(v -> {
            TimeDialogFragment dialogFragment = new TimeDialogFragment();
            dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_DURATION_DIALOG,
                    TimeDialogArguments.builder()
                            .sex(getSex())
                            .minutes(durationView.getValueInt())
                            .showDays(durationView.getValueInt() >= TimeUtils.MINUTES_IN_DAY)
                            .showHours(true)
                            .showMinutes(true)
                            .title(getString(R.string.duration))
                            .build());
        });
        setupEditTextView(noteWithPhotoView);
        noteWithPhotoView.setPhotoListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_REVIEW) {
            if (resultCode == RESULT_OK) {
                noteWithPhotoView.setImageFileName(null);
            }
        }
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarLogo(ResourcesUtils.getExerciseLogoRes(getSex()));
        setupToolbarTitle(R.string.exercise);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        setupToolbarLogo(ResourcesUtils.getExerciseLogoRes(getSex()));
        noteWithPhotoView.setSex(getSex());
    }

    @Override
    public ExerciseEventDetailPresenter getPresenter() {
        return presenter;
    }

    @Override
    @LayoutRes
    protected int getContentLayoutResourceId() {
        return R.layout.activity_event_detail_exercise;
    }

    @Override
    public void setupEventDetail(@NonNull ExerciseEvent event) {
        exerciseNameView.setText(event.getName());
        durationView.setValue(event.getDurationInMinutes());
        WidgetsUtils.setDateTime(event.getDateTime(), dateView, timeView);
        notifyTimeView.setValue(event.getNotifyTimeInMinutes());
        notifyTimeView.setVisibility(notifyTimeViewVisible() ? View.VISIBLE : View.GONE);
        noteWithPhotoView.setText(event.getNote());
        noteWithPhotoView.setImageFileName(event.getImageFileName());
        checkBoxView.setValue(event.getIsDone());
    }

    @Override
    protected ExerciseEvent buildEvent(ExerciseEvent event) {
        ExerciseEvent.ExerciseEventBuilder builder = event == null
                ? ExerciseEvent.builder()
                : event.toBuilder();

        String exerciseName = exerciseNameView.getText();
        Integer duration = durationView.getValue();
        DateTime dateTime = WidgetsUtils.getDateTime(dateView, timeView);
        Integer minutes = notifyTimeView.getValue();
        String note = noteWithPhotoView.getText();
        String imageFileName = noteWithPhotoView.getImageFileName();

        builder.dateTime(dateTime)
                .name(exerciseName)
                .durationInMinutes(duration)
                .dateTime(dateTime)
                .notifyTimeInMinutes(minutes)
                .note(note)
                .imageFileName(imageFileName)
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
    public void validationFailed() {
        if (!isValidationStarted) {
            isValidationStarted = true;
            unsubscribeOnDestroy(presenter.listenForFieldsUpdate(exerciseNameView.textObservable()));
        }
    }

    @Override
    public void exerciseEventNameValidated(boolean valid) {
        exerciseNameView.validated(valid);
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
            case TAG_DURATION_DIALOG:
                durationView.setValue(minutes);
                break;
            case TAG_NOTIFY_TIME_DIALOG:
                notifyTimeView.setValue(minutes);
                break;
        }
    }

    @Override
    public void requestPhotoAdd() {
        ImagePickerDialogFragment dialogFragment = new ImagePickerDialogFragment();
        dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_IMAGE_PICKER,
                ImagePickerDialogArguments.builder()
                        .sex(getSex())
                        .showDeleteItem(false)
                        .showCircleFrame(false)
                        .build());
    }

    @Override
    public void requestPhotoReview() {
        Intent intent = ImageReviewActivity.getIntent(this, noteWithPhotoView.getImageFileName(), getSex(), false);
        startActivityForResult(intent, REQUEST_IMAGE_REVIEW);
    }

    @Override
    public void requestPhotoDelete() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.delete_photo_confirmation_dialog_title)
                .setPositiveButton(R.string.delete,
                        (dialog, which) -> noteWithPhotoView.setImageFileName(null))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onSetImage(@Nullable String relativeFileName) {
        noteWithPhotoView.setImageFileName(relativeFileName);
    }
}
