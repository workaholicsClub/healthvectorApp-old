package ru.android.childdiary.presentation.testing.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.domain.development.testing.data.tests.core.Question;
import ru.android.childdiary.domain.development.testing.data.tests.core.Test;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.widgets.FieldJustifiedTextView;
import ru.android.childdiary.presentation.testing.TestingController;
import ru.android.childdiary.utils.strings.TestUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;

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
        TestingQuestionArguments arguments = (TestingQuestionArguments) getArguments().getSerializable(ExtraConstants.EXTRA_APP_PARTITION_ARGUMENTS);
        if (arguments == null) {
            logger.error("no arguments provided");
            return;
        }
        question = arguments.getQuestion();
        test = arguments.getTest();
        parameter = arguments.getParameter();
    }

    @Override
    protected void setupUi() {
        ((BaseMvpActivity) getActivity()).setupToolbarTitle(TestUtils.getTestTitle(getContext(), test, parameter));
        justifiedTextView.setText(question.getText());
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
