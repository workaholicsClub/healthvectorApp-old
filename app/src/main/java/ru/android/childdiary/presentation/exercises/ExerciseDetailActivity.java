package ru.android.childdiary.presentation.exercises;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.exercises.Exercise;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.widgets.FieldExerciseDescriptionView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldExerciseNameView;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class ExerciseDetailActivity extends BaseMvpActivity implements ExerciseDetailView {
    @BindView(R.id.exerciseNameView)
    FieldExerciseNameView exerciseNameView;

    @BindView(R.id.exerciseDescriptionView)
    FieldExerciseDescriptionView exerciseDescriptionView;

    @BindView(R.id.buttonAdd)
    Button buttonAdd;

    @InjectPresenter
    ExerciseDetailPresenter presenter;

    public static Intent getIntent(Context context, @NonNull Exercise exercise) {
        return new Intent(context, ExerciseDetailActivity.class)
                .putExtra(ExtraConstants.EXTRA_EXERCISE, exercise);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Exercise exercise = (Exercise) getIntent().getSerializableExtra(ExtraConstants.EXTRA_EXERCISE);
        presenter.setExercise(exercise);
        showExercise(exercise);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
        setupToolbarTitle(R.string.exercise);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), true));
    }

    @Override
    public void showExercise(@NonNull Exercise exercise) {
        exerciseNameView.setText(exercise.getName());
        exerciseDescriptionView.setText(exercise.getDescription());
    }
}
