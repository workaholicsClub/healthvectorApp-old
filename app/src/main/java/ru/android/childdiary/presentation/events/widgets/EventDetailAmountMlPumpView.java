package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
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
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;
import ru.android.childdiary.presentation.core.widgets.RegExpInputFilter;
import ru.android.childdiary.utils.DoubleUtils;

public class EventDetailAmountMlPumpView extends EventDetailEditTextView {
    private TextView textViewAmountMl;
    private CustomEditText editTextAmountMlLeft, editTextAmountMlRight;
    private ImageView imageViewLeft, imageViewRight;

    @Getter
    private Double amountMlLeft, amountMlRight;

    public EventDetailAmountMlPumpView(Context context) {
        super(context);
        init();
    }

    public EventDetailAmountMlPumpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailAmountMlPumpView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        View view;

        view = inflate(getContext(), R.layout.event_detail_amount_ml_pump, null);
        addView(view);
        textViewAmountMl = ButterKnife.findById(view, R.id.textView);

        view = inflate(getContext(), R.layout.event_detail_amount_ml_pump_left, null);
        addView(view);
        imageViewLeft = ButterKnife.findById(view, R.id.imageView);
        editTextAmountMlLeft = ButterKnife.findById(view, R.id.editText);

        view = inflate(getContext(), R.layout.event_detail_amount_ml_pump_right, null);
        addView(view);
        imageViewRight = ButterKnife.findById(view, R.id.imageView);
        editTextAmountMlRight = ButterKnife.findById(view, R.id.editText);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
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
    public List<Disposable> createSubscriptions(CustomEditText.OnKeyboardHiddenListener listener) {
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
