package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.List;

import io.reactivex.disposables.Disposable;
import ru.android.childdiary.presentation.core.widgets.OnKeyboardHiddenListener;

public abstract class FieldEditTextView extends LinearLayout {
    public FieldEditTextView(Context context) {
        super(context);
    }

    public FieldEditTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldEditTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract List<Disposable> createSubscriptions(OnKeyboardHiddenListener listener);
}
