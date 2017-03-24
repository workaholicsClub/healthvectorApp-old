package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;

public class EventDetailEditableTitleView extends EventDetailEditTextView {
    @BindView(R.id.editText)
    CustomEditText editText;

    @Getter
    private String title;

    public EventDetailEditableTitleView(Context context) {
        super(context);
        init();
    }

    public EventDetailEditableTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailEditableTitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.event_detail_editable_title, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    public void setTitle(String title) {
        this.title = title;
        editText.setText(title);
    }

    @Override
    public List<Disposable> createSubscriptions(CustomEditText.OnKeyboardHiddenListener listener) {
        editText.setOnKeyboardHiddenListener(listener);

        List<Disposable> disposables = new ArrayList<>();

        disposables.add(RxTextView.afterTextChangeEvents(editText).subscribe(textViewAfterTextChangeEvent -> {
            this.title = editText.getText().toString();
        }));

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
        editText.setEnabled(!readOnly);
        editText.setBackgroundResource(readOnly ? 0 : R.drawable.edit_text_background);
        editText.setHint(readOnly ? "" : getContext().getString(R.string.other_event_title_hint));
    }
}
