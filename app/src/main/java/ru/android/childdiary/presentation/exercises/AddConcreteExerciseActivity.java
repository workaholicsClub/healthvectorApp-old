package ru.android.childdiary.presentation.exercises;

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
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.exercises.ConcreteExercise;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.events.BaseAddItemActivity;
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
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public class AddConcreteExerciseActivity extends BaseAddItemActivity<AddConcreteExerciseView, ConcreteExercise>
        implements AddConcreteExerciseView {
    @InjectPresenter
    AddConcreteExercisePresenter presenter;

    @BindView(R.id.concreteExerciseNameView)
    FieldEditTextWithImageView concreteExerciseNameView;

    @BindView(R.id.dateView)
    FieldDateView dateView;

    @BindView(R.id.timeView)
    FieldTimeView timeView;

    @BindView(R.id.durationView)
    FieldDurationView durationView;

    @BindView(R.id.repeatParametersView)
    FieldRepeatParametersView repeatParametersView;

    @BindView(R.id.notifyTimeView)
    FieldNotifyTimeView notifyTimeView;

    public static Intent getIntent(Context context, @NonNull ConcreteExercise defaultConcreteExercise,
                                   @Nullable LocalTime startTime, @Nullable LocalTime finishTime) {
        Intent intent = new Intent(context, AddConcreteExerciseActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_DEFAULT_ITEM, defaultConcreteExercise);
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
                concreteExerciseNameView.textObservable(),
                repeatParametersView.linearGroupsObservable(),
                repeatParametersView.periodicityTypeObservable(),
                repeatParametersView.lengthValueObservable()
        ));
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.export_to_calendar);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), true));
    }

    @Override
    protected AddConcreteExercisePresenter getPresenter() {
        return presenter;
    }

    @Override
    protected int getContentLayoutResourceId() {
        return R.layout.activity_item_content_exercise;
    }

    @Override
    protected void setup(ConcreteExercise item) {
        concreteExerciseNameView.setText(item.getName());
        repeatParametersView.setRepeatParameters(item.getRepeatParameters());
        durationView.setValue(item.getDurationInMinutes());
        WidgetsUtils.setDateTime(item.getDateTime(), dateView, timeView);
        notifyTimeView.setValue(item.getNotifyTimeInMinutes());
        boolean notifyTimeViewVisible = ObjectUtils.isPositive(item.getNotifyTimeInMinutes());
        notifyTimeView.setVisibility(notifyTimeViewVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    protected ConcreteExercise build() {
        String name = concreteExerciseNameView.getText();
        RepeatParameters repeatParameters = repeatParametersView.getRepeatParameters();
        String concreteExerciseName = concreteExerciseNameView.getText();
        Integer duration = durationView.getValue();
        DateTime dateTime = WidgetsUtils.getDateTime(dateView, timeView);
        Integer minutes = notifyTimeView.getValue();

        return defaultItem.toBuilder()
                .repeatParameters(repeatParameters)
                .name(concreteExerciseName)
                .durationInMinutes(duration)
                .dateTime(dateTime)
                .isExported(true)
                .notifyTimeInMinutes(minutes)
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
    protected boolean contentEquals(ConcreteExercise item1, ConcreteExercise item2) {
        return ObjectUtils.contentEquals(item1, item2);
    }

    @Override
    protected FieldDateView getDateView() {
        return dateView;
    }

    @Nullable
    @Override
    protected FieldTimeView getTimeView() {
        return null;
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
        return null;
    }

    @Override
    protected FieldRepeatParametersView getRepeatParametersView() {
        return repeatParametersView;
    }

    @Override
    protected List<FieldEditTextView> getEditTextViews() {
        return Collections.singletonList(concreteExerciseNameView);
    }

    @Nullable
    @Override
    protected FieldDoctorView getDoctorView() {
        return null;
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
        return null;
    }

    @Override
    public void concreteExerciseNameValidated(boolean valid) {
        concreteExerciseNameView.validated(valid);
    }

    @Override
    protected void validationStarted() {
        unsubscribeOnDestroy(getPresenter().listenForFieldsUpdate(
                concreteExerciseNameView.textObservable(),
                repeatParametersView.linearGroupsObservable(),
                repeatParametersView.periodicityTypeObservable(),
                repeatParametersView.lengthValueObservable()
        ));
    }
}
