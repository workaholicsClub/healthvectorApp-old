package ru.android.healthvector.presentation.testing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.development.testing.data.TestResult;
import ru.android.healthvector.presentation.core.AppPartitionArguments;
import ru.android.healthvector.presentation.core.BaseMvpActivity;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.testing.fragments.TestingFinishArguments;
import ru.android.healthvector.presentation.testing.fragments.TestingFinishFragment;
import ru.android.healthvector.utils.ui.FragmentAnimationUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;

public class TestResultActivity extends BaseMvpActivity implements TestResultView {
    @IdRes
    private static final int FRAGMENT_CONTAINER_ID = R.id.mainContent;

    @InjectPresenter
    TestResultPresenter presenter;

    private TestResult testResult;

    public static Intent getIntent(Context context,
                                   @Nullable Sex sex,
                                   @NonNull TestResult testResult) {
        return new Intent(context, TestResultActivity.class)
                .putExtra(ExtraConstants.EXTRA_SEX, sex)
                .putExtra(ExtraConstants.EXTRA_TEST_RESULT, testResult);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        testResult = (TestResult) getIntent().getSerializableExtra(ExtraConstants.EXTRA_TEST_RESULT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_one_fragment);

        if (savedInstanceState == null) {
            presenter.initTestResult(testResult);
        }
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
    }

    @Override
    public void showFinish(@NonNull TestingFinishArguments arguments) {
        showAppPartition(new TestingFinishFragment(), arguments, null);
        if (arguments.isInvalidResults()) {
            warnAboutInvalidResults();
        }
    }

    private void warnAboutInvalidResults() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.birthday_changed)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private void showAppPartition(@NonNull Fragment fragment,
                                  @NonNull AppPartitionArguments arguments,
                                  @Nullable Boolean forward) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ExtraConstants.EXTRA_APP_PARTITION_ARGUMENTS, arguments);
        fragment.setArguments(bundle);
        FragmentAnimationUtils.slideTo(fragment, FRAGMENT_CONTAINER_ID, getSupportFragmentManager(), forward);
    }

    @Override
    public void confirmDeletion(@NonNull TestResult testResult) {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.delete_test_result_dialog_title)
                .setPositiveButton(R.string.delete,
                        (dialog, which) -> presenter.deletionConfirmed(testResult))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void deleted(@NonNull TestResult testResult) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        removeToolbarMargin();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                presenter.deleteTestResult(testResult);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
