package ru.android.childdiary.presentation.development.partitions.testing;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.development.testing.Test;
import ru.android.childdiary.domain.interactors.development.testing.TestResult;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.core.adapters.decorators.DividerItemDecoration;
import ru.android.childdiary.presentation.development.partitions.testing.adapters.result.TestResultActionListener;
import ru.android.childdiary.presentation.development.partitions.testing.adapters.result.TestResultAdapter;
import ru.android.childdiary.presentation.development.partitions.testing.adapters.test.TestAdapter;
import ru.android.childdiary.presentation.development.partitions.testing.adapters.test.TestClickListener;

public class TestResultFragment extends AppPartitionFragment implements TestResultView,
        TestClickListener, TestResultActionListener {
    @InjectPresenter
    TestResultPresenter presenter;

    @BindDimen(R.dimen.divider_padding)
    int DIVIDER_PADDING;

    @BindView(R.id.testsTitle)
    View testsTitle;

    @BindView(R.id.recyclerViewTests)
    RecyclerView recyclerViewTests;

    @BindView(R.id.testResultsTitle)
    View testResultsTitle;

    @BindView(R.id.recyclerViewTestResults)
    RecyclerView recyclerViewTestResults;

    private TestAdapter testAdapter;
    private TestResultAdapter testResultAdapter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_testing;
    }

    @Override
    protected void setupUi() {
        TextView textView;

        textView = ButterKnife.findById(testsTitle, R.id.textView);
        textView.setText(R.string.tests);

        textView = ButterKnife.findById(testResultsTitle, R.id.textView);
        textView.setText(R.string.test_results);

        LinearLayoutManager layoutManagerTests = new LinearLayoutManager(getContext());
        recyclerViewTests.setLayoutManager(layoutManagerTests);

        LinearLayoutManager layoutManagerTestResults = new LinearLayoutManager(getContext());
        recyclerViewTestResults.setLayoutManager(layoutManagerTestResults);

        Drawable divider = ContextCompat.getDrawable(getContext(), R.drawable.divider);
        RecyclerView.ItemDecoration dividerItemDecorationTests = new DividerItemDecoration(divider, DIVIDER_PADDING);
        recyclerViewTests.addItemDecoration(dividerItemDecorationTests);
        RecyclerView.ItemDecoration dividerItemDecorationTestResults = new DividerItemDecoration(divider, DIVIDER_PADDING);
        recyclerViewTestResults.addItemDecoration(dividerItemDecorationTestResults);

        testAdapter = new TestAdapter(getContext(), this);
        recyclerViewTests.setAdapter(testAdapter);

        testResultAdapter = new TestResultAdapter(getContext(), this, null);
        recyclerViewTestResults.setAdapter(testResultAdapter);

        ViewCompat.setNestedScrollingEnabled(recyclerViewTests, false);
        ViewCompat.setNestedScrollingEnabled(recyclerViewTestResults, false);
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
    }

    @Override
    public void showTestDetails(@NonNull Test test) {

    }

    @Override
    public void delete(TestResult item) {

    }

    @Override
    public void edit(TestResult item) {

    }
}
