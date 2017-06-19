package ru.android.childdiary.presentation.exercises;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.exercises.Exercise;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.core.adapters.swipe.ItemActionListener;
import ru.android.childdiary.presentation.exercises.adapters.ExerciseAdapter;

public class ExercisesFragment extends AppPartitionFragment implements ExercisesView, ItemActionListener<Exercise> {
    @BindView(R.id.textViewIntention)
    protected TextView textViewIntention;

    @BindView(R.id.recyclerViewChips)
    protected RecyclerView recyclerViewChips;

    @BindView(R.id.line)
    protected View line;

    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;

    @InjectPresenter
    ExercisesPresenter presenter;

    @Getter
    private ExerciseAdapter adapter;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_app_partition_sub_list;
    }

    @Override
    protected void setupUi() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ExerciseAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.GONE);
        textViewIntention.setVisibility(View.GONE);
        textViewIntention.setText(R.string.no_exercises);

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        recyclerViewChips.setVisibility(View.GONE);

        line.setVisibility(View.GONE);
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

        adapter.setItems(exercises);
        recyclerView.setVisibility(exercises.isEmpty() ? View.GONE : View.VISIBLE);

        line.setVisibility(exercises.isEmpty() ? View.GONE : View.VISIBLE);
        textViewIntention.setVisibility(exercises.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void navigateToExercise(@NonNull ExerciseDetailState state) {
        Intent intent = ExerciseDetailActivity.getIntent(getContext(), state);
        startActivity(intent);
    }

    @Override
    public void delete(Exercise item) {
    }

    @Override
    public void edit(Exercise item) {
        presenter.showExerciseDetails(item);
    }
}
