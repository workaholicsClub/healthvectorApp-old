package ru.android.healthvector.presentation.core.fields.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.healthvector.R;

public class FieldTextViewWithImageView extends LinearLayout {
    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.textView)
    TextView textView;

    private Drawable icon;

    public FieldTextViewWithImageView(Context context) {
        super(context);
        init(null);
    }

    public FieldTextViewWithImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FieldTextViewWithImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        inflate(getContext(), R.layout.field_text_view_with_image_view, this);
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.FieldTextViewWithImageView, 0, 0);
            try {
                icon = ta.getDrawable(R.styleable.FieldTextViewWithImageView_fieldIcon);
            } finally {
                ta.recycle();
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        imageView.setImageDrawable(icon);
    }

    public void setText(String text) {
        textView.setText(text);
    }
}
