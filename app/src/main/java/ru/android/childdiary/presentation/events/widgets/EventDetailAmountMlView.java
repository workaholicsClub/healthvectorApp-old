package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.util.AttributeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;

public class EventDetailAmountMlView extends EventDetailEditTextView {
    @BindView(R.id.editText)
    CustomEditText editText;

    @Getter
    private Double amountMl;

    public EventDetailAmountMlView(Context context) {
        super(context);
        init();
    }

    public EventDetailAmountMlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailAmountMlView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.event_detail_amount_ml, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Override
    public void setOnKeyboardHiddenListener(CustomEditText.OnKeyboardHiddenListener listener) {
        editText.setOnKeyboardHiddenListener(listener);
    }
}
