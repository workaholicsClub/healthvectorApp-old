package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import lombok.val;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.bindings.FieldValueChangeListener;
import ru.android.childdiary.presentation.core.bindings.FieldValueView;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;
import ru.android.childdiary.presentation.core.widgets.OnKeyboardHiddenListener;
import ru.android.childdiary.utils.strings.DoubleUtils;

public abstract class FieldUnitView extends FieldEditTextView implements FieldValueView<Double> {
    private final Set<FieldValueChangeListener<Double>> valueChangeListeners = new HashSet<>();

    @BindView(R.id.imageViewIcon)
    ImageView imageViewIcon;

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.editTextWrapper)
    View editTextWrapper;

    @BindView(R.id.editText)
    CustomEditText editText;

    @Getter
    private Double value;
    private Drawable icon;
    private String text, hint;

    public FieldUnitView(Context context) {
        super(context);
    }

    public FieldUnitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldUnitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init(@Nullable AttributeSet attrs) {
        inflate(getContext(), R.layout.field_unit, this);
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.FieldUnitView, 0, 0);
            try {
                icon = ta.getDrawable(R.styleable.FieldUnitView_fieldIcon);
                text = ta.getString(R.styleable.FieldUnitView_fieldText);
                hint = ta.getString(R.styleable.FieldUnitView_fieldHint);
            } finally {
                ta.recycle();
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setFilters(new InputFilter[]{getInputFilter()});

        imageViewIcon.setImageDrawable(icon);
        textView.setText(text);
        editText.setHint(hint);
    }

    public void setValue(Double value) {
        this.value = value;
        valueChanged();
        editText.setText(reviewString(value));
    }

    @Override
    public List<Disposable> createSubscriptions(OnKeyboardHiddenListener listener) {
        editText.setOnKeyboardHiddenListener(listener);

        List<Disposable> disposables = new ArrayList<>();

        disposables.add(RxTextView.afterTextChangeEvents(editText).subscribe(textViewAfterTextChangeEvent -> {
            Double value = DoubleUtils.parse(editText.getText().toString());
            this.value = value;
            valueChanged();
        }));

        disposables.add(RxView.focusChanges(editText).subscribe(hasFocus -> {
            if (hasFocus) {
                editText.setText(editString(value));
                editText.setSelection(editText.getText().length());
            } else {
                editText.setText(reviewString(value));
            }
        }));

        disposables.add(RxTextView.editorActionEvents(editText).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_DONE) {
                listener.onKeyboardHidden(editText);
            }
        }));

        return disposables;
    }

    protected abstract InputFilter getInputFilter();

    protected abstract String editString(@Nullable Double value);

    protected abstract String reviewString(@Nullable Double value);

    private void valueChanged() {
        for (val listener : valueChangeListeners) {
            listener.onValueChange(value);
        }
    }

    @Override
    public void addValueChangeListener(@NonNull FieldValueChangeListener<Double> listener) {
        valueChangeListeners.add(listener);
    }

    @Override
    public void removeValueChangeListener(@NonNull FieldValueChangeListener<Double> listener) {
        valueChangeListeners.remove(listener);
    }

    public void validated(boolean valid) {
        editTextWrapper.setBackgroundResource(valid ? R.drawable.edit_text_background : R.drawable.edit_text_background_error);
    }
}
