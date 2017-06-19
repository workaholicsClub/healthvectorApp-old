package ru.android.childdiary.presentation.core.events;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.core.LengthValue;
import ru.android.childdiary.domain.interactors.core.PeriodicityType;
import ru.android.childdiary.domain.interactors.core.TimeUnit;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasureValue;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.dialogs.LengthValueDialogArguments;
import ru.android.childdiary.presentation.core.fields.dialogs.LengthValueDialogFragment;
import ru.android.childdiary.presentation.core.fields.dialogs.MedicineMeasureValueDialogArguments;
import ru.android.childdiary.presentation.core.fields.dialogs.MedicineMeasureValueDialogFragment;
import ru.android.childdiary.presentation.core.fields.dialogs.TimeDialogArguments;
import ru.android.childdiary.presentation.core.fields.dialogs.TimeDialogFragment;
import ru.android.childdiary.presentation.core.fields.widgets.FieldCheckBoxView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDateView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDoctorView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDurationView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldEditTextView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldMedicineMeasureValueView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldMedicineView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldNoteWithPhotoView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldNotifyTimeView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldRepeatParametersView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldTimeView;
import ru.android.childdiary.presentation.core.images.ImagePickerDialogArguments;
import ru.android.childdiary.presentation.core.images.ImagePickerDialogFragment;
import ru.android.childdiary.presentation.core.images.review.ImageReviewActivity;
import ru.android.childdiary.presentation.core.widgets.CustomDatePickerDialog;
import ru.android.childdiary.presentation.core.widgets.CustomTimePickerDialog;
import ru.android.childdiary.presentation.medical.pickers.medicines.MedicinePickerActivity;
import ru.android.childdiary.presentation.medical.pickers.visits.DoctorPickerActivity;
import ru.android.childdiary.utils.TimeUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public abstract class BaseItemActivity<V extends BaseItemView<T>, T extends Serializable>
        extends BaseMvpActivity implements BaseItemView<T>,
        FieldCheckBoxView.FieldCheckBoxListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, TimeDialogFragment.Listener,
        MedicineMeasureValueDialogFragment.Listener, LengthValueDialogFragment.Listener,
        FieldNoteWithPhotoView.PhotoListener, ImagePickerDialogFragment.Listener {
    private static final String TAG_TIME_PICKER = "TIME_PICKER";
    private static final String TAG_DATE_PICKER = "DATE_PICKER";
    private static final String TAG_DURATION_DIALOG = "TAG_DURATION_DIALOG";
    private static final String TAG_NOTIFY_TIME_DIALOG = "TAG_NOTIFY_TIME_DIALOG";
    private static final String TAG_MEDICINE_MEASURE_VALUE_DIALOG = "TAG_MEDICINE_MEASURE_VALUE_DIALOG";
    private static final String TAG_LENGTH_VALUE_DIALOG = "TAG_LENGTH_VALUE_DIALOG";
    private static final String TAG_PROGRESS_DIALOG_GENERATING_EVENTS = "TAG_PROGRESS_DIALOG_GENERATING_EVENTS";

    private static final int REQUEST_DOCTOR = 1;
    private static final int REQUEST_MEDICINE = 2;

    protected T defaultItem;

    @BindView(R.id.buttonAdd)
    protected Button buttonAdd;

    @BindView(R.id.rootView)
    View rootView;

    private ViewGroup detailsView;

    private boolean isValidationStarted;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //noinspection unchecked
        defaultItem = (T) getIntent().getSerializableExtra(ExtraConstants.EXTRA_DEFAULT_ITEM);

        if (getCheckBoxView() != null) {
            getCheckBoxView().setText(R.string.export_to_calendar);
            getCheckBoxView().setFieldCheckBoxListener(this);
        }

        for (FieldEditTextView editTextView : getEditTextViews()) {
            setupEditTextView(editTextView);
        }

        getDateView().setFieldDialogListener(v -> showDatePicker(TAG_DATE_PICKER, getDateView().getValue(), null, null));
        if (getTimeView() != null) {
            getTimeView().setFieldDialogListener(v -> showTimePicker(TAG_TIME_PICKER, getTimeView().getValue()));
        }
        getNotifyTimeView().setFieldDialogListener(v -> {
            TimeDialogFragment dialogFragment = new TimeDialogFragment();
            dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_NOTIFY_TIME_DIALOG,
                    TimeDialogArguments.builder()
                            .sex(getSex())
                            .minutes(getNotifyTimeView().getValueInt())
                            .showDays(true)
                            .showHours(true)
                            .showMinutes(true)
                            .title(getString(R.string.notify_time_dialog_title))
                            .build());
        });
        if (getDurationView() != null) {
            getDurationView().setFieldDialogListener(v -> {
                TimeDialogFragment dialogFragment = new TimeDialogFragment();
                dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_DURATION_DIALOG,
                        TimeDialogArguments.builder()
                                .sex(getSex())
                                .minutes(getDurationView().getValueInt())
                                .showDays(getDurationView().getValueInt() >= TimeUtils.MINUTES_IN_DAY)
                                .showHours(true)
                                .showMinutes(true)
                                .title(getString(R.string.duration))
                                .build());
            });
        }
        if (getDoctorView() != null) {
            getDoctorView().setFieldDialogListener(view -> {
                startActivityForResult(DoctorPickerActivity.getIntent(this, getSex()),
                        REQUEST_DOCTOR);
                hideKeyboardAndClearFocus(rootView.findFocus());
            });
        }
        if (getMedicineView() != null) {
            getMedicineView().setFieldDialogListener(view -> {
                startActivityForResult(MedicinePickerActivity.getIntent(this, getSex()),
                        REQUEST_MEDICINE);
                hideKeyboardAndClearFocus(rootView.findFocus());
            });
        }
        if (getMedicineMeasureValueView() != null) {
            getMedicineMeasureValueView().setFieldDialogListener(view -> getPresenter().requestMedicineMeasureValueDialog());
        }
        LocalTime startTime = (LocalTime) getIntent().getSerializableExtra(ExtraConstants.EXTRA_START_TIME);
        LocalTime finishTime = (LocalTime) getIntent().getSerializableExtra(ExtraConstants.EXTRA_FINISH_TIME);
        getRepeatParametersView().setTimeLimits(startTime, finishTime);
        getRepeatParametersView().setListener(() -> getPresenter().requestLengthValueDialog());
        getRepeatParametersView().setFieldTimesListener((i, time) -> showTimePicker(String.valueOf(i), time));
        if (getNoteWithPhotoView() != null) {
            getNoteWithPhotoView().setPhotoListener(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DOCTOR && getDoctorView() != null) {
            if (resultCode == RESULT_OK) {
                Doctor doctor = (Doctor) data.getSerializableExtra(ExtraConstants.EXTRA_ITEM);
                getDoctorView().setValue(doctor);
            } else {
                getPresenter().checkValue(getDoctorView().getValue());
            }
        } else if (requestCode == REQUEST_MEDICINE && getMedicineView() != null) {
            if (resultCode == RESULT_OK) {
                Medicine medicine = (Medicine) data.getSerializableExtra(ExtraConstants.EXTRA_ITEM);
                getMedicineView().setValue(medicine);
            } else {
                getPresenter().checkValue(getMedicineView().getValue());
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //noinspection unchecked
        T item = (T) savedInstanceState.getSerializable(ExtraConstants.EXTRA_ITEM);
        setup(item);
        logger.debug("restore: " + item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        T item = build();
        outState.putSerializable(ExtraConstants.EXTRA_ITEM, item);
        logger.debug("save: " + item);
    }

    @Override
    protected void setContentViewBeforeBind() {
        LayoutInflater inflater = LayoutInflater.from(this);
        detailsView = ButterKnife.findById(this, R.id.detailsView);
        View contentView = inflater.inflate(getContentLayoutResourceId(), detailsView);
    }

    protected void setupEditTextView(FieldEditTextView view) {
        List<Disposable> disposables = view.createSubscriptions(this::hideKeyboardAndClearFocus);
        for (Disposable disposable : disposables) {
            unsubscribeOnDestroy(disposable);
        }
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        if (getCheckBoxView() != null) {
            getCheckBoxView().setSex(getSex());
        }
        getRepeatParametersView().setSex(getSex());
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideKeyboardAndClearFocus(rootView.findFocus());
    }

    @Override
    public void onChecked() {
    }

    @Override
    public void showFrequencyList(List<Integer> frequencyList) {
        getRepeatParametersView().updateFrequency(frequencyList);
    }

    @Override
    public void showPeriodicityList(List<PeriodicityType> periodicityList) {
        getRepeatParametersView().updatePeriodicity(periodicityList);
    }

    @Override
    public void setDoctor(@Nullable Doctor doctor) {
        if (getDoctorView() != null) {
            getDoctorView().setValue(doctor);
        }
    }

    @Override
    public void setMedicine(@Nullable Medicine medicine) {
        if (getMedicineView() != null) {
            getMedicineView().setValue(medicine);
        }
    }

    @Override
    public void showMedicineMeasureValueDialog(@NonNull ArrayList<MedicineMeasure> medicineMeasureList) {
        if (getMedicineMeasureValueView() == null) {
            return;
        }
        MedicineMeasureValueDialogFragment dialogFragment = new MedicineMeasureValueDialogFragment();
        dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_MEDICINE_MEASURE_VALUE_DIALOG,
                MedicineMeasureValueDialogArguments.builder()
                        .sex(getSex())
                        .medicineMeasureList(medicineMeasureList)
                        .medicineMeasureValue(getMedicineMeasureValueView().getValue())
                        .build());
    }

    @Override
    public void showLengthValueDialog(@NonNull ArrayList<TimeUnit> timeUnits) {
        LengthValue lengthValue = getRepeatParametersView().getLengthValue();
        LengthValueDialogFragment dialogFragment = new LengthValueDialogFragment();
        dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_LENGTH_VALUE_DIALOG,
                LengthValueDialogArguments.builder()
                        .sex(getSex())
                        .timeUnits(timeUnits)
                        .lengthValue(lengthValue)
                        .build());
    }

    @Override
    public void showGeneratingEvents(boolean loading) {
        if (loading) {
            buttonAdd.setEnabled(false);
            showProgress(TAG_PROGRESS_DIALOG_GENERATING_EVENTS,
                    getString(R.string.please_wait),
                    getString(R.string.events_generating));
        } else {
            buttonAdd.setEnabled(true);
            hideProgress(TAG_PROGRESS_DIALOG_GENERATING_EVENTS);
        }
    }

    @Override
    public final void validationFailed() {
        if (!isValidationStarted) {
            isValidationStarted = true;
            validationStarted();
        }
    }

    protected void validationStarted() {
    }

    @Override
    public void showValidationErrorMessage(String msg) {
        showToast(msg);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof TimePickerDialog) {
            ((TimePickerDialog) fragment).setOnTimeSetListener(this);
        } else if (fragment instanceof DatePickerDialog) {
            ((DatePickerDialog) fragment).setOnDateSetListener(this);
        }
    }

    protected void showDatePicker(String tag, @Nullable LocalDate date,
                                  @Nullable LocalDate minDate, @Nullable LocalDate maxDate) {
        DatePickerDialog dpd = CustomDatePickerDialog.create(this, this, date, getSex(),
                minDate, maxDate);
        dpd.show(getFragmentManager(), tag);
    }

    @Override
    public final void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        LocalDate date = LocalDate.fromCalendarFields(calendar);
        setDate(view.getTag(), date);
    }

    private void setDate(String tag, LocalDate date) {
        switch (tag) {
            case TAG_DATE_PICKER:
                getDateView().setValue(date);
                break;
        }
    }

    protected void showTimePicker(String tag, @Nullable LocalTime time) {
        TimePickerDialog tpd = CustomTimePickerDialog.create(this, this, time, getSex());
        tpd.show(getFragmentManager(), tag);
    }

    @Override
    public final void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        LocalTime time = new LocalTime(hourOfDay, minute);
        setTime(view.getTag(), time);
    }

    private void setTime(String tag, LocalTime time) {
        try {
            int i = Integer.parseInt(tag);
            getRepeatParametersView().setTime(i, time);
        } catch (NumberFormatException e) {
            switch (tag) {
                case TAG_TIME_PICKER:
                    if (getTimeView() != null) {
                        getTimeView().setValue(time);
                    }
                    break;
            }
        }
    }

    @Override
    public void onSetTime(String tag, int minutes) {
        switch (tag) {
            case TAG_DURATION_DIALOG:
                if (getDurationView() != null) {
                    getDurationView().setValue(minutes);
                }
                break;
            case TAG_NOTIFY_TIME_DIALOG:
                getNotifyTimeView().setValue(minutes);
                break;
        }
    }

    @Override
    public void onSetMedicineMeasureValue(String tag, @NonNull MedicineMeasureValue medicineMeasureValue) {
        if (getMedicineMeasureValueView() != null) {
            getMedicineMeasureValueView().setValue(medicineMeasureValue);
        }
    }

    @Override
    public void onSetLengthValue(String tag, @NonNull LengthValue lengthValue) {
        getRepeatParametersView().setLengthValue(lengthValue);
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
        if (getNoteWithPhotoView() == null) {
            return;
        }
        Intent intent = ImageReviewActivity.getIntent(this, getNoteWithPhotoView().getImageFileName());
        startActivity(intent);
    }

    @Override
    public void requestPhotoDelete() {
        if (getNoteWithPhotoView() == null) {
            return;
        }
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.delete_photo_confirmation_dialog_title)
                .setPositiveButton(R.string.delete,
                        (DialogInterface dialog, int which) -> getNoteWithPhotoView().setImageFileName(null))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onSetImage(@Nullable String relativeFileName) {
        if (getNoteWithPhotoView() != null) {
            getNoteWithPhotoView().setImageFileName(relativeFileName);
        }
    }

    @Override
    public void onBackPressed() {
        boolean processed = getRepeatParametersView().dismissPopupWindow();
        if (processed) {
            return;
        }

        saveChangesOrExit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveChangesOrExit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract void saveChangesOrExit();

    protected abstract BaseItemPresenter<V, T> getPresenter();

    @LayoutRes
    protected abstract int getContentLayoutResourceId();

    protected abstract void setup(T item);

    protected abstract T build();

    protected abstract boolean contentEquals(T item1, T item2);

    protected abstract FieldDateView getDateView();

    @Nullable
    protected abstract FieldTimeView getTimeView();

    protected abstract FieldNotifyTimeView getNotifyTimeView();

    @Nullable
    protected abstract FieldDurationView getDurationView();

    @Nullable
    protected abstract FieldCheckBoxView getCheckBoxView();

    protected abstract FieldRepeatParametersView getRepeatParametersView();

    protected abstract List<FieldEditTextView> getEditTextViews();

    @Nullable
    protected abstract FieldDoctorView getDoctorView();

    @Nullable
    protected abstract FieldMedicineView getMedicineView();

    @Nullable
    protected abstract FieldMedicineMeasureValueView getMedicineMeasureValueView();

    @Nullable
    protected abstract FieldNoteWithPhotoView getNoteWithPhotoView();
}