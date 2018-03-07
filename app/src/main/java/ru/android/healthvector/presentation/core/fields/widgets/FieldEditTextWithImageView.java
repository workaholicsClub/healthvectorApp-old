package ru.android.healthvector.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;

import butterknife.BindView;
import lombok.Getter;
import ru.android.healthvector.R;
import ru.android.healthvector.presentation.core.widgets.CustomEditText;

public class FieldEditTextWithImageView extends FieldEditTextWithImageBaseView {
    @Getter
    @BindView(R.id.editText)
    CustomEditText editText;

    public FieldEditTextWithImageView(Context context) {
        super(context);
    }

    public FieldEditTextWithImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldEditTextWithImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_edit_text_with_image;
    }
}
