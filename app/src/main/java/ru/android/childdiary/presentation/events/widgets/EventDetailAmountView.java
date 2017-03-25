package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;
import ru.android.childdiary.utils.DoubleUtils;

public class EventDetailAmountView extends EventDetailEditTextView {
    @BindView(R.id.editText)
    CustomEditText editText;

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindInt(R.integer.max_length_amount)
    int amountMaxLength;

    @Getter
    private Double amount;

    public EventDetailAmountView(Context context) {
        super(context);
        init();
    }

    public EventDetailAmountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailAmountView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.event_detail_amount, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(amountMaxLength)});
    }

    public void setAmount(Double amount) {
        this.amount = amount;
        editText.setText(DoubleUtils.amountReview(amount));
    }

    @Override
    public List<Disposable> createSubscriptions(CustomEditText.OnKeyboardHiddenListener listener) {
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

    @Override
    public void setReadOnly(boolean readOnly) {
        editText.setEnabled(!readOnly);
        imageView.setVisibility(readOnly ? INVISIBLE : VISIBLE);
    }
}
