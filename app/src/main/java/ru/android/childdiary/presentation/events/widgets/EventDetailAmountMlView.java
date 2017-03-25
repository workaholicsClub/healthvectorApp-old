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

public class EventDetailAmountMlView extends EventDetailEditTextView {
    @BindView(R.id.editText)
    CustomEditText editText;

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindInt(R.integer.max_length_amount_ml)
    int amountMlMaxLength;

    @Getter
    private Double amountMl;

    public EventDetailAmountMlView(Context context) {
        super(context);
        init();
    }

    public EventDetailAmountMlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailAmountMlView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.event_detail_amount_ml, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(amountMlMaxLength)});
    }

    public void setAmountMl(Double amount) {
        amountMl = amount;
        editText.setText(DoubleUtils.amountMlReview(getContext(), amount));
    }

    @Override
    public List<Disposable> createSubscriptions(CustomEditText.OnKeyboardHiddenListener listener) {
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

    @Override
    public void setReadOnly(boolean readOnly) {
        editText.setEnabled(!readOnly);
        imageView.setVisibility(readOnly ? INVISIBLE : VISIBLE);
    }
}
