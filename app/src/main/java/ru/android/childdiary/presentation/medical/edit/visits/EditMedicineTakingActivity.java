package ru.android.childdiary.presentation.medical.edit.visits;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasureValue;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.medical.core.BaseEditItemActivity;
import ru.android.childdiary.presentation.medical.core.BaseEditItemPresenter;
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
import ru.android.childdiary.utils.ObjectUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

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
                                   @NonNull MedicineTaking defaultMedicineTaking) {
        Intent intent = new Intent(context, EditMedicineTakingActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_ITEM, medicineTaking);
        intent.putExtra(ExtraConstants.EXTRA_DEFAULT_ITEM, defaultMedicineTaking);
        return intent;
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
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.edit_medicine_taking_title);
    }

    @Override
    protected BaseEditItemPresenter<EditMedicineTakingView, MedicineTaking> getPresenter() {
        return presenter;
    }

    @Override
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

        boolean exported = ObjectUtils.isTrue(item.getExported());
        checkBoxView.setChecked(exported);
        if (exported) {
            medicineView.setReadOnly(true);
            medicineMeasureView.setReadOnly(true);
            checkBoxView.setVisibility(View.GONE);
            dateView.setReadOnly(true);
            timeView.setReadOnly(true);
            repeatParametersView.setReadOnly(true);
            noteWithPhotoView.setEnabled(false);
            notifyTimeView.setReadOnly(true);
            if (item.getFinishDateTime() == null) {
                buttonAdd.setText(R.string.finish);
                buttonAdd.setVisibility(View.VISIBLE);
            }
        }

        notifyTimeView.setValue(item.getNotifyTimeInMinutes());
        boolean notifyTimeViewVisible = ObjectUtils.isPositive(defaultItem.getNotifyTimeInMinutes());
        notifyTimeView.setVisibility(notifyTimeViewVisible ? View.VISIBLE : View.GONE);
        noteWithPhotoView.setText(item.getNote());
        // TODO image file name
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
        String imageFileName = null;

        return item.toBuilder()
                .medicine(medicine)
                .amount(amount)
                .medicineMeasure(medicineMeasure)
                .repeatParameters(repeatParameters)
                .dateTime(dateTime)
                .exported(exported)
                .notifyTimeInMinutes(minutes)
                .note(note)
                .imageFileName(imageFileName)
                .build();
    }

    @Override
    protected boolean contentEquals(MedicineTaking item1, MedicineTaking item2) {
        return ObjectUtils.contentEquals(item1, item2);
    }

    @Override
    protected FieldDateView getDateView() {
        return dateView;
    }

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
    public FieldDoctorView getDoctorView() {
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
}
