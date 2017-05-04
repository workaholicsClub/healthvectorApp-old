package ru.android.childdiary.presentation.medical;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.events.BaseAddItemActivity;
import ru.android.childdiary.presentation.core.events.BaseAddItemPresenter;
import ru.android.childdiary.presentation.core.fields.widgets.FieldCheckBoxView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDateView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDurationView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldEditTextView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldMedicineMeasureView;
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

    @BindView(R.id.medicineMeasureView)
    FieldMedicineMeasureView medicineMeasureView;

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

    public static Intent getIntent(Context context, @NonNull MedicineTaking medicineTaking) {
        Intent intent = new Intent(context, AddMedicineTakingActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_DEFAULT_ITEM, medicineTaking);
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
    }

    @Override
    public void showMedicineMeasureList(List<MedicineMeasure> medicineMeasureList) {
        medicineMeasureView.updateAdapter(medicineMeasureList);
    }

    @Override
    protected BaseAddItemPresenter<AddMedicineTakingView, MedicineTaking> getPresenter() {
        return presenter;
    }

    @Override
    protected int getContentLayoutResourceId() {
        return R.layout.activity_add_item_medicine_taking;
    }

    @Override
    protected void setup(MedicineTaking item) {
        medicineView.setValue(item.getMedicine());
        // TODO amount
        medicineMeasureView.setValue(item.getMedicineMeasure());
        // TODO repeatParameters
        WidgetsUtils.setDateTime(item.getDateTime(), dateView, timeView);
        notifyTimeView.setValue(item.getNotifyTimeInMinutes());
        boolean notifyTimeViewVisible = ObjectUtils.isPositive(item.getNotifyTimeInMinutes());
        notifyTimeView.setVisibility(notifyTimeViewVisible ? View.VISIBLE : View.GONE);
        noteWithPhotoView.setText(item.getNote());
        // TODO image file name
    }

    @Override
    protected MedicineTaking build() {
        Medicine medicine = medicineView.getValue();
        Double amount = null;
        MedicineMeasure medicineMeasure = medicineMeasureView.getValue();
        RepeatParameters repeatParameters = null;
        DateTime dateTime = WidgetsUtils.getDateTime(dateView, timeView);
        Integer minutes = notifyTimeView.getValue();
        String note = noteWithPhotoView.getText();
        String imageFileName = null;

        return defaultItem.toBuilder()
                .medicine(medicine)
                .amount(amount)
                .medicineMeasure(medicineMeasure)
                .repeatParameters(repeatParameters)
                .dateTime(dateTime)
                .notifyTimeInMinutes(minutes)
                .note(note)
                .imageFileName(imageFileName)
                .build();
    }

    @Override
    protected boolean contentEquals(MedicineTaking item1, MedicineTaking item2) {
        return false;
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
        return Arrays.asList(noteWithPhotoView);
    }
}
