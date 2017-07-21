package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;
import ru.android.childdiary.presentation.core.widgets.RegExpInputFilter;
import ru.android.childdiary.utils.strings.DoubleUtils;

public class FieldWeightView extends FieldEditTextView {
    @BindView(R.id.editText)
    CustomEditText editText;

    @Getter
    private Double value;

    public FieldWeightView(Context context) {
        super(context);
        init();
    }

    public FieldWeightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FieldWeightView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.field_weight, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setFilters(new InputFilter[]{new RegExpInputFilter.SimpleWeightInputFilter()});
    }

    public void setWeight(Double weight) {
        value = weight;
        editText.setText(DoubleUtils.weightReview(getContext(), weight));
    }

    @Override
    public List<Disposable> createSubscriptions(CustomEditText.OnKeyboardHiddenListener listener) {
        editText.setOnKeyboardHiddenListener(listener);

        List<Disposable> disposables = new ArrayList<>();

        disposables.add(RxTextView.afterTextChangeEvents(editText).subscribe(textViewAfterTextChangeEvent -> {
            Double weight = DoubleUtils.parse(editText.getText().toString());
            value = weight;
        }));

        disposables.add(RxView.focusChanges(editText).subscribe(hasFocus -> {
            if (hasFocus) {
                editText.setText(DoubleUtils.weightEdit(value));
                editText.setSelection(editText.getText().length());
            } else {
                editText.setText(DoubleUtils.weightReview(getContext(), value));
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
