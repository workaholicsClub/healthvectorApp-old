package ru.android.childdiary.presentation.testing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.AppPartitionArguments;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.testing.fragments.TestingStartFragment;

import static android.support.v4.app.FragmentTransaction.TRANSIT_UNSET;

public class TestingActivity extends BaseMvpActivity implements TestingView {
    @IdRes
    private static final int FRAGMENT_CONTAINER_ID = R.id.mainContent;

    @InjectPresenter
    TestingPresenter presenter;

    public static Intent getIntent(Context context, @NonNull TestType testType, @NonNull Child child) {
        return new Intent(context, TestingActivity.class)
                .putExtra(ExtraConstants.EXTRA_TYPE, testType)
                .putExtra(ExtraConstants.EXTRA_CHILD, child);
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
            TestType testType = (TestType) getIntent().getSerializableExtra(ExtraConstants.EXTRA_TYPE);
            Child child = (Child) getIntent().getSerializableExtra(ExtraConstants.EXTRA_CHILD);
            presenter.initTest(testType, child);
        }
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_close);
    }

    @Override
    public void showStart(@NonNull AppPartitionArguments arguments) {
        String tag = "Test";

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ExtraConstants.EXTRA_APP_PARTITION_ARGUMENTS, arguments);
            fragment = new TestingStartFragment();
            fragment.setArguments(bundle);
            logger.debug("fragment cache: create new fragment: " + fragment);
        } else {
            logger.debug("fragment cache: show fragment: " + fragment);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(TRANSIT_UNSET)
                .replace(FRAGMENT_CONTAINER_ID, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showQuestion(@NonNull AppPartitionArguments arguments) {

    }

    @Override
    public void showFinish(@NonNull AppPartitionArguments arguments) {

    }
}
