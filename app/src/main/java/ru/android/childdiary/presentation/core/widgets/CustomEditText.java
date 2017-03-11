package ru.android.childdiary.presentation.core.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

import lombok.Setter;

public class CustomEditText extends AppCompatEditText {
    @Setter
    private OnKeyboardHiddenListener onKeyboardHiddenListener;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (onKeyboardHiddenListener != null) {
                onKeyboardHiddenListener.onKeyboardHidden(this);
                return true;
            }
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public interface OnKeyboardHiddenListener {
        void onKeyboardHidden(CustomEditText editText);
    }
}
