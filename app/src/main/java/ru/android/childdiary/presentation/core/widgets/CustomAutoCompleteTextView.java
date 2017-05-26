package ru.android.childdiary.presentation.core.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import lombok.Setter;

public class CustomAutoCompleteTextView extends AppCompatAutoCompleteTextView {
    @Setter
    @Nullable
    private OnKeyboardHiddenListener onKeyboardHiddenListener;

    public CustomAutoCompleteTextView(Context context) {
        super(context);
        init();
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setThreshold(1);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection connection = super.onCreateInputConnection(outAttrs);
        outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        return connection;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        boolean result = super.onKeyPreIme(keyCode, event);
        if (result) {
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (onKeyboardHiddenListener != null) {
                onKeyboardHiddenListener.onKeyboardHidden(this);
            }
            return true;
        }
        return false;
    }

    public interface OnKeyboardHiddenListener {
        void onKeyboardHidden(CustomAutoCompleteTextView editText);
    }
}
