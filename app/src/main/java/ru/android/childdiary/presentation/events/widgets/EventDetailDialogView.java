package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.Getter;
import lombok.Setter;
import ru.android.childdiary.R;

public abstract class EventDetailDialogView<T> extends LinearLayout {
    @BindView(R.id.textView)
    TextView textView;

    @Setter
    private EventDetailDialogListener eventDetailDialogListener;
    @Getter
    private T value;

    public EventDetailDialogView(Context context) {
        super(context);
        init();
    }

    public EventDetailDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailDialogView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), getLayoutResourceId(), this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void setValue(@Nullable T value) {
        this.value = value;
        textView.setText(getTextForValue(value));
    }

    @OnClick(R.id.textView)
    void onTextViewClick() {
        if (eventDetailDialogListener != null) {
            eventDetailDialogListener.requestValueChange(this);
        }
    }

    @LayoutRes
    protected abstract int getLayoutResourceId();

    protected abstract String getTextForValue(T value);

    public interface EventDetailDialogListener {
        void requestValueChange(EventDetailDialogView view);
    }
}
