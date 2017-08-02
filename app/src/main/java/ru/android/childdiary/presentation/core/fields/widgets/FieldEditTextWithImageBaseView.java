package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.widgets.OnKeyboardHiddenListener;
import ru.android.childdiary.presentation.core.widgets.OnKeyboardHiddenListenerContainer;

public abstract class FieldEditTextWithImageBaseView extends FieldEditTextView implements FieldReadOnly {
    @BindView(R.id.imageView)
    ImageView imageView;

    @BindDimen(R.dimen.name_edit_text_padding_bottom)
    int editTextBottomPadding;

    private Drawable icon;
    private String hint;
    private int maxLength;
    private boolean hideIfEmpty;

    public FieldEditTextWithImageBaseView(Context context) {
        super(context);
    }

    public FieldEditTextWithImageBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldEditTextWithImageBaseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init(@Nullable AttributeSet attrs) {
        inflate(getContext(), getLayoutResourceId(), this);
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.FieldEditTextWithImageView, 0, 0);
            try {
                icon = ta.getDrawable(R.styleable.FieldEditTextWithImageView_fieldIcon);
                hint = ta.getString(R.styleable.FieldEditTextWithImageView_fieldHint);
                maxLength = ta.getInt(R.styleable.FieldEditTextWithImageView_fieldMaxLength, 0);
                hideIfEmpty = ta.getBoolean(R.styleable.FieldEditTextWithImageView_fieldHideIfEmpty, false);
            } finally {
                ta.recycle();
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageView.setImageDrawable(icon);
        getEditText().setHint(hint);
        getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    public String getText() {
        return getEditText().getText().toString().trim();
    }

    public void setText(String text) {
        getEditText().setText(text);
    }

    @Override
    public List<Disposable> createSubscriptions(OnKeyboardHiddenListener listener) {
        if (getEditText() instanceof OnKeyboardHiddenListenerContainer) {
            ((OnKeyboardHiddenListenerContainer) getEditText()).setOnKeyboardHiddenListener(listener);
        }

        List<Disposable> disposables = new ArrayList<>();

        disposables.add(RxView.focusChanges(getEditText()).subscribe(hasFocus -> {
            if (hasFocus) {
                getEditText().setSelection(getEditText().getText().length());
            }
        }));

        disposables.add(RxTextView.editorActionEvents(getEditText()).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_DONE) {
                listener.onKeyboardHidden(getEditText());
            }
        }));

        return disposables;
    }

    public Observable<TextViewAfterTextChangeEvent> textObservable() {
        return RxTextView.afterTextChangeEvents(getEditText());
    }

    public void validated(boolean valid) {
        getEditText().setBackgroundResource(valid ? R.drawable.edit_text_background : R.drawable.edit_text_background_error);
        getEditText().setPadding(0, 0, 0, editTextBottomPadding);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        setVisibility(readOnly && TextUtils.isEmpty(getText()) && hideIfEmpty ? GONE : VISIBLE);
        getEditText().setEnabled(!readOnly);
    }

    @LayoutRes
    protected abstract int getLayoutResourceId();

    protected abstract EditText getEditText();
}
