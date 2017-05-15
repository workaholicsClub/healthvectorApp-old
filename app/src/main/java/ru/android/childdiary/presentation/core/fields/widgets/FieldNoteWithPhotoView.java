package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import lombok.Setter;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class FieldNoteWithPhotoView extends FieldEditTextView implements FieldReadOnly, View.OnTouchListener {
    private static final int DRAWABLE_RIGHT = 2;

    @BindView(R.id.editText)
    CustomEditText editText;

    @BindView(R.id.imageViewContainer)
    View imageViewContainer;

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.buttonDeletePhoto)
    Button buttonDeletePhoto;

    @Nullable
    @Getter
    private String imageFileName;

    @Nullable
    @Setter
    private PhotoListener photoListener;

    public FieldNoteWithPhotoView(Context context) {
        super(context);
        init();
    }

    public FieldNoteWithPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FieldNoteWithPhotoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.field_note_with_photo, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        editText.setOnTouchListener(this);
        update();
    }

    public String getText() {
        return editText.getText().toString().trim();
    }

    public void setText(String text) {
        editText.setText(text);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int rightDrawableWidth = editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
            int right = editText.getRight() - rightDrawableWidth;
            if (event.getRawX() >= right) {
                if (photoListener != null) {
                    photoListener.requestPhotoAdd();
                }
                return true;
            }
        }
        return false;
    }

    @OnClick(R.id.buttonDeletePhoto)
    void onButtonDeletePhotoClick() {
        if (photoListener != null) {
            photoListener.requestPhotoDelete();
        }
    }

    public void setImageFileName(@Nullable String imageFileName) {
        this.imageFileName = imageFileName;
        update();
    }

    private void update() {
        imageView.setImageDrawable(ResourcesUtils.getPhotoDrawable(getContext(), imageFileName));
        imageViewContainer.setVisibility(imageFileName == null ? GONE : VISIBLE);
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
        editText.setVisibility(TextUtils.isEmpty(getText()) ? INVISIBLE : VISIBLE);
        editText.setEnabled(!readOnly);
        buttonDeletePhoto.setVisibility(readOnly ? GONE : VISIBLE);
    }

    public interface PhotoListener {
        void requestPhotoAdd();

        void requestPhotoDelete();
    }
}
