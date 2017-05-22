package ru.android.childdiary.presentation.medical.filter.core;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;
import ru.android.childdiary.utils.DateUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public abstract class MedicalFilterDialogFragment<T extends Serializable, A extends MedicalFilterDialogArguments<T>>
        extends BaseMvpDialogFragment<A> implements View.OnClickListener {
    private static final String TAG_DIALOG_FROM_DATE = "TAG_DIALOG_FROM_DATE";
    private static final String TAG_DIALOG_TO_DATE = "TAG_DIALOG_TO_DATE";

    @BindView(R.id.filter_by_date_from)
    View filterByDateFrom;

    @BindView(R.id.filter_by_date_to)
    View filterByDateTo;

    @BindView(R.id.filter_by_item)
    TextView filterByItem;

    @BindView(R.id.autoCompleteTextView)
    AutoCompleteTextView autoCompleteTextView;

    private TextView textViewFromDateValue;
    private TextView textViewToDateValue;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.dialog_filter_medical_data;
    }

    @Override
    protected void setupUi() {
        TextView textViewFromDateCaption = ButterKnife.findById(filterByDateFrom, R.id.filter_by_date_caption);
        textViewFromDateCaption.setText(R.string.filter_from_date);

        textViewFromDateValue = ButterKnife.findById(filterByDateFrom, R.id.filter_by_date_value);
        textViewFromDateValue.setText(DateUtils.date(getContext(), dialogArguments.getFromDate()));
        textViewFromDateValue.setOnClickListener(this);

        TextView textViewToDate = ButterKnife.findById(filterByDateTo, R.id.filter_by_date_caption);
        textViewToDate.setText(R.string.filter_to_date);

        textViewToDateValue = ButterKnife.findById(filterByDateTo, R.id.filter_by_date_value);
        textViewToDateValue.setText(DateUtils.date(getContext(), dialogArguments.getToDate()));
        textViewToDateValue.setOnClickListener(this);

        filterByItem.setText(getFilterByItemTextResId());

        int layoutItemId = android.R.layout.simple_dropdown_item_1line;
        List<String> strings = Arrays.asList("test1", "test2", "test3");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), layoutItemId, strings);

        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setHint(getAutoCompleteHintResId());
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
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @StringRes
    protected abstract int getFilterByItemTextResId();

    @StringRes
    protected abstract int getAutoCompleteHintResId();

    @Override
    public void onClick(View v) {
        if (v == textViewFromDateValue) {

        } else if (v == textViewToDateValue) {

        }
    }
}
