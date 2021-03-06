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

import java.util.List;

import butterknife.BindView;
import ru.android.healthvector.R;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.calendar.data.MedicineTakingEvent;
import ru.android.healthvector.domain.calendar.data.core.MasterEvent;
import ru.android.healthvector.domain.dictionaries.medicinemeasure.data.MedicineMeasure;
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;
import ru.android.healthvector.domain.medical.data.MedicineMeasureValue;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.core.fields.dialogs.MedicineMeasureValueDialogArguments;
import ru.android.healthvector.presentation.core.fields.dialogs.MedicineMeasureValueDialogFragment;
import ru.android.healthvector.presentation.core.fields.dialogs.TimeDialogArguments;
import ru.android.healthvector.presentation.core.fields.dialogs.TimeDialogFragment;
import ru.android.healthvector.presentation.core.fields.widgets.FieldCheckBoxView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldDateView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldMedicineMeasureValueView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldMedicineView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldNoteWithPhotoView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldNotifyTimeView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldTimeView;
import ru.android.healthvector.presentation.core.images.ImagePickerDialogArguments;
import ru.android.healthvector.presentation.core.images.ImagePickerDialogFragment;
import ru.android.healthvector.presentation.core.images.review.ImageReviewActivity;
import ru.android.healthvector.presentation.dictionaries.medicines.MedicinePickerActivity;
import ru.android.healthvector.presentation.events.core.PeriodicEventDetailActivity;
import ru.android.healthvector.utils.ui.ResourcesUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;
import ru.android.healthvector.utils.ui.WidgetsUtils;

public class MedicineTakingEventDetailActivity
        extends PeriodicEventDetailActivity<MedicineTakingEventDetailView, MedicineTakingEvent>
        implements MedicineTakingEventDetailView, MedicineMeasureValueDialogFragment.Listener,
        FieldNoteWithPhotoView.PhotoListener, ImagePickerDialogFragment.Listener,
        FieldCheckBoxView.FieldCheckBoxListener {
    private static final String TAG_TIME_PICKER = "TIME_PICKER";
    private static final String TAG_DATE_PICKER = "DATE_PICKER";
    private static final String TAG_IMAGE_PICKER = "IMAGE_PICKER";
    private static final String TAG_NOTIFY_TIME_DIALOG = "TAG_NOTIFY_TIME_DIALOG";
    private static final String TAG_MEDICINE_MEASURE_VALUE_DIALOG = "TAG_MEDICINE_MEASURE_VALUE_DIALOG";

    private static final int REQUEST_MEDICINE = 1;
    private static final int REQUEST_IMAGE_REVIEW = 2;

    @InjectPresenter
    MedicineTakingEventDetailPresenter presenter;

    @BindView(R.id.medicineView)
    FieldMedicineView medicineView;

    @BindView(R.id.medicineMeasureValueView)
    FieldMedicineMeasureValueView medicineMeasureValueView;

    @BindView(R.id.dateView)
    FieldDateView dateView;

    @BindView(R.id.timeView)
    FieldTimeView timeView;

    @BindView(R.id.noteWithPhotoView)
    FieldNoteWithPhotoView noteWithPhotoView;

    @BindView(R.id.notifyTimeView)
    FieldNotifyTimeView notifyTimeView;

    @BindView(R.id.checkBoxView)
    FieldCheckBoxView checkBoxView;

    public static Intent getIntent(Context context,
                                   @Nullable MasterEvent masterEvent,
                                   @NonNull MedicineTakingEvent defaultEvent,
                                   boolean isLinearGroupFinished) {
        return new Intent(context, MedicineTakingEventDetailActivity.class)
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
        medicineView.setFieldDialogListener(view -> {
            startActivityForResult(MedicinePickerActivity.getIntent(this, getSex(), true),
                    REQUEST_MEDICINE);
            hideKeyboardAndClearFocus();
        });
        medicineMeasureValueView.setFieldDialogListener(view -> getPresenter().requestMedicineMeasureValueDialog());
        setupEditTextView(noteWithPhotoView);
        noteWithPhotoView.setPhotoListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDICINE) {
            if (resultCode == RESULT_OK) {
                Medicine medicine = (Medicine) data.getSerializableExtra(ExtraConstants.EXTRA_ITEM);
                medicineView.setValue(medicine);
            } else {
                getPresenter().checkValue(medicineView.getValue());
            }
        } else if (requestCode == REQUEST_IMAGE_REVIEW) {
            if (resultCode == RESULT_OK) {
                noteWithPhotoView.setImageFileName(null);
            }
        }
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarLogo(ResourcesUtils.getMedicineTakingLogoRes(getSex()));
        setupToolbarTitle(R.string.event_medicine_taking);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        setupToolbarLogo(ResourcesUtils.getMedicineTakingLogoRes(getSex()));
        noteWithPhotoView.setSex(getSex());
    }

    @Override
    public MedicineTakingEventDetailPresenter getPresenter() {
        return presenter;
    }

    @Override
    @LayoutRes
    protected int getContentLayoutResourceId() {
        return R.layout.activity_event_detail_medicine_taking;
    }

    @Override
    public void setupEventDetail(@NonNull MedicineTakingEvent event) {
        medicineView.setValue(event.getMedicine());
        MedicineMeasureValue medicineMeasureValue = MedicineMeasureValue.builder()
                .amount(event.getAmount())
                .medicineMeasure(event.getMedicineMeasure())
                .build();
        medicineMeasureValueView.setValue(medicineMeasureValue);
        WidgetsUtils.setDateTime(event.getDateTime(), dateView, timeView);
        notifyTimeView.setValue(event.getNotifyTimeInMinutes());
        notifyTimeView.setVisibility(notifyTimeViewVisible() ? View.VISIBLE : View.GONE);
        noteWithPhotoView.setText(event.getNote());
        noteWithPhotoView.setImageFileName(event.getImageFileName());
        checkBoxView.setValue(event.getIsDone());
    }

    @Override
    protected MedicineTakingEvent buildEvent(MedicineTakingEvent event) {
        MedicineTakingEvent.MedicineTakingEventBuilder builder = event == null
                ? MedicineTakingEvent.builder()
                : event.toBuilder();

        Medicine medicine = medicineView.getValue();
        MedicineMeasureValue medicineMeasureValue = medicineMeasureValueView.getValue();
        Double amount = medicineMeasureValue == null ? null : medicineMeasureValue.getAmount();
        MedicineMeasure medicineMeasure = medicineMeasureValue == null ? null : medicineMeasureValue.getMedicineMeasure();
        DateTime dateTime = WidgetsUtils.getDateTime(dateView, timeView);
        Integer minutes = notifyTimeView.getValue();
        String note = noteWithPhotoView.getText();
        String imageFileName = noteWithPhotoView.getImageFileName();

        builder.dateTime(dateTime)
                .medicine(medicine)
                .amount(amount)
                .medicineMeasure(medicineMeasure)
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
    public void onSetMedicineMeasureValue(String tag, @Nullable MedicineMeasureValue medicineMeasureValue) {
        medicineMeasureValueView.setValue(medicineMeasureValue);
    }

    @Override
    public void showMedicineMeasureValueDialog(@NonNull List<MedicineMeasure> medicineMeasureList) {
        MedicineMeasureValueDialogFragment dialogFragment = new MedicineMeasureValueDialogFragment();
        dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_MEDICINE_MEASURE_VALUE_DIALOG,
                MedicineMeasureValueDialogArguments.builder()
                        .sex(getSex())
                        .medicineMeasureList(medicineMeasureList)
                        .medicineMeasureValue(medicineMeasureValueView.getValue())
                        .build());
    }

    @Override
    public void setMedicine(@Nullable Medicine medicine) {
        medicineView.setValue(medicine);
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
