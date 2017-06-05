package ru.android.childdiary.presentation.exercises;

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
import ru.android.childdiary.presentation.exercises.adapters.ExerciseAdapter;

public class ExercisesFragment extends AppPartitionFragment implements ExercisesView {
    @InjectPresenter
    ExercisesPresenter presenter;

    @BindView(R.id.textViewIntention)
    protected TextView textViewIntention;

    @BindView(R.id.recyclerViewChips)
    protected RecyclerView recyclerViewChips;

    @BindView(R.id.line)
    protected View line;

    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;

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
        adapter = new ExerciseAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.GONE);
        textViewIntention.setVisibility(View.GONE);
        textViewIntention.setText(R.string.add_doctor_visit);

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
    public void showExercises(@NonNull List<Exercise> exercises) {
        if (exercises.isEmpty()) {
            // TODO
            showToast("error");
        } else {
            adapter.setItems(exercises);
            recyclerView.setVisibility(exercises.isEmpty() ? View.GONE : View.VISIBLE);

            textViewIntention.setVisibility(exercises.isEmpty() ? View.VISIBLE : View.GONE); // TODO
            textViewIntention.setText(exercises.isEmpty() ? R.string.add_doctor_visit : R.string.nothing_found); // TODO
        }
    }

    @Override
    public void navigateToExercise(@NonNull Exercise exercise) {
        // TODO
    }

    @Override
    public void navigateToExerciseExport(@NonNull Exercise exercise) {
        // TODO
    }
}
