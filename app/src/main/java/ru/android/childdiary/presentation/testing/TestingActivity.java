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
import ru.android.childdiary.domain.interactors.development.testing.processors.core.TestParameters;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;
import ru.android.childdiary.presentation.core.AppPartitionArguments;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.dialogs.AgeDialogArguments;
import ru.android.childdiary.presentation.core.fields.dialogs.AgeDialogFragment;
import ru.android.childdiary.presentation.testing.dialogs.TestParametersDialogArguments;
import ru.android.childdiary.presentation.testing.dialogs.TestParametersDialogFragment;
import ru.android.childdiary.presentation.testing.fragments.TestingFinishArguments;
import ru.android.childdiary.presentation.testing.fragments.TestingFinishFragment;
import ru.android.childdiary.presentation.testing.fragments.TestingQuestionArguments;
import ru.android.childdiary.presentation.testing.fragments.TestingQuestionFragment;
import ru.android.childdiary.presentation.testing.fragments.TestingStartArguments;
import ru.android.childdiary.presentation.testing.fragments.TestingStartFragment;
import ru.android.childdiary.utils.strings.TimeUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

import static android.support.v4.app.FragmentTransaction.TRANSIT_UNSET;

public class TestingActivity extends BaseMvpActivity implements TestingView, TestingController,
        TestParametersDialogFragment.Listener, AgeDialogFragment.Listener {
    private static final String TAG_DATE_AND_PARAMETER_DIALOG = "TAG_DATE_AND_PARAMETER_DIALOG";
    private static final String TAG_DIALOG_AGE_WHEN_THIS_HAPPENED = "TAG_DIALOG_AGE_WHEN_THIS_HAPPENED";

    @IdRes
    private static final int FRAGMENT_CONTAINER_ID = R.id.mainContent;

    @InjectPresenter
    TestingPresenter presenter;

    private Test test;

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
        test = (Test) getIntent().getSerializableExtra(ExtraConstants.EXTRA_TEST);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_one_fragment);

        if (savedInstanceState == null) {
            Child child = (Child) getIntent().getSerializableExtra(ExtraConstants.EXTRA_CHILD);
            LocalDate date = (LocalDate) getIntent().getSerializableExtra(ExtraConstants.EXTRA_DATE);
            presenter.initTest(test, child, date);
        }
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(test.getName());
        toolbar.setNavigationIcon(R.drawable.toolbar_action_close);
    }

    @Override
    public void showStart(@NonNull TestingStartArguments arguments) {
        showAppPartition(new TestingStartFragment(), arguments, null);
    }

    @Override
    public void showQuestion(@NonNull TestingQuestionArguments arguments) {
        showAppPartition(new TestingQuestionFragment(), arguments, arguments.getForward());
    }

    @Override
    public void showFinish(@NonNull TestingFinishArguments arguments) {
        showAppPartition(new TestingFinishFragment(), arguments, true);
    }

    private void showAppPartition(@NonNull Fragment fragment,
                                  @NonNull AppPartitionArguments arguments,
                                  @Nullable Boolean forward) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ExtraConstants.EXTRA_APP_PARTITION_ARGUMENTS, arguments);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (forward == null) {
            transaction.setTransition(TRANSIT_UNSET);
        } else {
            if (forward) {
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            } else {
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
            }
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
    public void specifyTestParameters(@NonNull Child child, @NonNull Test test) {
        TestParametersDialogFragment fragment = new TestParametersDialogFragment();
        fragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_DATE_AND_PARAMETER_DIALOG,
                TestParametersDialogArguments.builder()
                        .sex(getSex())
                        .child(child)
                        .test(test)
                        .build());
    }

    @Override
    public void askWhenThisHappened(@Nullable TimeUtils.Age age) {
        AgeDialogFragment dialogFragment = new AgeDialogFragment();
        dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_DIALOG_AGE_WHEN_THIS_HAPPENED,
                AgeDialogArguments.builder()
                        .sex(getSex())
                        .title(getString(R.string.specify_age_when_this_happened))
                        .maxAge(age)
                        .age(age)
                        .build());
    }

    @Override
    public void onTestParametersSet(@NonNull TestParameters parameters) {
        presenter.onTestParametersSet(parameters);
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

    @Override
    public void onSetAge(String tag, @NonNull TimeUtils.Age age) {
        if (TAG_DIALOG_AGE_WHEN_THIS_HAPPENED.equals(tag)) {
            presenter.specifyAge(age);
        }
    }
}
