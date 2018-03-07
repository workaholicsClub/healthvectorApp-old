package ru.android.healthvector.presentation.medical.edit.visits;

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

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import ru.android.healthvector.R;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.calendar.data.core.RepeatParameters;
import ru.android.healthvector.domain.dictionaries.medicinemeasure.data.MedicineMeasure;
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;
import ru.android.healthvector.domain.medical.data.MedicineMeasureValue;
import ru.android.healthvector.domain.medical.data.MedicineTaking;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.core.events.BaseEditItemActivity;
import ru.android.healthvector.presentation.core.events.BaseEditItemPresenter;
import ru.android.healthvector.presentation.core.fields.widgets.FieldCheckBoxView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldDateView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldDoctorView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldDurationView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldEditTextView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldMedicineMeasureValueView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldMedicineView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldNoteWithPhotoView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldNotifyTimeView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldRepeatParametersView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldTimeView;
import ru.android.healthvector.utils.ObjectUtils;
import ru.android.healthvector.utils.strings.DateUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;
import ru.android.healthvector.utils.ui.WidgetsUtils;

public class EditMedicineTakingActivity extends BaseEditItemActivity<EditMedicineTakingView, MedicineTaking>
        implements EditMedicineTakingView {
    @InjectPresenter
    EditMedicineTakingPresenter presenter;

    @BindView(R.id.medicineView)
    FieldMedicineView medicineView;

    @BindView(R.id.medicineMeasureValueView)
    FieldMedicineMeasureValueView medicineMeasureView;

    @BindView(R.id.dateView)
    FieldDateView dateView;

    @BindView(R.id.timeView)
    FieldTimeView timeView;

    @BindView(R.id.checkBoxView)
    FieldCheckBoxView checkBoxView;

    @BindView(R.id.repeatParametersView)
    FieldRepeatParametersView repeatParametersView;

    @BindView(R.id.noteWithPhotoView)
    FieldNoteWithPhotoView noteWithPhotoView;

    @BindView(R.id.notifyTimeView)
    FieldNotifyTimeView notifyTimeView;

    public static Intent getIntent(Context context, @NonNull MedicineTaking medicineTaking,
                                   @NonNull MedicineTaking defaultMedicineTaking,
                                   @Nullable LocalTime startTime, @Nullable LocalTime finishTime) {
        return new Intent(context, EditMedicineTakingActivity.class)
                .putExtra(ExtraConstants.EXTRA_ITEM, medicineTaking)
                .putExtra(ExtraConstants.EXTRA_DEFAULT_ITEM, defaultMedicineTaking)
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
        setupToolbarTitle(R.string.edit_medicine_taking_title);
    }

    @Override
    protected BaseEditItemPresenter<EditMedicineTakingView, MedicineTaking> getPresenter() {
        return presenter;
    }

    @Override
    @LayoutRes
    protected int getContentLayoutResourceId() {
        return R.layout.activity_item_content_medicine_taking;
    }

    @Override
    protected void setup(MedicineTaking item) {
        medicineView.setValue(item.getMedicine());
        MedicineMeasureValue medicineMeasureValue = MedicineMeasureValue.builder()
                .amount(item.getAmount())
                .medicineMeasure(item.getMedicineMeasure())
                .build();
        medicineMeasureView.setValue(medicineMeasureValue);
        repeatParametersView.setRepeatParameters(item.getRepeatParameters());
        WidgetsUtils.setDateTime(item.getDateTime(), dateView, timeView);
        notifyTimeView.setValue(item.getNotifyTimeInMinutes());
        boolean notifyTimeViewVisible = defaultItem.getNotifyTimeInMinutes() != null;
        notifyTimeView.setVisibility(notifyTimeViewVisible ? View.VISIBLE : View.GONE);
        noteWithPhotoView.setText(item.getNote());
        noteWithPhotoView.setImageFileName(item.getImageFileName());

        boolean exported = ObjectUtils.isTrue(item.getIsExported());
        checkBoxView.setChecked(exported);
        if (exported) {
            medicineView.setReadOnly(true);
            medicineMeasureView.setReadOnly(true);
            checkBoxView.setVisibility(View.GONE);
            dateView.setReadOnly(true);
            timeView.setReadOnly(true);
            repeatParametersView.setReadOnly(true);
            notifyTimeView.setReadOnly(true);
            if (!item.isDone()) {
                buttonAdd.setText(R.string.finish);
                buttonAdd.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected MedicineTaking build() {
        Medicine medicine = medicineView.getValue();
        MedicineMeasureValue medicineMeasureValue = medicineMeasureView.getValue();
        Double amount = medicineMeasureValue == null ? null : medicineMeasureValue.getAmount();
        MedicineMeasure medicineMeasure = medicineMeasureValue == null ? null : medicineMeasureValue.getMedicineMeasure();
        RepeatParameters repeatParameters = repeatParametersView.getRepeatParameters();
        DateTime dateTime = WidgetsUtils.getDateTime(dateView, timeView);
        boolean exported = checkBoxView.isChecked();
        Integer minutes = notifyTimeView.getValue();
        String note = noteWithPhotoView.getText();
        String imageFileName = noteWithPhotoView.getImageFileName();

        return item.toBuilder()
                .medicine(medicine)
                .amount(amount)
                .medicineMeasure(medicineMeasure)
                .repeatParameters(repeatParameters)
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
    protected boolean contentEquals(MedicineTaking item1, MedicineTaking item2) {
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
        return null;
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
        return Collections.singletonList(noteWithPhotoView);
    }

    @Nullable
    @Override
    protected FieldDoctorView getDoctorView() {
        return null;
    }

    @Nullable
    @Override
    protected FieldMedicineView getMedicineView() {
        return medicineView;
    }

    @Nullable
    @Override
    public FieldMedicineMeasureValueView getMedicineMeasureValueView() {
        return medicineMeasureView;
    }

    @Nullable
    @Override
    public FieldNoteWithPhotoView getNoteWithPhotoView() {
        return noteWithPhotoView;
    }

    @Override
    public void confirmDeleteOneItem(@NonNull MedicineTaking medicineTaking) {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.delete_medicine_taking_confirmation_dialog_title)
                .setPositiveButton(R.string.delete,
                        (dialog, which) -> getPresenter().deleteOneItem(medicineTaking))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void askDeleteConnectedEventsOrNot(@NonNull MedicineTaking medicineTaking) {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.ask_delete_medicine_taking_connected_events_or_not)
                .setPositiveButton(R.string.delete_only_medicine_taking,
                        (dialog, which) -> getPresenter().deleteOneItem(medicineTaking))
                .setNegativeButton(R.string.delete_medicine_taking_and_events,
                        (dialog, which) -> getPresenter().deleteWithConnectedEvents(medicineTaking))
                .show();
    }

    @Override
    public void askCompleteFromDate(@NonNull MedicineTaking medicineTaking, @NonNull DateTime dateTime) {
        String dateStr = DateUtils.date(this, dateTime);
        String timeStr = DateUtils.time(this, dateTime);
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(getString(R.string.ask_complete_medicine_taking, timeStr, dateStr))
                .setPositiveButton(R.string.complete_without_deletion,
                        (dialog, which) -> getPresenter().completeWithoutDeletion(medicineTaking, dateTime))
                .setNegativeButton(R.string.complete_and_delete_events,
                        (dialog, which) -> getPresenter().completeAndDeleteFromDate(medicineTaking, dateTime))
                .show();
    }
}
