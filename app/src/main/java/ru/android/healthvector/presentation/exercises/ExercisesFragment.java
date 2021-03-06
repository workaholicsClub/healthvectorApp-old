package ru.android.healthvector.presentation.exercises;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.joda.time.LocalTime;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import icepick.State;
import lombok.Getter;
import ru.android.healthvector.R;
import ru.android.healthvector.domain.exercises.data.ConcreteExercise;
import ru.android.healthvector.domain.exercises.data.Exercise;
import ru.android.healthvector.presentation.core.AppPartitionFragment;
import ru.android.healthvector.presentation.core.adapters.decorators.DividerItemDecoration;
import ru.android.healthvector.presentation.exercises.adapters.ExerciseAdapter;
import ru.android.healthvector.presentation.exercises.adapters.ExerciseClickListener;
import ru.android.healthvector.presentation.profile.ProfileEditActivity;
import ru.android.healthvector.utils.HtmlUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;

public class ExercisesFragment extends AppPartitionFragment implements ExercisesView,
        ExerciseClickListener, HtmlUtils.OnLinkClickListener {
    private static final String LINK_TRY_AGAIN = "try_again";

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.textViewIntention)
    TextView textViewIntention;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @State
    boolean loading;

    @InjectPresenter
    ExercisesPresenter presenter;

    @BindColor(R.color.intention_text)
    int intentionTextColor;

    @Getter
    private ExerciseAdapter adapter;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_app_partition_with_list;
    }

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
        // setup recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ExerciseAdapter(getContext(), this);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), adapter);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.GONE);

        // setup intention
        imageView.setVisibility(View.GONE);
        textViewIntention.setVisibility(View.GONE);
        String noExercises = getString(R.string.no_exercises);
        String checkNetworkConnection = getString(R.string.check_network_connection);
        String tryAgain = getString(R.string.try_again);
        String text = getString(R.string.no_exercises_format,
                noExercises, checkNetworkConnection, LINK_TRY_AGAIN, tryAgain);
        HtmlUtils.setupClickableLinks(textViewIntention, text, this, intentionTextColor);

        // setup progress
        if (loading) {
            startLoading();
        } else {
            stopLoading();
        }
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        adapter.setSex(getSex());
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.updateData();
    }

    @Override
    public void showExercises(@NonNull List<Exercise> exercises) {
        logger.debug("showExercises: " + exercises);

        // setup recycler view
        adapter.setItems(exercises);
        recyclerView.setVisibility(exercises.isEmpty() ? View.GONE : View.VISIBLE);

        // no chips

        // setup intention
        imageView.setVisibility(exercises.isEmpty() ? View.VISIBLE : View.GONE);
        textViewIntention.setVisibility(exercises.isEmpty() ? View.VISIBLE : View.GONE);

        // setup progress
        if (loading) {
            startLoading();
        } else {
            stopLoading();
        }
    }

    @Override
    public void navigateToExercise(@NonNull ExerciseDetailState state) {
        Intent intent = ExerciseDetailActivity.getIntent(getContext(), state);
        startActivity(intent);
    }

    @Override
    public void navigateToConcreteExerciseAdd(@NonNull ConcreteExercise defaultConcreteExercise,
                                              @Nullable LocalTime startTime,
                                              @Nullable LocalTime finishTime) {
        Intent intent = AddConcreteExerciseActivity.getIntent(getContext(), defaultConcreteExercise, startTime, finishTime);
        startActivity(intent);
    }

    @Override
    public void startLoading() {
        loading = true;
        recyclerView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        textViewIntention.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopLoading() {
        loading = false;
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void noChildSpecified() {
        if (getContext() == null) {
            logger.error("context is null");
            return;
        }
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.add_profile_to_continue)
                .setPositiveButton(R.string.add, (dialog, which) -> presenter.addProfile())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void showExerciseDetails(@NonNull Exercise exercise) {
        presenter.showExerciseDetails(exercise);
    }

    @Override
    public void addConcreteExercise(@NonNull Exercise exercise) {
        presenter.addConcreteExercise(exercise);
    }

    @Override
    public void onLinkClick(String url) {
        if (LINK_TRY_AGAIN.equals(url)) {
            presenter.tryToLoadDataFromNetwork();
        }
    }

    @Override
    public void navigateToProfileAdd() {
        Intent intent = ProfileEditActivity.getIntent(getContext(), null);
        startActivity(intent);
    }
}
