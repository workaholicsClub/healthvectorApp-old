package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.utils.ui.FontUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public abstract class FieldRadioView<T extends Enum<T>> extends LinearLayout implements View.OnClickListener {
    private static final boolean DEFAULT_SHOW_ICON = true;

    private final Typeface typeface = FontUtils.getTypefaceRegular(getContext());
    private final List<TextView> texts = new ArrayList<>();
    private final List<ImageView> radios = new ArrayList<>();
    private final int margin;

    private boolean showIcon = DEFAULT_SHOW_ICON;

    @Nullable
    private T[] values;

    @Nullable
    private Sex sex;

    @Getter
    private T selected;

    public FieldRadioView(Context context) {
        super(context);
        margin = getResources().getDimensionPixelSize(R.dimen.base_margin);
        init(null);
    }

    public FieldRadioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        margin = getResources().getDimensionPixelSize(R.dimen.base_margin);
        init(attrs);
    }

    public FieldRadioView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        margin = getResources().getDimensionPixelSize(R.dimen.base_margin);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.FieldRadioView, 0, 0);
            try {
                showIcon = ta.getBoolean(R.styleable.FieldRadioView_fieldShowIcon, DEFAULT_SHOW_ICON);
            } finally {
                ta.recycle();
            }
        }
        setOrientation(LinearLayout.VERTICAL);
        update();
    }

    private void update() {
        texts.clear();
        radios.clear();
        removeAllViews();
        View child = inflate(getContext(), getTitleLayoutResourceId(), null);
        addView(child);
        T[] items = getItems();
        for (T value : items) {
            setupRadioItem(value);
        }
    }

    private void setupRadioItem(T value) {
        View child = inflate(getContext(), R.layout.field_radio, null);
        addView(child);
        child.setOnClickListener(this);
        child.setTag(value);
        TextView textView = child.findViewById(R.id.textView);
        textView.setText(getTextForValue(value));
        texts.add(textView);
        ImageView radio = child.findViewById(R.id.imageView);
        radios.add(radio);
        View marginView = child.findViewById(R.id.marginView);
        marginView.setVisibility(showIcon ? VISIBLE : GONE);
        if (!showIcon) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
            layoutParams.leftMargin = margin;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                layoutParams.setMarginStart(margin);
            }
            textView.setLayoutParams(layoutParams);
        }
    }

    protected abstract Class<T> getEnumType();

    @LayoutRes
    protected abstract int getTitleLayoutResourceId();

    @Nullable
    protected abstract String getTextForValue(@Nullable T value);

    @Override
    public void onClick(View v) {
        //noinspection unchecked
        setSelected((T) v.getTag());
    }

    public void setSex(@Nullable Sex sex) {
        if (this.sex != sex) {
            this.sex = sex;
            updateItems();
        }
    }

    public void setSelected(T value) {
        selected = value;
        updateItems();
    }

    public void setValues(@Nullable T[] values) {
        this.values = values;
        update();
    }

    public void selectFirst() {
        T[] items = getItems();
        setSelected(items[0]);
    }

    private T[] getItems() {
        return values == null ? getEnumType().getEnumConstants() : values;
    }

    private void updateItems() {
        int i = 0;
        T[] items = getItems();
        for (T value : items) {
            radios.get(i).setImageResource(ResourcesUtils.getRadioRes(sex, value == selected));
            boolean enabled = value == selected;
            //noinspection deprecation
            texts.get(i).setTextAppearance(getContext(), enabled ? R.style.PrimaryTextAppearance : R.style.SecondaryTextAppearance);
            texts.get(i).setTypeface(typeface);
            ++i;
        }
    }
}
