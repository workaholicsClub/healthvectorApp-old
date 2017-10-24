package ru.android.childdiary.presentation.medical.edit.medicines;

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
import org.joda.time.LocalTime;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.calendar.data.core.RepeatParameters;
import ru.android.childdiary.domain.dictionaries.doctors.data.Doctor;
import ru.android.childdiary.domain.medical.data.DoctorVisit;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.events.BaseEditItemActivity;
import ru.android.childdiary.presentation.core.events.BaseEditItemPresenter;
import ru.android.childdiary.presentation.core.fields.widgets.FieldCheckBoxView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDateView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDoctorView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDurationView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldEditTextView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldEditTextWithImageView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldMedicineMeasureValueView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldMedicineView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldNoteWithPhotoView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldNotifyTimeView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldRepeatParametersView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldTimeView;
import ru.android.childdiary.utils.ObjectUtils;
import ru.android.childdiary.utils.strings.DateUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public class EditDoctorVisitActivity extends BaseEditItemActivity<EditDoctorVisitView, DoctorVisit>
        implements EditDoctorVisitView {
    @InjectPresenter
    EditDoctorVisitPresenter presenter;

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

    @BindView(R.id.checkBoxView)
    FieldCheckBoxView checkBoxView;

    @BindView(R.id.repeatParametersView)
    FieldRepeatParametersView repeatParametersView;

    @BindView(R.id.noteWithPhotoView)
    FieldNoteWithPhotoView noteWithPhotoView;

    @BindView(R.id.notifyTimeView)
    FieldNotifyTimeView notifyTimeView;

    public static Intent getIntent(Context context, @NonNull DoctorVisit doctorVisit,
                                   @NonNull DoctorVisit defaultDoctorVisit,
                                   @Nullable LocalTime startTime, @Nullable LocalTime finishTime) {
        return new Intent(context, EditDoctorVisitActivity.class)
                .putExtra(ExtraConstants.EXTRA_ITEM, doctorVisit)
                .putExtra(ExtraConstants.EXTRA_DEFAULT_ITEM, defaultDoctorVisit)
                .putExtra(ExtraConstants.EXTRA_START_TIME, startTime)
                .putExtra(ExtraConstants.EXTRA_FINISH_TIME, finishTime);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        changeThemeIfNeeded(item.getChild());
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        noteWithPhotoView.setSex(getSex());
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.doctor_visit);
    }

    @Override
    protected BaseEditItemPresenter<EditDoctorVisitView, DoctorVisit> getPresenter() {
        return presenter;
    }

    @Override
    @LayoutRes
    protected int getContentLayoutResourceId() {
        return R.layout.activity_item_content_doctor_visit;
    }

    @Override
    protected void setup(DoctorVisit item) {
        doctorView.setValue(item.getDoctor());
        repeatParametersView.setRepeatParameters(item.getRepeatParameters());
        doctorVisitNameView.setText(item.getName());
        durationView.setValue(item.getDurationInMinutes());
        WidgetsUtils.setDateTime(item.getDateTime(), dateView, timeView);
        notifyTimeView.setValue(item.getNotifyTimeInMinutes());
        boolean notifyTimeViewVisible = defaultItem.getNotifyTimeInMinutes() != null;
        notifyTimeView.setVisibility(notifyTimeViewVisible ? View.VISIBLE : View.GONE);
        noteWithPhotoView.setText(item.getNote());
        noteWithPhotoView.setImageFileName(item.getImageFileName());

        boolean exported = ObjectUtils.isTrue(item.getIsExported());
        checkBoxView.setChecked(exported);
        if (exported) {
            doctorVisitNameView.setReadOnly(true);
            doctorView.setReadOnly(true);
            durationView.setReadOnly(true);
            checkBoxView.setVisibility(View.GONE);
            dateView.setReadOnly(true);
            timeView.setReadOnly(true);
            repeatParametersView.setReadOnly(true);
            noteWithPhotoView.setReadOnly(true);
            notifyTimeView.setReadOnly(true);
            if (!item.isDone()) {
                buttonAdd.setText(R.string.finish);
                buttonAdd.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected DoctorVisit build() {
        Doctor doctor = doctorView.getValue();
        RepeatParameters repeatParameters = repeatParametersView.getRepeatParameters();
        String doctorVisitName = doctorVisitNameView.getText();
        Integer duration = durationView.getValue();
        DateTime dateTime = WidgetsUtils.getDateTime(dateView, timeView);
        boolean exported = checkBoxView.isChecked();
        Integer minutes = notifyTimeView.getValue();
        String note = noteWithPhotoView.getText();
        String imageFileName = noteWithPhotoView.getImageFileName();

        return item.toBuilder()
                .doctor(doctor)
                .repeatParameters(repeatParameters)
                .name(doctorVisitName)
                .durationInMinutes(duration)
                .dateTime(dateTime)
                .isExported(exported)
                .notifyTimeInMinutes(minutes)
                .note(note)
                .imageFileName(imageFileName)
                .build();
    }

    @Override
    public void onChecked() {
        boolean exported = ObjectUtils.isTrue(item.getIsExported());
        if (!exported) {
            boolean readOnly = !checkBoxView.isChecked();
            repeatParametersView.setReadOnly(readOnly);
        }
    }

    @Override
    protected boolean contentEquals(DoctorVisit item1, DoctorVisit item2) {
        return ObjectUtils.contentEquals(item1, item2);
    }

    @Override
    protected FieldDateView getDateView() {
        return dateView;
    }

    @Nullable
    @Override
    protected FieldTimeView getTimeView() {
        return timeView;
    }

    @Override
    protected FieldNotifyTimeView getNotifyTimeView() {
        return notifyTimeView;
    }

    @Nullable
    @Override
    protected FieldDurationView getDurationView() {
        return durationView;
    }

    @Nullable
    @Override
    protected FieldCheckBoxView getCheckBoxView() {
        return checkBoxView;
    }

    @Override
    protected FieldRepeatParametersView getRepeatParametersView() {
        return repeatParametersView;
    }

    @Override
    protected List<FieldEditTextView> getEditTextViews() {
        return Arrays.asList(doctorVisitNameView, noteWithPhotoView);
    }

    @Nullable
    @Override
    protected FieldDoctorView getDoctorView() {
        return doctorView;
    }

    @Nullable
    @Override
    protected FieldMedicineView getMedicineView() {
        return null;
    }

    @Nullable
    @Override
    protected FieldMedicineMeasureValueView getMedicineMeasureValueView() {
        return null;
    }

    @Nullable
    @Override
    protected FieldNoteWithPhotoView getNoteWithPhotoView() {
        return noteWithPhotoView;
    }

    @Override
    public void confirmDeleteOneItem(@NonNull DoctorVisit doctorVisit) {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.delete_doctor_visit_confirmation_dialog_title)
                .setPositiveButton(R.string.delete,
                        (dialog, which) -> getPresenter().deleteOneItem(doctorVisit))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void askDeleteConnectedEventsOrNot(@NonNull DoctorVisit doctorVisit) {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.ask_delete_doctor_visit_connected_events_or_not)
                .setPositiveButton(R.string.delete_only_doctor_visit,
                        (dialog, which) -> getPresenter().deleteOneItem(doctorVisit))
                .setNegativeButton(R.string.delete_doctor_visit_and_events,
                        (dialog, which) -> getPresenter().deleteWithConnectedEvents(doctorVisit))
                .show();
    }

    @Override
    public void askCompleteFromDate(@NonNull DoctorVisit doctorVisit, @NonNull DateTime dateTime) {
        String dateStr = DateUtils.date(this, dateTime);
        String timeStr = DateUtils.time(this, dateTime);
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(getString(R.string.ask_complete_doctor_visit, timeStr, dateStr))
                .setPositiveButton(R.string.complete_without_deletion,
                        (dialog, which) -> getPresenter().completeWithoutDeletion(doctorVisit, dateTime))
                .setNegativeButton(R.string.complete_and_delete_events,
                        (dialog, which) -> getPresenter().completeAndDeleteFromDate(doctorVisit, dateTime))
                .show();
    }
}
