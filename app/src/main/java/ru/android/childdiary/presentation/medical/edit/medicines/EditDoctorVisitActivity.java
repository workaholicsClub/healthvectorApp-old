package ru.android.childdiary.presentation.medical.edit.medicines;

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
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.events.BaseEditItemActivity;
import ru.android.childdiary.presentation.core.events.BaseEditItemPresenter;
import ru.android.childdiary.presentation.core.fields.widgets.FieldCheckBoxView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDateView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDoctorView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDoctorVisitNameView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDurationView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldEditTextView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldNoteWithPhotoView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldNotifyTimeView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldRepeatParametersView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldTimeView;
import ru.android.childdiary.utils.ObjectUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public class EditDoctorVisitActivity extends BaseEditItemActivity<EditDoctorVisitView, DoctorVisit>
        implements EditDoctorVisitView {
    @InjectPresenter
    EditDoctorVisitPresenter presenter;

    @BindView(R.id.doctorVisitNameView)
    FieldDoctorVisitNameView doctorVisitNameView;

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

    public static Intent getIntent(Context context, @NonNull DoctorVisit defaultDoctorVisit) {
        Intent intent = new Intent(context, EditDoctorVisitActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_ITEM, defaultDoctorVisit);
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
    protected BaseEditItemPresenter<EditDoctorVisitView, DoctorVisit> getPresenter() {
        return presenter;
    }

    @Override
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
        boolean notifyTimeViewVisible = ObjectUtils.isPositive(item.getNotifyTimeInMinutes());
        notifyTimeView.setVisibility(notifyTimeViewVisible ? View.VISIBLE : View.GONE);
        noteWithPhotoView.setText(item.getNote());
        // TODO image file name
    }

    @Override
    protected DoctorVisit build() {
        Doctor doctor = doctorView.getValue();
        RepeatParameters repeatParameters = repeatParametersView.getRepeatParameters();
        String doctorVisitName = doctorVisitNameView.getText();
        Integer duration = durationView.getValue();
        DateTime dateTime = WidgetsUtils.getDateTime(dateView, timeView);
        Integer minutes = notifyTimeView.getValue();
        String note = noteWithPhotoView.getText();
        String imageFileName = null;

        return item.toBuilder()
                .doctor(doctor)
                .repeatParameters(repeatParameters)
                .name(doctorVisitName)
                .durationInMinutes(duration)
                .dateTime(dateTime)
                .notifyTimeInMinutes(minutes)
                .note(note)
                .imageFileName(imageFileName)
                .build();
    }

    @Override
    protected boolean contentEquals(DoctorVisit item1, DoctorVisit item2) {
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
        return durationView;
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
        return Arrays.asList(doctorVisitNameView, noteWithPhotoView);
    }
}
