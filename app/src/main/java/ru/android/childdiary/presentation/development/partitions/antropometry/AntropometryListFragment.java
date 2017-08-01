package ru.android.childdiary.presentation.development.partitions.antropometry;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindDimen;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.antropometry.Antropometry;
import ru.android.childdiary.presentation.chart.antropometry.AntropometryChartActivity;
import ru.android.childdiary.presentation.core.adapters.decorators.DividerItemDecoration;
import ru.android.childdiary.presentation.development.partitions.antropometry.adapters.AntropometryActionListener;
import ru.android.childdiary.presentation.development.partitions.antropometry.adapters.AntropometryAdapter;
import ru.android.childdiary.presentation.development.partitions.antropometry.edit.EditAntropometryActivity;
import ru.android.childdiary.presentation.development.partitions.core.BaseDevelopmentDiaryFragment;
import ru.android.childdiary.presentation.development.partitions.core.ChartContainer;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class AntropometryListFragment extends BaseDevelopmentDiaryFragment<AntropometryListView>
        implements AntropometryListView, ChartContainer, AntropometryActionListener {
    @BindDimen(R.dimen.divider_padding)
    int DIVIDER_PADDING;

    @Getter
    @InjectPresenter
    AntropometryListPresenter presenter;

    @Getter
    AntropometryAdapter adapter;

    @Override
    protected void setupUi() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        Drawable divider = ContextCompat.getDrawable(getContext(), R.drawable.divider);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(divider, DIVIDER_PADDING);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new AntropometryAdapter(getContext(), this, fabController);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        textViewIntention.setVisibility(View.GONE);
        textViewIntention.setText(R.string.add_medicine_taking);

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        recyclerViewChips.setVisibility(View.GONE);

        line.setVisibility(View.GONE);
    }

    @Override
    public void showAntropometryListState(@NonNull AntropometryListState state) {
        progressBar.setVisibility(View.GONE);

        Child child = state.getChild();
        showChild(child);

        List<Antropometry> antropometryList = state.getAntropometryList();
        adapter.setItems(antropometryList);
        adapter.setFabController(child.getId() == null ? null : fabController);
        recyclerView.setVisibility(antropometryList.isEmpty() ? View.GONE : View.VISIBLE);

        line.setVisibility(antropometryList.isEmpty() ? View.GONE : View.VISIBLE);
        textViewIntention.setVisibility(antropometryList.isEmpty() ? View.VISIBLE : View.GONE);
        textViewIntention.setText(R.string.no_antropometry_data);
    }

    @Override
    public void navigateToAntropometry(@NonNull Child child, @NonNull Antropometry antropometry) {
        Intent intent = EditAntropometryActivity.getIntent(getContext(), child, antropometry);
        startActivity(intent);
    }

    @Override
    public void navigateToChart(@NonNull Child child) {
        Intent intent = AntropometryChartActivity.getIntent(getContext(), child);
        startActivity(intent);
    }

    @Override
    public void noChildSpecified() {
        showToast(getString(R.string.no_child_specified));
    }

    @Override
    public void noChartData() {
        showToast(getString(R.string.no_antropometry_data));
    }

    @Override
    public void deleted(@NonNull Antropometry antropometry) {
    }

    @Override
    public void confirmDelete(@NonNull Antropometry antropometry) {
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.delete_antropometry_dialog_title)
                .setPositiveButton(R.string.delete,
                        (dialog, which) -> presenter.forceDelete(antropometry))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void showChart() {
        presenter.showChart();
    }

    @Override
    public void delete(Antropometry item) {
        presenter.delete(item);
    }

    @Override
    public void edit(Antropometry item) {
        presenter.edit(item);
    }
}
