package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.List;

import io.reactivex.disposables.Disposable;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;

public abstract class EventDetailEditTextView extends LinearLayout {
    public EventDetailEditTextView(Context context) {
        super(context);
    }

    public EventDetailEditTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EventDetailEditTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract List<Disposable> createSubscriptions(CustomEditText.OnKeyboardHiddenListener listener);
}
