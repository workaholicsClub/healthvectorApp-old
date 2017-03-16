package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;

public class EventDetailAmountMlPumpView extends EventDetailEditTextView {
    private CustomEditText editTextAmountMl;
    private CustomEditText editTextAmountMlLeft;
    private CustomEditText editTextAmountMlRight;

    @Getter
    private Double amountMlLeft, amountMlRight;

    public EventDetailAmountMlPumpView(Context context) {
        super(context);
        init();
    }

    public EventDetailAmountMlPumpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailAmountMlPumpView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        View view;
        view = inflate(getContext(), R.layout.event_detail_amount_ml_pump, null);
        addView(view);
        editTextAmountMl = ButterKnife.findById(view, R.id.editText);
        view = inflate(getContext(), R.layout.event_detail_amount_ml_pump_left, null);
        addView(view);
        editTextAmountMlLeft = ButterKnife.findById(view, R.id.editText);
        view = inflate(getContext(), R.layout.event_detail_amount_ml_pump_right, null);
        addView(view);
        editTextAmountMlRight = ButterKnife.findById(view, R.id.editText);
    }

    @Override
    public void setOnKeyboardHiddenListener(CustomEditText.OnKeyboardHiddenListener listener) {
        editTextAmountMl.setOnKeyboardHiddenListener(listener);
        editTextAmountMlLeft.setOnKeyboardHiddenListener(listener);
        editTextAmountMlRight.setOnKeyboardHiddenListener(listener);
    }
}
