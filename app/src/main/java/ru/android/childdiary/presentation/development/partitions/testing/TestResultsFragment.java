package ru.android.childdiary.presentation.development.partitions.testing;

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

import org.joda.time.LocalDate;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.TestResult;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.core.adapters.decorators.DividerItemDecoration;
import ru.android.childdiary.presentation.development.partitions.core.ChartContainer;
import ru.android.childdiary.presentation.development.partitions.testing.adapters.result.TestResultActionListener;
import ru.android.childdiary.presentation.development.partitions.testing.adapters.result.TestResultAdapter;
import ru.android.childdiary.presentation.development.partitions.testing.adapters.test.TestAdapter;
import ru.android.childdiary.presentation.development.partitions.testing.adapters.test.TestClickListener;
import ru.android.childdiary.presentation.testing.TestResultActivity;
import ru.android.childdiary.presentation.testing.TestingActivity;
import ru.android.childdiary.presentation.testing.dynamic.TestChartActivity;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class TestResultsFragment extends AppPartitionFragment implements TestResultsView,
        TestClickListener, TestResultActionListener, ChartContainer {
    @InjectPresenter
    TestResultsPresenter presenter;

    @BindDimen(R.dimen.divider_padding)
    int DIVIDER_PADDING;

    @BindView(R.id.recyclerViewTests)
    RecyclerView recyclerViewTests;

    @BindView(R.id.line)
    View line;

    @BindView(R.id.recyclerViewTestResults)
    RecyclerView recyclerViewTestResults;

    private TestAdapter testAdapter;
    private TestResultAdapter testResultAdapter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_test_results;
    }

    @Override
    protected void setupUi() {
        LinearLayoutManager layoutManagerTests = new LinearLayoutManager(getContext());
        recyclerViewTests.setLayoutManager(layoutManagerTests);

        LinearLayoutManager layoutManagerTestResults = new LinearLayoutManager(getContext());
        recyclerViewTestResults.setLayoutManager(layoutManagerTestResults);

        recyclerViewTests.addItemDecoration(getItemDecoration());
        recyclerViewTestResults.addItemDecoration(getItemDecoration());

        testAdapter = new TestAdapter(getContext(), this);
        recyclerViewTests.setAdapter(testAdapter);

        testResultAdapter = new TestResultAdapter(getContext(), this, null);
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
    public void noChartData() {
        showToast(getString(R.string.no_chart_data));
    }

    @Override
    public void confirmDeletion(@NonNull TestResult testResult) {
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

    private RecyclerView.ItemDecoration getItemDecoration() {
        Drawable divider = ContextCompat.getDrawable(getContext(), R.drawable.divider);
        return new DividerItemDecoration(divider, DIVIDER_PADDING);
    }

    @Override
    public void showChart() {
        presenter.showChart();
    }
}