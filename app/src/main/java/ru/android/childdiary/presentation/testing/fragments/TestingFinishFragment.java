package ru.android.childdiary.presentation.testing.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.widgets.FieldJustifiedTextView;
import ru.android.childdiary.presentation.testing.TestingController;
import ru.android.childdiary.utils.strings.TestUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class TestingFinishFragment extends AppPartitionFragment {
    @BindView(R.id.descriptionView)
    FieldJustifiedTextView justifiedTextView;

    @BindView(R.id.buttonClose)
    Button buttonClose;

    @Nullable
    private TestingController testingController;
    private String text;
    @Nullable
    private DomanTestParameter parameter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_testing_finish;
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
        TestingFinishArguments arguments = (TestingFinishArguments) getArguments().getSerializable(ExtraConstants.EXTRA_APP_PARTITION_ARGUMENTS);
        if (arguments == null) {
            logger.error("no arguments provided");
            return;
        }
        text = arguments.getText();
        parameter = arguments.getParameter();
    }

    @Override
    protected void setupUi() {
        if (parameter != null) {
            ((BaseMvpActivity) getActivity()).setupToolbarTitle(TestUtils.toString(getContext(), parameter));
        }
        justifiedTextView.setText(text);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        buttonClose.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), true));
    }

    @OnClick(R.id.buttonClose)
    void onButtonCloseClick() {
        if (testingController != null) {
            testingController.stopTesting();
        }
    }
}
