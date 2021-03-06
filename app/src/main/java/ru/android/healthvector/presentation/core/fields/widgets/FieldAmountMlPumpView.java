package ru.android.healthvector.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import ru.android.healthvector.R;
import ru.android.healthvector.presentation.core.widgets.CustomEditText;
import ru.android.healthvector.presentation.core.widgets.OnKeyboardHiddenListener;
import ru.android.healthvector.presentation.core.widgets.RegExpInputFilter;
import ru.android.healthvector.utils.strings.DoubleUtils;

public class FieldAmountMlPumpView extends FieldEditTextView {
    private TextView textViewAmountMl;
    private CustomEditText editTextAmountMlLeft, editTextAmountMlRight;
    private ImageView imageViewLeft, imageViewRight;

    @Getter
    private Double amountMlLeft, amountMlRight;

    public FieldAmountMlPumpView(Context context) {
        super(context);
    }

    public FieldAmountMlPumpView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldAmountMlPumpView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init(@Nullable AttributeSet attrs) {
        setOrientation(LinearLayout.VERTICAL);

        View viewAmount = inflate(getContext(), R.layout.field_amount_ml_pump, null);
        addView(viewAmount);
        textViewAmountMl = ButterKnife.findById(viewAmount, R.id.textView);

        View viewLeft = inflate(getContext(), R.layout.field_amount_ml_pump_left, null);
        addView(viewLeft);
        imageViewLeft = ButterKnife.findById(viewLeft, R.id.imageView);
        editTextAmountMlLeft = ButterKnife.findById(viewLeft, R.id.editText);

        View viewRight = inflate(getContext(), R.layout.field_amount_ml_pump_right, null);
        addView(viewRight);
        imageViewRight = ButterKnife.findById(viewRight, R.id.imageView);
        editTextAmountMlRight = ButterKnife.findById(viewRight, R.id.editText);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        editTextAmountMlLeft.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editTextAmountMlLeft.setFilters(new InputFilter[]{new RegExpInputFilter.AmountMlInputFilter()});
        editTextAmountMlRight.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editTextAmountMlRight.setFilters(new InputFilter[]{new RegExpInputFilter.AmountMlInputFilter()});
    }

    public void setAmountMlLeft(Double amount) {
        amountMlLeft = amount;
        editTextAmountMlLeft.setText(DoubleUtils.amountMlReview(getContext(), amountMlLeft));
    }

    public void setAmountMlRight(Double amount) {
        amountMlRight = amount;
        editTextAmountMlRight.setText(DoubleUtils.amountMlReview(getContext(), amountMlRight));
    }

    private void updateSum() {
        double left = amountMlLeft == null ? 0 : amountMlLeft;
        double right = amountMlRight == null ? 0 : amountMlRight;
        double sum = left + right;
        textViewAmountMl.setText(DoubleUtils.amountMlReview(getContext(), sum));
    }

    @Override
    public List<Disposable> createSubscriptions(OnKeyboardHiddenListener listener) {
        editTextAmountMlLeft.setOnKeyboardHiddenListener(listener);
        editTextAmountMlRight.setOnKeyboardHiddenListener(listener);

        List<Disposable> disposables = new ArrayList<>();

        disposables.add(RxTextView.afterTextChangeEvents(editTextAmountMlLeft).subscribe(textViewAfterTextChangeEvent -> {
            Double amount = DoubleUtils.parse(editTextAmountMlLeft.getText().toString());
            amountMlLeft = amount;
            updateSum();
        }));
        disposables.add(RxTextView.afterTextChangeEvents(editTextAmountMlRight).subscribe(textViewAfterTextChangeEvent -> {
            Double amount = DoubleUtils.parse(editTextAmountMlRight.getText().toString());
            amountMlRight = amount;
            updateSum();
        }));

        disposables.add(RxView.focusChanges(editTextAmountMlLeft).subscribe(hasFocus -> {
            if (hasFocus) {
                editTextAmountMlLeft.setText(DoubleUtils.amountMlEdit(amountMlLeft));
                editTextAmountMlLeft.setSelection(editTextAmountMlLeft.getText().length());
            } else {
                editTextAmountMlLeft.setText(DoubleUtils.amountMlReview(getContext(), amountMlLeft));
            }
        }));
        disposables.add(RxView.focusChanges(editTextAmountMlRight).subscribe(hasFocus -> {
            if (hasFocus) {
                editTextAmountMlRight.setText(DoubleUtils.amountMlEdit(amountMlRight));
                editTextAmountMlRight.setSelection(editTextAmountMlRight.getText().length());
            } else {
                editTextAmountMlRight.setText(DoubleUtils.amountMlReview(getContext(), amountMlRight));
            }
        }));

        disposables.add(RxTextView.editorActionEvents(editTextAmountMlRight).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_DONE) {
                listener.onKeyboardHidden(editTextAmountMlRight);
            }
        }));

        return disposables;
    }
}
