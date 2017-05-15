package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.HashSet;
import java.util.Set;

import butterknife.ButterKnife;
import lombok.Getter;
import ru.android.childdiary.utils.ObjectUtils;

public abstract class FieldValueView<T> extends LinearLayout implements FieldReadOnly {
    private final Set<ValueChangeListener<T>> valueChangeListeners = new HashSet<>();

    @Getter
    private T value;

    public FieldValueView(Context context) {
        super(context);
        init();
    }

    public FieldValueView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FieldValueView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init() {
        inflate(getContext(), getLayoutResourceId(), this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setReadOnly(false);
        valueChanged();
    }

    public void setValue(T value) {
        if (!ObjectUtils.equals(value, this.value)) {
            this.value = value;
            valueChanged();
            for (ValueChangeListener<T> listener : valueChangeListeners) {
                listener.onValueChange(value);
            }
        }
    }

    @LayoutRes
    protected abstract int getLayoutResourceId();

    protected abstract void valueChanged();

    public void addValueChangeListener(@NonNull ValueChangeListener<T> listener) {
        valueChangeListeners.add(listener);
    }

    public void removeValueChangeListener(@NonNull ValueChangeListener<T> listener) {
        valueChangeListeners.remove(listener);
    }

    public interface ValueChangeListener<T> {
        void onValueChange(@Nullable T value);
    }
}
