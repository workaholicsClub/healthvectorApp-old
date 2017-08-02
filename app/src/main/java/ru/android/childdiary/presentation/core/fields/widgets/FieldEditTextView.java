package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import ru.android.childdiary.presentation.core.widgets.OnKeyboardHiddenListener;

public abstract class FieldEditTextView extends LinearLayout {
    public FieldEditTextView(Context context) {
        super(context);
        init(null);
    }

    public FieldEditTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FieldEditTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    protected abstract void init(@Nullable AttributeSet attrs);

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public abstract List<Disposable> createSubscriptions(OnKeyboardHiddenListener listener);
}
