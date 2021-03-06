package ru.android.healthvector.presentation.development.partitions.testing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.joda.time.LocalDate;

import java.util.List;

import butterknife.BindView;
import ru.android.healthvector.R;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.testing.data.TestResult;
import ru.android.healthvector.domain.development.testing.data.tests.core.Test;
import ru.android.healthvector.presentation.chart.testing.TestChartActivity;
import ru.android.healthvector.presentation.core.AppPartitionFragment;
import ru.android.healthvector.presentation.core.adapters.decorators.DividerItemDecoration;
import ru.android.healthvector.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.healthvector.presentation.development.partitions.core.ChartContainer;
import ru.android.healthvector.presentation.development.partitions.testing.adapters.result.TestResultActionListener;
import ru.android.healthvector.presentation.development.partitions.testing.adapters.result.TestResultAdapter;
import ru.android.healthvector.presentation.development.partitions.testing.adapters.test.TestAdapter;
import ru.android.healthvector.presentation.development.partitions.testing.adapters.test.TestClickListener;
import ru.android.healthvector.presentation.profile.ProfileEditActivity;
import ru.android.healthvector.presentation.testing.TestResultActivity;
import ru.android.healthvector.presentation.testing.TestingActivity;
import ru.android.healthvector.utils.ui.ThemeUtils;

public class TestResultsFragment extends AppPartitionFragment implements TestResultsView,
        TestClickListener, TestResultActionListener, ChartContainer {
    @InjectPresenter
    TestResultsPresenter presenter;

    @BindView(R.id.recyclerViewTests)
    RecyclerView recyclerViewTests;

    @BindView(R.id.line)
    View line;

    @BindView(R.id.recyclerViewTestResults)
    RecyclerView recyclerViewTestResults;

    private TestAdapter testAdapter;
    private TestResultAdapter testResultAdapter;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_test_results;
    }

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
        LinearLayoutManager layoutManagerTests = new LinearLayoutManager(getContext());
        recyclerViewTests.setLayoutManager(layoutManagerTests);

        LinearLayoutManager layoutManagerTestResults = new LinearLayoutManager(getContext());
        recyclerViewTestResults.setLayoutManager(layoutManagerTestResults);

        testAdapter = new TestAdapter(getContext(), this);
        testResultAdapter = new TestResultAdapter(getContext(), this, null);

        recyclerViewTests.addItemDecoration(getItemDecoration(testAdapter));
        recyclerViewTestResults.addItemDecoration(getItemDecoration(testResultAdapter));

        recyclerViewTests.setAdapter(testAdapter);
        recyclerViewTestResults.setAdapter(testResultAdapter);

        ViewCompat.setNestedScrollingEnabled(recyclerViewTests, false);
        ViewCompat.setNestedScrollingEnabled(recyclerViewTestResults, false);

        line.setVisibility(View.GONE);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        testAdapter.setSex(getSex());
        testResultAdapter.setSex(getSex());
    }

    @Override
    public void showTests(@NonNull List<Test> tests) {
        testAdapter.setItems(tests);
    }

    @Override
    public void showTestResults(@NonNull List<TestResult> testResults) {
        testResultAdapter.setItems(testResults);
        int visibility = testResults.isEmpty() ? View.GONE : View.VISIBLE;
        recyclerViewTestResults.setVisibility(visibility);
        line.setVisibility(visibility);
    }

    @Override
    public void navigateToTest(@NonNull Test test, @NonNull Child child, @NonNull LocalDate date) {
        Intent intent = TestingActivity.getIntent(getContext(), test, child, date);
        startActivity(intent);
    }

    @Override
    public void navigateToTestResult(@NonNull TestResult testResult) {
        Intent intent = TestResultActivity.getIntent(getContext(), getSex(), testResult);
        startActivity(intent);
    }

    @Override
    public void navigateToChart(@NonNull Child child) {
        Intent intent = TestChartActivity.getIntent(getContext(), child);
        startActivity(intent);
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
    public void noChartData() {
        showToast(getString(R.string.no_doman_test_data));
    }

    @Override
    public void confirmDeletion(@NonNull TestResult testResult) {
        if (getContext() == null) {
            logger.error("context is null");
            return;
        }
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.delete_test_result_dialog_title)
                .setPositiveButton(R.string.delete,
                        (dialog, which) -> presenter.deletionConfirmed(testResult))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void deleted(@NonNull TestResult testResult) {
    }

    @Override
    public void showTestDetails(@NonNull Test test) {
        presenter.showTestDetails(test);
    }

    @Override
    public void delete(TestResult item) {
        presenter.deleteTestResult(item);
    }

    @Override
    public void edit(TestResult item) {
        presenter.reviewTestResult(item);
    }

    private RecyclerView.ItemDecoration getItemDecoration(BaseRecyclerViewAdapter adapter) {
        return new DividerItemDecoration(getContext(), adapter);
    }

    @Override
    public void showChart() {
        presenter.showChart();
    }

    @Override
    public void navigateToProfileAdd() {
        Intent intent = ProfileEditActivity.getIntent(getContext(), null);
        startActivity(intent);
    }
}
