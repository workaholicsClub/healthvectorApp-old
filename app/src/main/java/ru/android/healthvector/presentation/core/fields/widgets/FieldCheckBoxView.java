package ru.android.healthvector.presentation.core.fields.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import lombok.Setter;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.presentation.core.bindings.FieldValueChangeListener;
import ru.android.healthvector.utils.ObjectUtils;
import ru.android.healthvector.utils.ui.ResourcesUtils;

public class FieldCheckBoxView extends BaseFieldValueView<Boolean> implements View.OnClickListener,
        FieldValueChangeListener<Boolean> {
    @BindView(R.id.contentView)
    View contentView;

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.textView)
    TextView textView;

    @Nullable
    private Sex sex;
    private String text;
    private boolean enabled = true;

    @Nullable
    @Setter
    private FieldCheckBoxListener fieldCheckBoxListener;

    public FieldCheckBoxView(Context context) {
        super(context);
    }

    public FieldCheckBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldCheckBoxView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init(@Nullable AttributeSet attrs) {
        super.init(attrs);
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.FieldCheckBoxView, 0, 0);
            try {
                text = ta.getString(R.styleable.FieldCheckBoxView_fieldText);
            } finally {
                ta.recycle();
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setOnClickListener(this);
        addValueChangeListener(this);
        textView.setText(text);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_check_box;
    }

    @Override
    protected void valueChanged() {
        update();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
    }

    @Override
    public void onValueChange(@Nullable Boolean value) {
        if (fieldCheckBoxListener != null) {
            fieldCheckBoxListener.onChecked();
        }
    }

    public boolean isChecked() {
        return ObjectUtils.isTrue(getValue());
    }

    public void setChecked(boolean value) {
        setValue(value);
    }

    public void setSex(@Nullable Sex sex) {
        if (this.sex != sex) {
            this.sex = sex;
            update();
        }
    }

    public void setText(@Nullable String text) {
        this.text = text;
        textView.setText(text);
    }

    private void update() {
        imageView.setImageResource(ResourcesUtils.getCheckBoxRes(sex, isChecked(), enabled));
        //noinspection deprecation
        textView.setTextAppearance(getContext(), isChecked() ? R.style.PrimaryTextAppearance : R.style.SecondaryTextAppearance);
        textView.setTypeface(typeface);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        setOnClickListener(enabled ? this : null);
        //noinspection deprecation
        contentView.setBackgroundResource(enabled ? R.drawable.background_clickable : 0);
        imageView.setImageResource(ResourcesUtils.getCheckBoxRes(sex, isChecked(), enabled));
        //noinspection deprecation
        textView.setTextAppearance(getContext(), enabled ? R.style.PrimaryTextAppearance : R.style.SecondaryTextAppearance);
        textView.setTypeface(typeface);
    }

    @Override
    public void onClick(View v) {
        setValue(!isChecked());
    }

    public interface FieldCheckBoxListener {
        void onChecked();
    }
}
