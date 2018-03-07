package ru.android.healthvector.presentation.testing.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.DomanTestParameter;
import ru.android.healthvector.domain.development.testing.data.tests.core.Question;
import ru.android.healthvector.domain.development.testing.data.tests.core.Test;
import ru.android.healthvector.presentation.core.AppPartitionFragment;
import ru.android.healthvector.presentation.core.BaseMvpActivity;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.core.fields.widgets.FieldJustifiedTextView;
import ru.android.healthvector.presentation.testing.TestingController;
import ru.android.healthvector.utils.strings.TestUtils;
import ru.android.healthvector.utils.ui.ResourcesUtils;

public class TestingQuestionFragment extends AppPartitionFragment {
    @BindView(R.id.descriptionView)
    FieldJustifiedTextView justifiedTextView;

    @BindView(R.id.buttonYes)
    Button buttonYes;

    @BindView(R.id.buttonNo)
    Button buttonNo;

    @Nullable
    private TestingController testingController;
    private Question question;
    private Test test;
    @Nullable
    private DomanTestParameter parameter;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_testing_question;
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
        TestingQuestionArguments arguments = getArguments() == null ? null
                : (TestingQuestionArguments) getArguments().getSerializable(ExtraConstants.EXTRA_APP_PARTITION_ARGUMENTS);
        if (arguments == null) {
            logger.error("no arguments provided");
            return;
        }
        question = arguments.getQuestion();
        test = arguments.getTest();
        parameter = arguments.getParameter();
    }

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
        justifiedTextView.setText(question.getText());
        if (getActivity() == null) {
            logger.error("activity is null");
            return;
        }
        ((BaseMvpActivity) getActivity()).setupToolbarTitle(TestUtils.getTestTitle(getContext(), test, parameter));
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        buttonYes.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), true));
        buttonNo.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), true));
    }

    @OnClick(R.id.buttonYes)
    void onButtonYesClick() {
        if (testingController != null) {
            testingController.answerYes();
        }
    }

    @OnClick(R.id.buttonNo)
    void onButtonNoClick() {
        if (testingController != null) {
            testingController.answerNo();
        }
    }
}
