package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;
import lombok.Setter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.utils.ui.FontUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class FieldCheckBoxView extends LinearLayout implements View.OnClickListener {
    private final Typeface typeface = FontUtils.getTypefaceRegular(getContext());
    private Sex sex;
    @Getter
    private boolean checked;

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.textView)
    TextView textView;

    @Nullable
    @Setter
    private FieldCheckBoxListener fieldCheckBoxListener;

    public FieldCheckBoxView(Context context) {
        super(context);
        init();
    }

    public FieldCheckBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FieldCheckBoxView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.field_check_box, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setOnClickListener(this);
    }

    public void setChecked(boolean value) {
        checked = value;
        update();
        if (fieldCheckBoxListener != null) {
            fieldCheckBoxListener.onChecked(checked);
        }
    }

    public void setText(String value) {
        textView.setText(value);
    }

    public void setText(@StringRes int res) {
        textView.setText(res);
    }

    public void setSex(Sex sex) {
        if (this.sex != sex) {
            this.sex = sex;
            update();
        }
    }

    private void update() {
        imageView.setImageResource(ResourcesUtils.getCheckBoxRes(sex, checked));
        //noinspection deprecation
        textView.setTextAppearance(getContext(), checked ? R.style.PrimaryTextAppearance : R.style.SecondaryTextAppearance);
        textView.setTypeface(typeface);
    }

    @Override
    public void onClick(View v) {
        setChecked(!checked);
    }

    public interface FieldCheckBoxListener {
        void onChecked(boolean value);
    }
}
