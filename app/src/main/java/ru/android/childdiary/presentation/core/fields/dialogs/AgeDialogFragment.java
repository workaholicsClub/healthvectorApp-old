package ru.android.childdiary.presentation.core.fields.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.core.TimeUnit;
import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;
import ru.android.childdiary.utils.strings.StringUtils;
import ru.android.childdiary.utils.strings.TimeUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class AgeDialogFragment extends BaseMvpDialogFragment<AgeDialogArguments> {
    @BindView(R.id.rootView)
    View rootView;

    @BindView(R.id.numberPickerYears)
    NumberPicker numberPickerYears;

    @BindView(R.id.numberPickerMonths)
    NumberPicker numberPickerMonths;

    @BindView(R.id.yearsHeader)
    TextView yearsHeader;

    @BindView(R.id.monthsHeader)
    TextView monthsHeader;

    @Nullable
    private Listener listener;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.dialog_age;
    }

    @Override
    protected void setupUi() {
        int years = dialogArguments.getAge() == null ? 0 : dialogArguments.getAge().getYearsPart();
        int months = dialogArguments.getAge() == null ? 0 : dialogArguments.getAge().getMonthsPart();

        numberPickerYears.setMinValue(0);
        numberPickerYears.setMaxValue(dialogArguments.getMaxYears());
        numberPickerYears.setValue(years);
        numberPickerYears.setOnValueChangedListener(
                (picker, oldVal, newVal) -> {
                    updateYearsHeader();
                    setMaxMonths();
                });
        updateYearsHeader();

        numberPickerMonths.setMinValue(0);
        setMaxMonths();
        numberPickerMonths.setValue(months);
        numberPickerMonths.setOnValueChangedListener(
                (picker, oldVal, newVal) -> updateMonthsHeader());
        updateMonthsHeader();
    }

    private void setMaxMonths() {
        int years = numberPickerYears.getValue();
        boolean max = years == dialogArguments.getMaxYears();
        numberPickerMonths.setMaxValue(max ? 0 : TimeUtils.MONTHS_IN_YEAR - 1);
    }

    private void updateYearsHeader() {
        yearsHeader.setText(StringUtils.timeUnit(getContext(), numberPickerYears.getValue(), TimeUnit.YEAR));
    }

    private void updateMonthsHeader() {
        monthsHeader.setText(StringUtils.timeUnit(getContext(), numberPickerMonths.getValue(), TimeUnit.MONTH));
    }

    @NonNull
    @Override
    protected Dialog createDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(view)
                .setTitle(getString(R.string.age))
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> {
                            hideKeyboardAndClearFocus(rootView.findFocus());
                            int months = numberPickerYears.getValue() * TimeUtils.MONTHS_IN_YEAR
                                    + numberPickerMonths.getValue();
                            if (listener != null) {
                                listener.onSetAge(getTag(), TimeUtils.Age.builder().months(months).build());
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> hideKeyboardAndClearFocus(rootView.findFocus()));

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

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

    public interface Listener {
        void onSetAge(String tag, @NonNull TimeUtils.Age age);
    }
}
