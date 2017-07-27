package ru.android.childdiary.presentation.events;

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
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.DoctorVisitEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.dialogs.TimeDialogArguments;
import ru.android.childdiary.presentation.core.fields.dialogs.TimeDialogFragment;
import ru.android.childdiary.presentation.core.fields.widgets.FieldCheckBoxView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDateView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDoctorView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDurationView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldEditTextWithImageView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldNoteWithPhotoView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldNotifyTimeView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldTimeView;
import ru.android.childdiary.presentation.core.images.ImagePickerDialogArguments;
import ru.android.childdiary.presentation.core.images.ImagePickerDialogFragment;
import ru.android.childdiary.presentation.core.images.review.ImageReviewActivity;
import ru.android.childdiary.presentation.events.core.EventDetailActivity;
import ru.android.childdiary.presentation.medical.pickers.visits.DoctorPickerActivity;
import ru.android.childdiary.utils.strings.TimeUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public class DoctorVisitEventDetailActivity
        extends EventDetailActivity<DoctorVisitEventDetailView, DoctorVisitEvent>
        implements DoctorVisitEventDetailView, FieldNoteWithPhotoView.PhotoListener,
        ImagePickerDialogFragment.Listener, FieldCheckBoxView.FieldCheckBoxListener {
    private static final String TAG_TIME_PICKER = "TIME_PICKER";
    private static final String TAG_DATE_PICKER = "DATE_PICKER";
    private static final String TAG_DURATION_DIALOG = "TAG_DURATION_DIALOG";
    private static final String TAG_NOTIFY_TIME_DIALOG = "TAG_NOTIFY_TIME_DIALOG";

    private static final int REQUEST_DOCTOR = 1;

    @InjectPresenter
    DoctorVisitEventDetailPresenter presenter;

    @BindView(R.id.doctorVisitNameView)
    FieldEditTextWithImageView doctorVisitNameView;

    @BindView(R.id.doctorView)
    FieldDoctorView doctorView;

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

    public static Intent getIntent(Context context, @Nullable MasterEvent masterEvent,
                                   @NonNull DoctorVisitEvent defaultEvent) {
        Intent intent = new Intent(context, DoctorVisitEventDetailActivity.class);
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
        setupEditTextView(doctorVisitNameView);
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
        doctorView.setFieldDialogListener(view -> {
            startActivityForResult(DoctorPickerActivity.getIntent(this, getSex()),
                    REQUEST_DOCTOR);
            hideKeyboardAndClearFocus(rootView.findFocus());
        });
        setupEditTextView(noteWithPhotoView);
        noteWithPhotoView.setPhotoListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DOCTOR) {
            if (resultCode == RESULT_OK) {
                Doctor doctor = (Doctor) data.getSerializableExtra(ExtraConstants.EXTRA_ITEM);
                doctorView.setValue(doctor);
            } else {
                getPresenter().checkValue(doctorView.getValue());
            }
        }
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarLogo(ResourcesUtils.getDoctorVisitLogoRes(getSex()));
        setupToolbarTitle(R.string.event_doctor_visit);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        setupToolbarLogo(ResourcesUtils.getDoctorVisitLogoRes(getSex()));
    }

    @Override
    public DoctorVisitEventDetailPresenter getPresenter() {
        return presenter;
    }

    @Override
    @LayoutRes
    protected int getContentLayoutResourceId() {
        return R.layout.activity_event_detail_doctor_visit;
    }

    @Override
    public void setupEventDetail(@NonNull DoctorVisitEvent event) {
        doctorView.setValue(event.getDoctor());
        doctorVisitNameView.setText(event.getName());
        durationView.setValue(event.getDurationInMinutes());
        WidgetsUtils.setDateTime(event.getDateTime(), dateView, timeView);
        notifyTimeView.setValue(event.getNotifyTimeInMinutes());
        notifyTimeView.setVisibility(notifyTimeViewVisible() ? View.VISIBLE : View.GONE);
        noteWithPhotoView.setText(event.getNote());
        noteWithPhotoView.setImageFileName(event.getImageFileName());
        checkBoxView.setValue(event.getIsDone());
    }

    @Override
    protected DoctorVisitEvent buildEvent(DoctorVisitEvent event) {
        DoctorVisitEvent.DoctorVisitEventBuilder builder = event == null
                ? DoctorVisitEvent.builder()
                : event.toBuilder();

        Doctor doctor = doctorView.getValue();
        String doctorVisitName = doctorVisitNameView.getText();
        Integer duration = durationView.getValue();
        DateTime dateTime = WidgetsUtils.getDateTime(dateView, timeView);
        Integer minutes = notifyTimeView.getValue();
        String note = noteWithPhotoView.getText();
        String imageFileName = noteWithPhotoView.getImageFileName();

        builder.dateTime(dateTime)
                .doctor(doctor)
                .name(doctorVisitName)
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
            unsubscribeOnDestroy(presenter.listenForFieldsUpdate(doctorVisitNameView.textObservable()));
        }
    }

    @Override
    public void doctorVisitEventNameValidated(boolean valid) {
        doctorVisitNameView.validated(valid);
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
    public void setDoctor(@Nullable Doctor doctor) {
        doctorView.setValue(doctor);
    }

    @Override
    public void requestPhotoAdd() {
        ImagePickerDialogFragment dialogFragment = new ImagePickerDialogFragment();
        dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_DATE_PICKER,
                ImagePickerDialogArguments.builder()
                        .sex(getSex())
                        .showDeleteItem(false)
                        .showCircleFrame(false)
                        .build());
    }

    @Override
    public void requestPhotoReview() {
        Intent intent = ImageReviewActivity.getIntent(this, noteWithPhotoView.getImageFileName());
        startActivity(intent);
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
