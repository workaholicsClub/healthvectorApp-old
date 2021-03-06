package ru.android.healthvector.presentation.core.fields.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import lombok.Setter;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.presentation.core.widgets.CustomEditText;
import ru.android.healthvector.presentation.core.widgets.OnKeyboardHiddenListener;
import ru.android.healthvector.utils.ui.ResourcesUtils;

public class FieldNoteWithPhotoView extends FieldEditTextView implements FieldReadOnly, View.OnTouchListener {
    private static final int DRAWABLE_RIGHT = 2;

    @BindView(R.id.editText)
    CustomEditText editText;

    @BindView(R.id.imageViewContainer)
    View imageViewContainer;

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.buttonDeletePhoto)
    View buttonDeletePhoto;

    @Nullable
    @Getter
    private String imageFileName;

    @Nullable
    @Setter
    private PhotoListener photoListener;

    @Nullable
    private Sex sex;

    private String hint;

    public FieldNoteWithPhotoView(Context context) {
        super(context);
    }

    public FieldNoteWithPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldNoteWithPhotoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init(@Nullable AttributeSet attrs) {
        inflate(getContext(), R.layout.field_note_with_photo, this);
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.FieldNoteWithPhotoView, 0, 0);
            try {
                hint = ta.getString(R.styleable.FieldNoteWithPhotoView_fieldHint);
            } finally {
                ta.recycle();
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
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
            Drawable drawableRight = editText.getCompoundDrawables()[DRAWABLE_RIGHT];
            if (drawableRight == null) {
                return false;
            }
            int rightDrawableWidth = drawableRight.getBounds().width();
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

    @OnClick(R.id.imageView)
    void onImageViewClick() {
        if (photoListener != null) {
            photoListener.requestPhotoReview();
        }
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
        Drawable photo = ResourcesUtils.getPhotoDrawable(getContext(), imageFileName);
        imageView.setImageDrawable(photo);
        imageViewContainer.setVisibility(photo == null ? GONE : VISIBLE);
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

    @Override
    public void setReadOnly(boolean readOnly) {
        if (TextUtils.isEmpty(getText()) && TextUtils.isEmpty(imageFileName)) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }
        editText.setHint(readOnly ? null : hint);
        @DrawableRes int right = readOnly ? 0 : R.drawable.ic_add_photo;
        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, right, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, right, 0);
        }
        editText.setEnabled(!readOnly);
        buttonDeletePhoto.setVisibility(readOnly ? GONE : VISIBLE);
    }

    public void setSex(@Nullable Sex sex) {
        if (this.sex != sex) {
            this.sex = sex;
            buttonDeletePhoto.setBackgroundResource(ResourcesUtils.getCircleButtonRes(sex));
        }
    }

    public interface PhotoListener {
        void requestPhotoAdd();

        void requestPhotoReview();

        void requestPhotoDelete();
    }
}
