package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
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

public abstract class EventDetailDialogView<T> extends LinearLayout implements
        View.OnClickListener, ReadOnlyView {
    @BindView(R.id.textViewWrapper)
    View textViewWrapper;

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.imageView)
    ImageView imageView;

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

    @Override
    public void onClick(View view) {
        if (view == textViewWrapper) {
            if (eventDetailDialogListener != null) {
                eventDetailDialogListener.requestValueChange(this);
            }
        }
    }

    @LayoutRes
    protected abstract int getLayoutResourceId();

    protected abstract String getTextForValue(T value);

    @Override
    public void setReadOnly(boolean readOnly) {
        textViewWrapper.setOnClickListener(readOnly ? null : this);
        textViewWrapper.setBackgroundResource(readOnly ? 0 : R.drawable.background_clickable);
        imageView.setVisibility(readOnly ? INVISIBLE : VISIBLE);
    }

    public interface EventDetailDialogListener {
        void requestValueChange(EventDetailDialogView view);
    }
}
