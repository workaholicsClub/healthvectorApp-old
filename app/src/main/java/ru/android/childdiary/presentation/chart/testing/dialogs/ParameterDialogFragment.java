package ru.android.childdiary.presentation.chart.testing.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.domain.interactors.development.testing.data.tests.core.DomanTest;
import ru.android.childdiary.domain.interactors.development.testing.data.tests.core.Test;
import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDomanTestParameterView;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class ParameterDialogFragment extends BaseMvpDialogFragment<ParameterDialogArguments> {
    @BindView(R.id.domanTestParameterView)
    FieldDomanTestParameterView parameterView;

    @Nullable
    private Listener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        } else if (getParentFragment() instanceof Listener) {
            listener = (Listener) getParentFragment();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.dialog_parameter;
    }

    @Override
    protected void setupUi() {
        DomanTestParameter[] parameters = getParameters();
        parameterView.setValues(parameters);
        parameterView.setVisibility(parameters == null || parameters.length == 0 ? View.GONE : View.VISIBLE);
        parameterView.setSelected(dialogArguments.getSelectedParameter());
        parameterView.setSex(dialogArguments.getSex());
    }

    @Nullable
    private DomanTestParameter[] getParameters() {
        Test test = dialogArguments.getTest();
        if (test instanceof DomanTest) {
            return ((DomanTest) test).getParameters();
        }
        return null;
    }

    @NonNull
    @Override
    protected Dialog createDialog(@Nullable View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(view)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    DomanTestParameter parameter = parameterView.getSelected();
                    if (listener != null) {
                        listener.onParameterSet(parameter);
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

    public interface Listener {
        void onParameterSet(@NonNull DomanTestParameter testParameter);
    }
}
