package ru.android.childdiary.presentation.testing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.joda.time.LocalDate;

import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.Test;
import ru.android.childdiary.presentation.core.AppPartitionArguments;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.testing.fragments.TestingFinishArguments;
import ru.android.childdiary.presentation.testing.fragments.TestingFinishFragment;
import ru.android.childdiary.presentation.testing.fragments.TestingQuestionArguments;
import ru.android.childdiary.presentation.testing.fragments.TestingQuestionFragment;
import ru.android.childdiary.presentation.testing.fragments.TestingStartArguments;
import ru.android.childdiary.presentation.testing.fragments.TestingStartFragment;
import ru.android.childdiary.utils.ui.ThemeUtils;

import static android.support.v4.app.FragmentTransaction.TRANSIT_UNSET;

public class TestingActivity extends BaseMvpActivity implements TestingView, TestingController {
    @IdRes
    private static final int FRAGMENT_CONTAINER_ID = R.id.mainContent;

    @InjectPresenter
    TestingPresenter presenter;

    public static Intent getIntent(Context context,
                                   @NonNull Test test,
                                   @NonNull Child child,
                                   @NonNull LocalDate date) {
        return new Intent(context, TestingActivity.class)
                .putExtra(ExtraConstants.EXTRA_TEST, test)
                .putExtra(ExtraConstants.EXTRA_CHILD, child)
                .putExtra(ExtraConstants.EXTRA_DATE, date);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        if (savedInstanceState == null) {
            Test test = (Test) getIntent().getSerializableExtra(ExtraConstants.EXTRA_TEST);
            Child child = (Child) getIntent().getSerializableExtra(ExtraConstants.EXTRA_CHILD);
            LocalDate date = (LocalDate) getIntent().getSerializableExtra(ExtraConstants.EXTRA_DATE);
            presenter.initTest(test, child, date);
        }
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_close);
    }

    @Override
    public void showStart(@NonNull TestingStartArguments arguments) {
        showAppPartition(new TestingStartFragment(), arguments, false);
    }

    @Override
    public void showQuestion(@NonNull TestingQuestionArguments arguments) {
        showAppPartition(new TestingQuestionFragment(), arguments, true);
    }

    @Override
    public void showFinish(@NonNull TestingFinishArguments arguments) {
        showAppPartition(new TestingFinishFragment(), arguments, true);
    }

    private void showAppPartition(@NonNull Fragment fragment,
                                  @NonNull AppPartitionArguments arguments,
                                  boolean animate) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ExtraConstants.EXTRA_APP_PARTITION_ARGUMENTS, arguments);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (animate) {
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        } else {
            transaction.setTransition(TRANSIT_UNSET);
        }
        transaction.replace(FRAGMENT_CONTAINER_ID, fragment)
                .commit();
    }

    @Override
    public void showCloseConfirmation() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.stop_testing_confirm_dialog_title)
                .setPositiveButton(R.string.yes,
                        (dialog, which) -> presenter.close())
                .setNegativeButton(R.string.no, null)
                .show();
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void onBackPressed() {
        stopTesting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            stopTesting();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void startTesting() {
        presenter.startTesting();
    }

    @Override
    public void stopTesting() {
        presenter.stopTesting();
    }

    @Override
    public void answerYes() {
        presenter.answerYes();
    }

    @Override
    public void answerNo() {
        presenter.answerNo();
    }
}
