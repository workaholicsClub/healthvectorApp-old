package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.childdiary.R;

public class FieldRepeatParametersView extends LinearLayout {
    @BindView(R.id.textView)
    TextView textView;

    public FieldRepeatParametersView(Context context) {
        super(context);
        init();
    }

    public FieldRepeatParametersView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FieldRepeatParametersView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.field_repeat_parameters, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void setTitle(String text) {
        textView.setText(text);
    }

    public void setTitle(@StringRes int res) {
        textView.setText(getContext().getString(res));
    }
}
