package ru.android.childdiary.presentation.testing.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.development.testing.data.tests.core.Test;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.widgets.FieldJustifiedTextView;
import ru.android.childdiary.presentation.testing.TestingController;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class TestingStartFragment extends AppPartitionFragment {
    @BindView(R.id.descriptionView)
    FieldJustifiedTextView justifiedTextView;

    @BindView(R.id.buttonStartTesting)
    Button buttonStartTesting;

    @Nullable
    private TestingController testingController;
    private Test test;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_testing_start;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TestingController) {
            testingController = (TestingController) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        testingController = null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        TestingStartArguments arguments = (TestingStartArguments) getArguments().getSerializable(ExtraConstants.EXTRA_APP_PARTITION_ARGUMENTS);
        if (arguments == null) {
            logger.error("no arguments provided");
            return;
        }
        test = arguments.getTest();
    }

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
        justifiedTextView.setText(test.getDescription());
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        buttonStartTesting.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), true));
    }

    @OnClick(R.id.buttonStartTesting)
    void onButtonStartTestingClick() {
        if (testingController != null) {
            testingController.startTesting();
        }
    }
}
