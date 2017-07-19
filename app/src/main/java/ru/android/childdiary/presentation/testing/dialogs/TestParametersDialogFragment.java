package ru.android.childdiary.presentation.testing.dialogs;

import android.app.Dialog;
import android.content.Context;
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
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.TestParameters;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.DomanTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;
import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;
import ru.android.childdiary.presentation.core.fields.dialogs.AgeDialogArguments;
import ru.android.childdiary.presentation.core.fields.dialogs.AgeDialogFragment;
import ru.android.childdiary.presentation.core.fields.widgets.FieldAgeView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDateView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDomanTestParameterView;
import ru.android.childdiary.presentation.core.widgets.CustomDatePickerDialog;
import ru.android.childdiary.utils.strings.DateUtils;
import ru.android.childdiary.utils.strings.TestUtils;
import ru.android.childdiary.utils.strings.TimeUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

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
    protected int getLayoutResourceId() {
        return R.layout.dialog_test_parameters;
    }

    @Override
    protected void setupUi() {
        dateView.setValue(LocalDate.now());
        updateAge();
        dateView.setFieldDialogListener(view -> {
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
                            .maxYears(DomanTest.MAX_YEARS)
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
    protected Dialog createDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(view)
                .setTitle(R.string.menu_filter)
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
        String parameterStr = TestUtils.toString(getContext(), testParameters.getParameter());
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
        ageView.setValue(age);
        LocalDate birthDate = dialogArguments.getChild().getBirthDate();
        if (birthDate != null) {
            LocalDate date = birthDate.plusMonths(age.getMonths());
            dateView.setValue(date);
        }
    }

    public interface Listener {
        void onTestParametersSet(@NonNull TestParameters parameters);
    }
}
