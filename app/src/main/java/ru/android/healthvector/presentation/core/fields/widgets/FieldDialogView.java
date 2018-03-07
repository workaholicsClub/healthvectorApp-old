package ru.android.healthvector.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import lombok.Setter;
import ru.android.healthvector.R;

public abstract class FieldDialogView<T> extends BaseFieldValueView<T> implements View.OnClickListener {
    @BindView(R.id.textViewWrapper)
    View textViewWrapper;

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.imageView)
    ImageView imageView;

    @Nullable
    @Setter
    private FieldDialogListener fieldDialogListener;

    public FieldDialogView(Context context) {
        super(context);
    }

    public FieldDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldDialogView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @CallSuper
    @Override
    protected void valueChanged() {
        textView.setText(getTextForValue(getValue()));
    }

    @Override
    public void onClick(View v) {
        if (v == textViewWrapper) {
            if (fieldDialogListener != null) {
                fieldDialogListener.requestValueChange(this);
            }
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        imageView.setVisibility(readOnly ? INVISIBLE : VISIBLE);
        textViewWrapper.setOnClickListener(readOnly ? null : this);
        textViewWrapper.setClickable(!readOnly);
    }

    @Override
    public void setEnabled(boolean enabled) {
        setReadOnly(!enabled);
        //noinspection deprecation
        textView.setTextAppearance(getContext(), enabled ? R.style.PrimaryTextAppearance : R.style.SecondaryTextAppearance);
        textView.setTypeface(typeface);
    }

    @Nullable
    protected abstract String getTextForValue(@Nullable T value);

    public interface FieldDialogListener {
        void requestValueChange(FieldDialogView view);
    }
}
