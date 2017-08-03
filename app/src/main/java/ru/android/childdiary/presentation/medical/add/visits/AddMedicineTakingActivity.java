package ru.android.childdiary.presentation.medical.add.visits;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.data.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.medical.data.MedicineTaking;
import ru.android.childdiary.domain.interactors.dictionaries.medicines.Medicine;
import ru.android.childdiary.domain.interactors.dictionaries.medicinemeasure.MedicineMeasure;
import ru.android.childdiary.domain.interactors.medical.data.MedicineMeasureValue;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.bindings.RxFieldValueView;
import ru.android.childdiary.presentation.core.events.BaseAddItemActivity;
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

public class AddMedicineTakingActivity extends BaseAddItemActivity<AddMedicineTakingView, MedicineTaking>
        implements AddMedicineTakingView {
    @InjectPresenter
    AddMedicineTakingPresenter presenter;

    @BindView(R.id.medicineView)
    FieldMedicineView medicineView;

    @BindView(R.id.medicineMeasureValueView)
    FieldMedicineMeasureValueView medicineMeasureValueView;

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
                                   @Nullable LocalTime startTime, @Nullable LocalTime finishTime) {
        Intent intent = new Intent(context, AddMedicineTakingActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_DEFAULT_ITEM, medicineTaking);
        intent.putExtra(ExtraConstants.EXTRA_START_TIME, startTime);
        intent.putExtra(ExtraConstants.EXTRA_FINISH_TIME, finishTime);
        return intent;
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        changeThemeIfNeeded(defaultItem.getChild());

        unsubscribeOnDestroy(getPresenter().listenForDoneButtonUpdate(
                RxFieldValueView.valueChangeEvents(medicineView),
                RxFieldValueView.valueChangeEvents(checkBoxView),
                repeatParametersView.linearGroupsObservable(),
                repeatParametersView.periodicityTypeObservable(),
                repeatParametersView.lengthValueObservable()
        ));
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.add_medicine_taking_title);
    }

    @Override
    protected AddMedicineTakingPresenter getPresenter() {
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
        medicineMeasureValueView.setValue(medicineMeasureValue);
        repeatParametersView.setRepeatParameters(item.getRepeatParameters());
        WidgetsUtils.setDateTime(item.getDateTime(), dateView, timeView);

        boolean exported = ObjectUtils.isTrue(item.getIsExported());
        checkBoxView.setChecked(exported);

        notifyTimeView.setValue(item.getNotifyTimeInMinutes());
        boolean notifyTimeViewVisible = ObjectUtils.isPositive(item.getNotifyTimeInMinutes());
        notifyTimeView.setVisibility(notifyTimeViewVisible ? View.VISIBLE : View.GONE);
        noteWithPhotoView.setText(item.getNote());
        noteWithPhotoView.setImageFileName(item.getImageFileName());
    }

    @Override
    protected MedicineTaking build() {
        Medicine medicine = medicineView.getValue();
        MedicineMeasureValue medicineMeasureValue = medicineMeasureValueView.getValue();
        Double amount = medicineMeasureValue == null ? null : medicineMeasureValue.getAmount();
        MedicineMeasure medicineMeasure = medicineMeasureValue == null ? null : medicineMeasureValue.getMedicineMeasure();
        RepeatParameters repeatParameters = repeatParametersView.getRepeatParameters();
        DateTime dateTime = WidgetsUtils.getDateTime(dateView, timeView);
        boolean exported = checkBoxView.isChecked();
        Integer minutes = notifyTimeView.getValue();
        String note = noteWithPhotoView.getText();
        String imageFileName = noteWithPhotoView.getImageFileName();

        return defaultItem.toBuilder()
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
        if (getCheckBoxView() != null) {
            boolean readOnly = !getCheckBoxView().isChecked();
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

    @Override
    protected FieldMedicineMeasureValueView getMedicineMeasureValueView() {
        return medicineMeasureValueView;
    }

    @Nullable
    @Override
    protected FieldNoteWithPhotoView getNoteWithPhotoView() {
        return noteWithPhotoView;
    }

    @Override
    protected void validationStarted() {
        unsubscribeOnDestroy(getPresenter().listenForFieldsUpdate(
                RxFieldValueView.valueChangeEvents(medicineView),
                RxFieldValueView.valueChangeEvents(checkBoxView),
                repeatParametersView.linearGroupsObservable(),
                repeatParametersView.periodicityTypeObservable(),
                repeatParametersView.lengthValueObservable()
        ));
    }
}
