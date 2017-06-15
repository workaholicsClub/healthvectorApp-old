package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;

import ru.android.childdiary.R;
import ru.android.childdiary.utils.HtmlUtils;

public class FieldExerciseDescriptionView extends FieldTextViewWithImageView {
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
        textView.setText(HtmlUtils.fromHtml(text));
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
}
