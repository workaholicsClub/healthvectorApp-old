package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;

import ru.android.childdiary.R;

public class FieldExerciseNameView extends FieldTextViewWithImageView {
    public FieldExerciseNameView(Context context) {
        super(context);
    }

    public FieldExerciseNameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldExerciseNameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @LayoutRes
    @Override
    protected int getLayoutResourceId() {
        return R.layout.field_exercise;
    }

    @Override
    protected int getIconResId() {
        return R.drawable.ic_exercise_name;
    }
}
