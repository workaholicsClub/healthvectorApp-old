package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;

public class EventDetailNoteView extends EventDetailEditTextView {
    @BindView(R.id.editTextWrapper)
    TextInputLayout editTextWrapper;

    @BindView(R.id.editText)
    CustomEditText editText;

    @BindView(R.id.placeholder)
    TextView placeholder;

    private boolean readOnly;

    public EventDetailNoteView(Context context) {
        super(context);
        init();
    }

    public EventDetailNoteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailNoteView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.event_detail_note, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    public String getText() {
        return editText.getText().toString().trim();
    }

    public void setText(String text) {
        editText.setText(text);
        setReadOnly(readOnly);
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

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        editTextWrapper.setEnabled(!readOnly);
        editTextWrapper.setBackgroundResource(readOnly ? 0 : R.drawable.edit_text_background);
        boolean isTextEmpty = TextUtils.isEmpty(editText.getText());
        editTextWrapper.setVisibility(isTextEmpty && readOnly ? GONE : VISIBLE);
        placeholder.setVisibility(isTextEmpty && readOnly ? VISIBLE : GONE);
    }
}
