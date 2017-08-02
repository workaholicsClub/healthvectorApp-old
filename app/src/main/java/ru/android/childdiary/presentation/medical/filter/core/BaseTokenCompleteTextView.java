package ru.android.childdiary.presentation.medical.filter.core;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;

import java.io.Serializable;

import butterknife.ButterKnife;
import lombok.Setter;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.widgets.OnKeyboardHiddenListener;

public abstract class BaseTokenCompleteTextView<T extends Serializable> extends TokenCompleteTextView<T> {
    @Setter
    @Nullable
    private OnKeyboardHiddenListener onKeyboardHiddenListener;

    public BaseTokenCompleteTextView(Context context) {
        super(context);
        init();
    }

    public BaseTokenCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseTokenCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setThreshold(1);
        allowCollapse(false);
        allowDuplicates(false);
        setSplitChar(' ');
        setTokenLimit(5);
        performBestGuess(true);
        setDeletionStyle(TokenDeleteStyle.Clear);
        setTokenClickStyle(TokenClickStyle.None);
    }

    @Override
    protected View getViewForObject(T item) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.token_item, (ViewGroup) getParent(), false);

        TextView textView = ButterKnife.findById(view, R.id.textView);
        textView.setText(getTextForValue(item));

        return view;
    }

    protected abstract String getTextForValue(T item);

    @Override
    protected TokenImageSpan buildSpanForObject(T obj) {
        if (obj == null || obj == defaultObject(null)) {
            return null;
        }
        View tokenView = getViewForObject(obj);
        return new TokenImageSpan(tokenView, obj, (int) maxTextWidth());
    }

    @Override
    public boolean onEditorAction(TextView view, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (onKeyboardHiddenListener != null) {
                onKeyboardHiddenListener.onKeyboardHidden(this);
            }
        }
        return super.onEditorAction(view, actionId, keyEvent);
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
}
