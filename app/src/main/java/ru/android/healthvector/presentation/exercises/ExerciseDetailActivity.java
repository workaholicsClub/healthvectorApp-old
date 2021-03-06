package ru.android.healthvector.presentation.exercises;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.joda.time.LocalTime;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.healthvector.R;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.exercises.data.ConcreteExercise;
import ru.android.healthvector.domain.exercises.data.Exercise;
import ru.android.healthvector.presentation.core.BaseMvpActivity;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.core.fields.widgets.FieldJustifiedTextView;
import ru.android.healthvector.utils.HtmlUtils;
import ru.android.healthvector.utils.ui.ResourcesUtils;

public class ExerciseDetailActivity extends BaseMvpActivity implements ExerciseDetailView,
        HtmlUtils.OnLinkClickListener {
    private static final int REQUEST_ADD_CONCRETE_EXERCISE = 1;

    @BindView(R.id.exerciseDescriptionView)
    FieldJustifiedTextView exerciseDescriptionView;

    @BindView(R.id.buttonAdd)
    Button buttonAdd;

    @InjectPresenter
    ExerciseDetailPresenter presenter;

    public static Intent getIntent(Context context, @NonNull ExerciseDetailState state) {
        return new Intent(context, ExerciseDetailActivity.class)
                .putExtra(ExtraConstants.EXTRA_STATE, state);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        ExerciseDetailState state = (ExerciseDetailState) getIntent().getSerializableExtra(ExtraConstants.EXTRA_STATE);
        changeThemeIfNeeded(state.getChild());
        presenter.setExerciseDetailState(state);
        showExercise(state);

        exerciseDescriptionView.setOnLinkClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_CONCRETE_EXERCISE) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
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

    @OnClick(R.id.buttonAdd)
    void onButtonAddClick() {
        presenter.addConcreteExercise();
    }

    @Override
    public void onLinkClick(String url) {
        presenter.openUrl(url);
    }

    @Override
    public void showExercise(@NonNull ExerciseDetailState state) {
        Child child = state.getChild();
        buttonAdd.setVisibility(child.getId() == null ? View.GONE : View.VISIBLE);
        Exercise exercise = state.getExercise();
        setupToolbarTitle(exercise.getName());
        exerciseDescriptionView.setText(exercise.getDescription());
    }

    @Override
    public void navigateToConcreteExerciseAdd(@NonNull ConcreteExercise defaultConcreteExercise,
                                              @Nullable LocalTime startTime,
                                              @Nullable LocalTime finishTime) {
        Intent intent = AddConcreteExerciseActivity.getIntent(this, defaultConcreteExercise, startTime, finishTime);
        startActivityForResult(intent, REQUEST_ADD_CONCRETE_EXERCISE);
    }

    @Override
    public void navigateToWebBrowser(@NonNull String title, @NonNull String url) {
        Intent intent = WebBrowserActivity.getIntent(this, title, url, getSex());
        startActivity(intent);
    }
}
