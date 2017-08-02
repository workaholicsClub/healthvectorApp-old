package ru.android.childdiary.presentation.core.fields.widgets;

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
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;
import ru.android.childdiary.presentation.core.widgets.OnKeyboardHiddenListener;
import ru.android.childdiary.presentation.core.widgets.RegExpInputFilter;
import ru.android.childdiary.utils.strings.DoubleUtils;

public class FieldAmountMlView extends FieldEditTextView {
    @BindView(R.id.editText)
    CustomEditText editText;

    @Getter
    private Double amountMl;

    public FieldAmountMlView(Context context) {
        super(context);
    }

    public FieldAmountMlView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldAmountMlView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init(@Nullable AttributeSet attrs) {
        inflate(getContext(), R.layout.field_amount_ml, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setFilters(new InputFilter[]{new RegExpInputFilter.AmountMlInputFilter()});
    }

    public void setAmountMl(Double amount) {
        amountMl = amount;
        editText.setText(DoubleUtils.amountMlReview(getContext(), amount));
    }

    @Override
    public List<Disposable> createSubscriptions(OnKeyboardHiddenListener listener) {
        editText.setOnKeyboardHiddenListener(listener);

        List<Disposable> disposables = new ArrayList<>();

        disposables.add(RxTextView.afterTextChangeEvents(editText).subscribe(textViewAfterTextChangeEvent -> {
            Double amount = DoubleUtils.parse(editText.getText().toString());
            amountMl = amount;
        }));

        disposables.add(RxView.focusChanges(editText).subscribe(hasFocus -> {
            if (hasFocus) {
                editText.setText(DoubleUtils.amountMlEdit(amountMl));
                editText.setSelection(editText.getText().length());
            } else {
                editText.setText(DoubleUtils.amountMlReview(getContext(), amountMl));
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
