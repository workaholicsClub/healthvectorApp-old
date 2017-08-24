package ru.android.childdiary.presentation.development.partitions.antropometry;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.development.antropometry.data.Antropometry;
import ru.android.childdiary.presentation.chart.antropometry.AntropometryChartActivity;
import ru.android.childdiary.presentation.core.adapters.decorators.DividerItemDecoration;
import ru.android.childdiary.presentation.development.partitions.antropometry.adapters.AntropometryActionListener;
import ru.android.childdiary.presentation.development.partitions.antropometry.adapters.AntropometryAdapter;
import ru.android.childdiary.presentation.development.partitions.antropometry.add.AddAntropometryActivity;
import ru.android.childdiary.presentation.development.partitions.antropometry.edit.EditAntropometryActivity;
import ru.android.childdiary.presentation.development.partitions.core.BaseDevelopmentDiaryFragment;
import ru.android.childdiary.presentation.development.partitions.core.ChartContainer;
import ru.android.childdiary.utils.HtmlUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class AntropometryListFragment extends BaseDevelopmentDiaryFragment<AntropometryListView>
        implements AntropometryListView, ChartContainer, AntropometryActionListener,
        HtmlUtils.OnLinkClickListener {
    private static final String LINK_ADD = "add";

    @Getter
    @InjectPresenter
    AntropometryListPresenter presenter;

    @Getter
    AntropometryAdapter adapter;

    @Override
    protected void setupUi() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AntropometryAdapter(getContext(), this, fabController);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), adapter);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        textViewIntention.setVisibility(View.GONE);
        String text = getString(R.string.link_format,
                LINK_ADD,
                getString(R.string.add_antropometry));
        HtmlUtils.setupClickableLinks(textViewIntention, text, this, ContextCompat.getColor(getContext(), R.color.intention_text));
    }

    @Override
    public void showAntropometryListState(@NonNull AntropometryListState state) {
        progressBar.setVisibility(View.GONE);

        Child child = state.getChild();
        showChild(child);

        List<Antropometry> antropometryList = state.getAntropometryList();
        adapter.setItems(antropometryList);
        adapter.setFabController(fabController);
        recyclerView.setVisibility(antropometryList.isEmpty() ? View.GONE : View.VISIBLE);

        textViewIntention.setVisibility(antropometryList.isEmpty() ? View.VISIBLE : View.GONE);
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
        showToast(getString(R.string.intention_add_child_profile));
    }

    @Override
    public void noChartData() {
        showToast(getString(R.string.add_antropometry_intention));
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

    @Override
    public void onLinkClick(String url) {
        if (LINK_ADD.equals(url)) {
            presenter.addAntropometry();
        }
    }

    @Override
    public void navigateToAntropometryAdd(@NonNull Child child, @NonNull Antropometry antropometry) {
        Intent intent = AddAntropometryActivity.getIntent(getContext(), child, antropometry);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.closeAllItems(); // для одинакового поведения с Мед. данными
    }
}
