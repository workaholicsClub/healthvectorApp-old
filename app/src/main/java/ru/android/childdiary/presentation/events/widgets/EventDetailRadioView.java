package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.utils.ui.FontUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public abstract class EventDetailRadioView<T extends Enum<T>> extends LinearLayout implements View.OnClickListener {
    private final Typeface typeface = FontUtils.getTypefaceRegular(getContext());
    private final List<TextView> texts = new ArrayList<>();
    private final List<ImageView> radios = new ArrayList<>();
    private Sex sex;
    @Getter
    private T selected;

    public EventDetailRadioView(Context context) {
        super(context);
        init();
    }

    public EventDetailRadioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailRadioView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        View child;
        child = inflate(getContext(), getTitleLayoutResourceId(), null);
        addView(child);
        for (T value : getEnumType().getEnumConstants()) {
            child = inflate(getContext(), R.layout.event_detail_radio, null);
            addView(child);
            child.setOnClickListener(this);
            child.setTag(value);
            TextView textView = ButterKnife.findById(child, R.id.textView);
            textView.setText(getTextForValue(value));
            texts.add(textView);
            ImageView radio = ButterKnife.findById(child, R.id.imageViewRadio);
            radios.add(radio);
        }
    }

    protected abstract Class<T> getEnumType();

    @LayoutRes
    protected abstract int getTitleLayoutResourceId();

    protected abstract String getTextForValue(@Nullable T value);

    @Override
    public void onClick(View v) {
        //noinspection unchecked
        setSelected((T) v.getTag());
    }

    public void setSex(Sex sex) {
        if (this.sex != sex) {
            this.sex = sex;
            updateItems();
        }
    }

    public void setSelected(T value) {
        selected = value;
        updateItems();
    }

    private void updateItems() {
        int i = 0;
        for (T value : getEnumType().getEnumConstants()) {
            radios.get(i).setImageResource(ResourcesUtils.getRadioRes(sex, value == selected));
            boolean enabled = value == selected;
            //noinspection deprecation
            texts.get(i).setTextAppearance(getContext(), enabled ? R.style.PrimaryTextAppearance : R.style.SecondaryTextAppearance);
            texts.get(i).setTypeface(typeface);
            ++i;
        }
    }
}
