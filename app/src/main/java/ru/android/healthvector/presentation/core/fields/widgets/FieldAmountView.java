package ru.android.healthvector.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import ru.android.healthvector.R;
import ru.android.healthvector.presentation.core.widgets.CustomEditText;
import ru.android.healthvector.presentation.core.widgets.OnKeyboardHiddenListener;
import ru.android.healthvector.presentation.core.widgets.RegExpInputFilter;
import ru.android.healthvector.utils.strings.DoubleUtils;

public class FieldAmountView extends FieldEditTextView {
    @BindView(R.id.editText)
    CustomEditText editText;

    @Getter
    private Double amount;

    public FieldAmountView(Context context) {
        super(context);
    }

    public FieldAmountView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldAmountView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init(@Nullable AttributeSet attrs) {
        inflate(getContext(), R.layout.field_amount, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setFilters(new InputFilter[]{new RegExpInputFilter.AmountInputFilter()});
    }

    public void setAmount(Double amount) {
        this.amount = amount;
        editText.setText(DoubleUtils.amountReview(amount));
    }

    @Override
    public List<Disposable> createSubscriptions(OnKeyboardHiddenListener listener) {
        editText.setOnKeyboardHiddenListener(listener);

        List<Disposable> disposables = new ArrayList<>();

        disposables.add(RxTextView.afterTextChangeEvents(editText).subscribe(textViewAfterTextChangeEvent -> {
            Double amount = DoubleUtils.parse(editText.getText().toString());
            this.amount = amount;
        }));

        disposables.add(RxView.focusChanges(editText).subscribe(hasFocus -> {
            if (hasFocus) {
                editText.setText(DoubleUtils.amountEdit(amount));
                editText.setSelection(editText.getText().length());
            } else {
                editText.setText(DoubleUtils.amountReview(amount));
            }
        }));

        disposables.add(RxTextView.editorActionEvents(editText).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_DONE) {
                listener.onKeyboardHidden(editText);
            }
        }));

        return disposables;
    }
}
