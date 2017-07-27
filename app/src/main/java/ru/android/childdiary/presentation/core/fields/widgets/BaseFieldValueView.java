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
import lombok.val;
import ru.android.childdiary.presentation.core.bindings.FieldValueChangeListener;
import ru.android.childdiary.presentation.core.bindings.FieldValueView;
import ru.android.childdiary.utils.ObjectUtils;

public abstract class BaseFieldValueView<T> extends LinearLayout implements FieldValueView<T>, FieldReadOnly {
    private final Set<FieldValueChangeListener<T>> valueChangeListeners = new HashSet<>();

    @Getter
    private T value;

    public BaseFieldValueView(Context context) {
        super(context);
        init(null);
    }

    public BaseFieldValueView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BaseFieldValueView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    protected void init(@Nullable AttributeSet attrs) {
        inflate(getContext(), getLayoutResourceId(), this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setReadOnly(false);
        valueChanged();
    }

    public void setValue(@Nullable T value) {
        if (!ObjectUtils.equals(value, this.value)) {
            this.value = value;
            valueChanged();
            for (val listener : valueChangeListeners) {
                listener.onValueChange(value);
            }
        }
    }

    @LayoutRes
    protected abstract int getLayoutResourceId();

    protected abstract void valueChanged();

    @Override
    public void addValueChangeListener(@NonNull FieldValueChangeListener<T> listener) {
        valueChangeListeners.add(listener);
    }

    @Override
    public void removeValueChangeListener(@NonNull FieldValueChangeListener<T> listener) {
        valueChangeListeners.remove(listener);
    }
}
