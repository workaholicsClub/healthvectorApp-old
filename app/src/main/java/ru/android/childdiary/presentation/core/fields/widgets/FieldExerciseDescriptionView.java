package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import lombok.Setter;
import ru.android.childdiary.R;
import ru.android.childdiary.utils.HtmlUtils;

public class FieldExerciseDescriptionView extends FieldTextViewWithImageView {
    @Nullable
    @Setter
    private OnLinkClickListener onLinkClickListener;

    public FieldExerciseDescriptionView(Context context) {
        super(context);
    }

    public FieldExerciseDescriptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldExerciseDescriptionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        textView.setMovementMethod(new LinkMovementMethod());
    }

    @Override
    public void setText(String text) {
        setTextViewHtml(textView, text);
    }

    private void setTextViewHtml(TextView textView, String html) {
        CharSequence sequence = HtmlUtils.fromHtml(html);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urlSpans = stringBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for (URLSpan urlSpan : urlSpans) {
            makeLinkClickable(stringBuilder, urlSpan);
        }
        textView.setText(stringBuilder);
    }

    private void makeLinkClickable(SpannableStringBuilder stringBuilder, final URLSpan urlSpan) {
        int start = stringBuilder.getSpanStart(urlSpan);
        int end = stringBuilder.getSpanEnd(urlSpan);
        int flags = stringBuilder.getSpanFlags(urlSpan);
        ClickableSpan clickable = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                if (onLinkClickListener != null) {
                    onLinkClickListener.onLinkClick(urlSpan.getURL());
                }
            }
        };
        stringBuilder.setSpan(clickable, start, end, flags);
        stringBuilder.removeSpan(urlSpan);
    }

    @LayoutRes
    @Override
    protected int getLayoutResourceId() {
        return R.layout.field_exercise;
    }

    @Override
    protected int getIconResId() {
        return R.drawable.ic_exercise_description;
    }

    public interface OnLinkClickListener {
        void onLinkClick(String url);
    }
}
