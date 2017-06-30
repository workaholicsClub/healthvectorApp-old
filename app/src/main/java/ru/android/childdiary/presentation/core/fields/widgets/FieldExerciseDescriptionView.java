package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import lombok.Setter;
import ru.android.childdiary.R;
import ru.android.childdiary.utils.HtmlUtils;

public class FieldExerciseDescriptionView extends FieldTextViewWithImageView {
    @Nullable
    @Setter
    private HtmlUtils.OnLinkClickListener onLinkClickListener;

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
    public void setText(String text) {
        HtmlUtils.setupClickableLinks(textView, text, onLinkClickListener);
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
