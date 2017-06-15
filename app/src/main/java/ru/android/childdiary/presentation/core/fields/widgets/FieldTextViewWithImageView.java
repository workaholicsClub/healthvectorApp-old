package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.childdiary.R;

public abstract class FieldTextViewWithImageView extends LinearLayout {
    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.textView)
    TextView textView;

    public FieldTextViewWithImageView(Context context) {
        super(context);
        init();
    }

    public FieldTextViewWithImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FieldTextViewWithImageView(Context context, AttributeSet attrs, int defStyle) {
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
        imageView.setImageResource(getIconResId());
    }

    public void setText(String text) {
        textView.setText(text);
    }

    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_text_view_with_image_view;
    }

    @DrawableRes
    protected abstract int getIconResId();
}
