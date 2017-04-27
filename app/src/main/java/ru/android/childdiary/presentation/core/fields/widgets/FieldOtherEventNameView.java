package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;

public class FieldOtherEventNameView extends FieldEditTextView {
    @BindView(R.id.editText)
    CustomEditText editText;

    @BindDimen(R.dimen.name_edit_text_padding_bottom)
    int editTextBottomPadding;

    public FieldOtherEventNameView(Context context) {
        super(context);
        init();
    }

    public FieldOtherEventNameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FieldOtherEventNameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.field_other_event_name, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public String getText() {
        return editText.getText().toString().trim();
    }

    public void setText(String text) {
        editText.setText(text);
    }

    @Override
    public List<Disposable> createSubscriptions(CustomEditText.OnKeyboardHiddenListener listener) {
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

    public Observable<TextViewAfterTextChangeEvent> otherEventNameObservable() {
        return RxTextView.afterTextChangeEvents(editText);
    }

    public void validated(boolean valid) {
        editText.setBackgroundResource(valid ? R.drawable.edit_text_background : R.drawable.edit_text_background_error);
        editText.setPadding(0, 0, 0, editTextBottomPadding);
    }
}
