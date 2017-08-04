package ru.android.childdiary.presentation.medical.filter.core;

import android.app.Dialog;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokenautocomplete.FilteredArrayAdapter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.LocalDate;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDateFilterView;
import ru.android.childdiary.presentation.core.widgets.CustomDatePickerDialog;
import ru.android.childdiary.presentation.medical.filter.medicines.MedicineTokenCompleteTextView;
import ru.android.childdiary.presentation.medical.filter.visits.DoctorTokenCompleteTextView;
import ru.android.childdiary.utils.ui.ThemeUtils;

public abstract class MedicalFilterDialogFragment<T extends Serializable, A extends MedicalFilterDialogArguments<T>>
        extends BaseMvpDialogFragment<A> implements DatePickerDialog.OnDateSetListener {
    private static final String TAG_DIALOG_FROM_DATE = "TAG_DIALOG_FROM_DATE";
    private static final String TAG_DIALOG_TO_DATE = "TAG_DIALOG_TO_DATE";

    @BindView(R.id.textViewByDoctor)
    protected TextView textViewByDoctor;

    @BindView(R.id.textViewByMedicine)
    protected TextView textViewByMedicine;

    @BindView(R.id.doctorAutoCompleteTextView)
    protected DoctorTokenCompleteTextView doctorTokenCompleteTextView;

    @BindView(R.id.medicineAutoCompleteTextView)
    protected MedicineTokenCompleteTextView medicineTokenCompleteTextView;

    @BindView(R.id.rootView)
    View rootView;

    @BindView(R.id.dateFromView)
    FieldDateFilterView dateFromView;

    @BindView(R.id.dateToView)
    FieldDateFilterView dateToView;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.dialog_filter_medical;
    }

    @Override
    protected void setupUi() {
        dateFromView.setTitle(getString(R.string.filter_from_date));
        dateToView.setTitle(getString(R.string.filter_to_date));

        dateFromView.setValue(dialogArguments.getFromDate());
        dateToView.setValue(dialogArguments.getToDate());

        dateFromView.setFieldDialogListener(view -> {
            hideKeyboardAndClearFocus(rootView.findFocus());
            LocalDate date = dateFromView.getValue();
            LocalDate maxDate = dateToView.getValue();
            DatePickerDialog dpd = CustomDatePickerDialog.create(getContext(), this,
                    date, dialogArguments.getSex(), null, maxDate);
            dpd.show(getActivity().getFragmentManager(), TAG_DIALOG_FROM_DATE);
        });
        dateToView.setFieldDialogListener(view -> {
            hideKeyboardAndClearFocus(rootView.findFocus());
            LocalDate date = dateToView.getValue();
            LocalDate minDate = dateFromView.getValue();
            DatePickerDialog dpd = CustomDatePickerDialog.create(getContext(), this,
                    date, dialogArguments.getSex(), minDate, null);
            dpd.show(getActivity().getFragmentManager(), TAG_DIALOG_TO_DATE);
        });

        ArrayAdapter<T> adapter = createFilteredAdapter();
        getAutoCompleteTextView().setAdapter(adapter);
        getAutoCompleteTextView().setOnKeyboardHiddenListener(this::hideKeyboardAndClearFocus);
    }

    @NonNull
    @Override
    protected Dialog createDialog(@Nullable View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(view)
                .setTitle(R.string.menu_filter)
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> {
                            List<T> selectedItems = getAutoCompleteTextView().getObjects();
                            LocalDate fromDate = dateFromView.getValue();
                            LocalDate toDate = dateToView.getValue();
                            buildFilter(selectedItems, fromDate, toDate);
                        })
                .setNegativeButton(R.string.cancel, null);

        AlertDialog dialog = builder.setCancelable(false)
                .create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        hideKeyboardAndClearFocus(rootView.findFocus());
    }

    @Override
    public void onPause() {
        super.onPause();
        hideKeyboardAndClearFocus(rootView.findFocus());
    }

    protected abstract BaseTokenCompleteTextView<T> getAutoCompleteTextView();

    protected abstract FilteredArrayAdapter<T> createFilteredAdapter();

    protected abstract void buildFilter(@NonNull List<T> selectedItems,
                                        @NonNull LocalDate fromDate,
                                        @NonNull LocalDate toDate);

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        LocalDate date = LocalDate.fromCalendarFields(calendar);
        switch (view.getTag()) {
            case TAG_DIALOG_FROM_DATE:
                dateFromView.setValue(date);
                break;
            case TAG_DIALOG_TO_DATE:
                dateToView.setValue(date);
                break;
        }
    }
}
