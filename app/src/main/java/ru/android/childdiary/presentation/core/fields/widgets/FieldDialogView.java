package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;
import lombok.Setter;
import ru.android.childdiary.R;
import ru.android.childdiary.utils.ui.FontUtils;

public abstract class FieldDialogView<T> extends LinearLayout implements View.OnClickListener, FieldReadOnly {
    private final Typeface typeface = FontUtils.getTypefaceRegular(getContext());

    @BindView(R.id.textViewWrapper)
    View textViewWrapper;

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.imageView)
    ImageView imageView;

    @Nullable
    @Setter
    private FieldDialogListener fieldDialogListener;

    @Nullable
    @Getter
    private T value;

    public FieldDialogView(Context context) {
        super(context);
        init();
    }

    public FieldDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FieldDialogView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), getLayoutResourceId(), this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setReadOnly(false);
    }

    public void setValue(@Nullable T value) {
        this.value = value;
        textView.setText(getTextForValue(value));
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
        //noinspection deprecation
        textView.setTextAppearance(getContext(), readOnly ? R.style.SecondaryTextAppearance : R.style.PrimaryTextAppearance);
        textView.setTypeface(typeface);
        imageView.setVisibility(readOnly ? INVISIBLE : VISIBLE);
        textViewWrapper.setOnClickListener(readOnly ? null : this);
        textViewWrapper.setClickable(!readOnly);
    }

    @LayoutRes
    protected abstract int getLayoutResourceId();

    @Nullable
    protected abstract String getTextForValue(@Nullable T value);

    public interface FieldDialogListener {
        void requestValueChange(FieldDialogView view);
    }
}
