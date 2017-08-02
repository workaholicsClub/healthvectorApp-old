package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;
import ru.android.childdiary.presentation.core.widgets.OnKeyboardHiddenListener;

public class FieldNoteView extends FieldEditTextView {
    @BindView(R.id.editTextWrapper)
    TextInputLayout editTextWrapper;

    @BindView(R.id.editText)
    CustomEditText editText;

    public FieldNoteView(Context context) {
        super(context);
    }

    public FieldNoteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldNoteView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init(@Nullable AttributeSet attrs) {
        inflate(getContext(), R.layout.field_note, this);
    }

    public String getText() {
        return editText.getText().toString().trim();
    }

    public void setText(String text) {
        editText.setText(text);
    }

    @Override
    public List<Disposable> createSubscriptions(OnKeyboardHiddenListener listener) {
        editText.setOnKeyboardHiddenListener(listener);

        List<Disposable> disposables = new ArrayList<>();

        disposables.add(RxView.focusChanges(editText).subscribe(hasFocus -> {
            if (hasFocus) {
                editText.setSelection(editText.getText().length());
            }
        }));

        disposables.add(RxTextView.editorActionEvents(editText).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_DONE) {
                listener.onKeyboardHidden(editText);
            }
        }));

        return disposables;
    }
}
