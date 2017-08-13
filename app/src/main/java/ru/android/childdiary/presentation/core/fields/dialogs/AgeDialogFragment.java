package ru.android.childdiary.presentation.core.fields.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.calendar.data.core.TimeUnit;
import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;
import ru.android.childdiary.utils.strings.StringUtils;
import ru.android.childdiary.utils.strings.TimeUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class AgeDialogFragment extends BaseMvpDialogFragment<AgeDialogArguments> {
    private final List<Listener> listeners = new ArrayList<>();

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

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.dialog_age;
    }

    @Override
    protected void setupUi() {
        int years = dialogArguments.getAge() == null ? 0 : dialogArguments.getAge().getYearsPart();
        int months = dialogArguments.getAge() == null ? 0 : dialogArguments.getAge().getMonthsPart();

        numberPickerYears.setMinValue(0);
        numberPickerYears.setMaxValue(dialogArguments.getMaxAge().getYearsPart());
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
        TimeUtils.Age maxAge = dialogArguments.getMaxAge();
        boolean max = years == maxAge.getYearsPart();
        numberPickerMonths.setMaxValue(max ? maxAge.getMonthsPart() : TimeUtils.MONTHS_IN_YEAR - 1);
    }

    private void updateYearsHeader() {
        yearsHeader.setText(StringUtils.timeUnit(getContext(), numberPickerYears.getValue(), TimeUnit.YEAR));
    }

    private void updateMonthsHeader() {
        monthsHeader.setText(StringUtils.timeUnit(getContext(), numberPickerMonths.getValue(), TimeUnit.MONTH));
    }

    @NonNull
    @Override
    protected Dialog createDialog(@Nullable View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(view)
                .setTitle(dialogArguments.getTitle())
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> {
                            hideKeyboardAndClearFocus(rootView.findFocus());
                            int months = numberPickerYears.getValue() * TimeUtils.MONTHS_IN_YEAR
                                    + numberPickerMonths.getValue();
                            for (Listener listener : listeners) {
                                listener.onSetAge(getTag(), TimeUtils.Age.builder().months(months).build());
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> hideKeyboardAndClearFocus(rootView.findFocus()));

        return builder.setCancelable(false)
                .create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listeners.add((Listener) context);
        }
        if (getParentFragment() instanceof Listener) {
            listeners.add((Listener) getParentFragment());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listeners.clear();
    }

    public interface Listener {
        void onSetAge(String tag, @NonNull TimeUtils.Age age);
    }
}
