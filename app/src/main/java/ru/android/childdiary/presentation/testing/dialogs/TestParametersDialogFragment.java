package ru.android.childdiary.presentation.testing.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.LocalDate;

import java.util.Calendar;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.TestParameters;
import ru.android.childdiary.domain.interactors.development.testing.tests.DomanMentalTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.DomanPhysicalTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.DomanTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;
import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDateView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDomanTestParameterView;
import ru.android.childdiary.presentation.core.widgets.CustomDatePickerDialog;
import ru.android.childdiary.utils.ui.ThemeUtils;

import static ru.android.childdiary.data.types.DomanTestParameter.MENTAL_AUDITION;
import static ru.android.childdiary.data.types.DomanTestParameter.MENTAL_SENSITIVITY;
import static ru.android.childdiary.data.types.DomanTestParameter.MENTAL_VISION;
import static ru.android.childdiary.data.types.DomanTestParameter.PHYSICAL_MANUAL;
import static ru.android.childdiary.data.types.DomanTestParameter.PHYSICAL_MOBILITY;
import static ru.android.childdiary.data.types.DomanTestParameter.PHYSICAL_SPEECH;

public class TestParametersDialogFragment extends BaseMvpDialogFragment<TestParametersDialogArguments>
        implements DatePickerDialog.OnDateSetListener {
    private static final String TAG_DIALOG_DATE = "TAG_DIALOG_DATE";

    @BindView(R.id.dateView)
    FieldDateView dateView;

    @BindView(R.id.domanTestParameterView)
    FieldDomanTestParameterView parameterView;

    @Nullable
    private Listener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.dialog_test_parameters;
    }

    @Override
    protected void setupUi() {
        dateView.setValue(LocalDate.now());
        dateView.setFieldDialogListener(view -> {
            LocalDate date = dateView.getValue();
            // TODO minDate maxDate
            DatePickerDialog dpd = CustomDatePickerDialog.create(getContext(), this,
                    date, dialogArguments.getSex(), null, null);
            dpd.show(getActivity().getFragmentManager(), TAG_DIALOG_DATE);
        });
        DomanTestParameter[] parameters = getParameters();
        parameterView.setValues(parameters);
        parameterView.setVisibility(parameters == null || parameters.length == 0 ? View.GONE : View.VISIBLE);
        parameterView.selectFirst();
        parameterView.setSex(dialogArguments.getSex());
    }

    @Nullable
    private DomanTestParameter[] getParameters() {
        Test test = dialogArguments.getTest();
        if (test instanceof DomanTest) {
            if (test instanceof DomanPhysicalTest) {
                return new DomanTestParameter[]{
                        PHYSICAL_MOBILITY,
                        PHYSICAL_SPEECH,
                        PHYSICAL_MANUAL
                };
            } else {
                if (test instanceof DomanMentalTest) {
                    return new DomanTestParameter[]{
                            MENTAL_VISION,
                            MENTAL_AUDITION,
                            MENTAL_SENSITIVITY
                    };
                }
            }
        }
        return null;
    }

    @NonNull
    @Override
    protected Dialog createDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(view)
                .setTitle(R.string.menu_filter)
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> {
                            LocalDate date = dateView.getValue();
                            DomanTestParameter parameter = parameterView.getSelected();
                            if (listener != null) {
                                listener.onTestParametersSet(TestParameters.builder()
                                        .date(date)
                                        .parameter(parameter)
                                        .build());
                            }
                        })
                .setNegativeButton(R.string.cancel, null);
        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        return dialog;
    }

    @Override
    public final void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        LocalDate date = LocalDate.fromCalendarFields(calendar);
        dateView.setValue(date);
    }

    public interface Listener {
        void onTestParametersSet(@NonNull TestParameters parameters);
    }
}
