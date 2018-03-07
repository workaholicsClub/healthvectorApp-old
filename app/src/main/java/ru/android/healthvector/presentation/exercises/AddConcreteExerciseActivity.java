package ru.android.healthvector.presentation.exercises;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
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
import ru.android.healthvector.R;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.calendar.data.core.RepeatParameters;
import ru.android.healthvector.domain.exercises.data.ConcreteExercise;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.core.events.BaseAddItemActivity;
import ru.android.healthvector.presentation.core.fields.widgets.FieldCheckBoxView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldDateView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldDoctorView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldDurationView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldEditTextView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldEditTextWithImageView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldMedicineMeasureValueView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldMedicineView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldNoteWithPhotoView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldNotifyTimeView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldRepeatParametersView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldTimeView;
import ru.android.healthvector.utils.ObjectUtils;
import ru.android.healthvector.utils.ui.ResourcesUtils;
import ru.android.healthvector.utils.ui.WidgetsUtils;

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
        return new Intent(context, AddConcreteExerciseActivity.class)
                .putExtra(ExtraConstants.EXTRA_DEFAULT_ITEM, defaultConcreteExercise)
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
    @LayoutRes
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
        boolean notifyTimeViewVisible = item.getNotifyTimeInMinutes() != null;
        notifyTimeView.setVisibility(notifyTimeViewVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    protected ConcreteExercise build() {
        String name = concreteExerciseNameView.getText();
        RepeatParameters repeatParameters = repeatParametersView.getRepeatParameters();
        Integer duration = durationView.getValue();
        DateTime dateTime = WidgetsUtils.getDateTime(dateView, timeView);
        Integer minutes = notifyTimeView.getValue();

        return defaultItem.toBuilder()
                .repeatParameters(repeatParameters)
                .name(name)
                .durationInMinutes(duration)
                .dateTime(dateTime)
                .isExported(true)
                .notifyTimeInMinutes(minutes)
                .build();
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
