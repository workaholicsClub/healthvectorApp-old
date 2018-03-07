package ru.android.healthvector.presentation.testing.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.LocalDate;

import java.util.Calendar;

import butterknife.BindView;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.DomanTestParameter;
import ru.android.healthvector.data.types.TestType;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.testing.data.processors.core.TestParameters;
import ru.android.healthvector.domain.development.testing.data.tests.core.DomanTest;
import ru.android.healthvector.domain.development.testing.data.tests.core.Test;
import ru.android.healthvector.presentation.core.BaseMvpDialogFragment;
import ru.android.healthvector.presentation.core.fields.dialogs.AgeDialogArguments;
import ru.android.healthvector.presentation.core.fields.dialogs.AgeDialogFragment;
import ru.android.healthvector.presentation.core.fields.widgets.FieldAgeView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldDateView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldDomanTestParameterView;
import ru.android.healthvector.presentation.core.widgets.CustomDatePickerDialog;
import ru.android.healthvector.utils.strings.DateUtils;
import ru.android.healthvector.utils.strings.TestUtils;
import ru.android.healthvector.utils.strings.TimeUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;

public class TestParametersDialogFragment extends BaseMvpDialogFragment<TestParametersDialogArguments>
        implements DatePickerDialog.OnDateSetListener, AgeDialogFragment.Listener, TestParametersView {
    private static final String TAG_DIALOG_DATE = "TAG_DIALOG_DATE";
    private static final String TAG_DIALOG_AGE = "TAG_DIALOG_AGE";

    @BindView(R.id.dateView)
    FieldDateView dateView;

    @BindView(R.id.ageView)
    FieldAgeView ageView;

    @BindView(R.id.domanTestParameterView)
    FieldDomanTestParameterView parameterView;

    @InjectPresenter
    TestParametersPresenter presenter;

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
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.dialog_test_parameters;
    }

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
        dateView.setValue(LocalDate.now());
        updateAge();
        dateView.setFieldDialogListener(view -> {
            if (getActivity() == null) {
                logger.error("activity is null");
                return;
            }
            LocalDate date = dateView.getValue();
            LocalDate birthDate = dialogArguments.getChild().getBirthDate();
            LocalDate minDate = null, maxDate = null;
            if (birthDate != null) {
                minDate = birthDate;
                maxDate = birthDate.plusYears(DomanTest.MAX_YEARS);
            }
            DatePickerDialog dpd = CustomDatePickerDialog.create(getContext(), this,
                    date, dialogArguments.getSex(), minDate, maxDate);
            dpd.show(getActivity().getFragmentManager(), TAG_DIALOG_DATE);
        });

        if (dialogArguments.getChild().getId() == null) {
            dateView.setReadOnly(true);
        }

        ageView.setFieldDialogListener(v -> {
            AgeDialogFragment dialogFragment = new AgeDialogFragment();
            dialogFragment.showAllowingStateLoss(getChildFragmentManager(), TAG_DIALOG_AGE,
                    AgeDialogArguments.builder()
                            .sex(dialogArguments.getSex())
                            .title(getString(R.string.age))
                            .maxAge(TimeUtils.Age.builder()
                                    .months(DomanTest.MAX_YEARS * TimeUtils.MONTHS_IN_YEAR)
                                    .build())
                            .age(ageView.getValue())
                            .build());
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
            return ((DomanTest) test).getParameters();
        }
        return null;
    }

    @NonNull
    @Override
    protected Dialog createDialog(@Nullable View view, @Nullable Bundle savedInstanceState) {
        @SuppressWarnings("ConstantConditions")
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(view)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, null);
        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener((dialogView) -> {
                TimeUtils.Age age = ageView.getValue();
                if (age == null) {
                    showToast(getString(R.string.validation_no_age));
                    return;
                }
                LocalDate date = dateView.getValue();
                DomanTestParameter parameter = parameterView.getSelected();
                presenter.check(dialogArguments.getChild(), dialogArguments.getTest().getTestType(),
                        TestParameters.builder()
                                .birthDate(dialogArguments.getChild().getBirthDate())
                                .date(date)
                                .age(age)
                                .parameter(parameter)
                                .build());
            });
        });
        return dialog;
    }

    @Override
    public void close(@NonNull Child child, @NonNull TestParameters testParameters) {
        if (listener != null) {
            listener.onTestParametersSet(testParameters);
        }

        getDialog().dismiss();
    }

    @Override
    public void dateAlreadyUsed(@NonNull LocalDate date,
                                @NonNull TestType testType,
                                @NonNull TestParameters testParameters) {
        String dateStr = DateUtils.date(getContext(), date);
        String parameterStr = TestUtils.testParameter(getContext(), testParameters.getParameter());
        if (getContext() == null) {
            logger.error("context is null");
            return;
        }
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setMessage(getString(R.string.date_already_used_dialog_text, dateStr, parameterStr))
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    @Override
    public final void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        LocalDate date = LocalDate.fromCalendarFields(calendar);
        dateView.setValue(date);
        updateAge();
    }

    private void updateAge() {
        LocalDate date = dateView.getValue();
        LocalDate birthDate = dialogArguments.getChild().getBirthDate();
        if (birthDate != null) {
            TimeUtils.Age age = TimeUtils.getAge(birthDate, date);
            ageView.setValue(age);
        }
    }

    @Override
    public void onSetAge(String tag, @NonNull TimeUtils.Age age) {
        if (TAG_DIALOG_AGE.equals(tag)) {
            ageView.setValue(age);
            LocalDate birthDate = dialogArguments.getChild().getBirthDate();
            if (birthDate != null) {
                LocalDate date = birthDate.plusMonths(age.getMonths());
                dateView.setValue(date);
            }
        }
    }

    public interface Listener {
        void onTestParametersSet(@NonNull TestParameters parameters);
    }
}
