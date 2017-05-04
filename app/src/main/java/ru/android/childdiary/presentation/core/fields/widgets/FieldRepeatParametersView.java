package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import ru.android.childdiary.R;

public class FieldRepeatParametersView extends LinearLayout implements View.OnClickListener {
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
        setOrientation(LinearLayout.VERTICAL);

        View child;
        TextView textView;

        child = inflate(getContext(), R.layout.field_frequency, null);
        addView(child);
        textView = ButterKnife.findById(child, R.id.textView);
        textView.setOnClickListener(this);

        child = inflate(getContext(), R.layout.field_periodicity, null);
        addView(child);
        textView = ButterKnife.findById(child, R.id.textView);
        textView.setOnClickListener(this);

        child = inflate(getContext(), R.layout.field_length, null);
        addView(child);
        textView = ButterKnife.findById(child, R.id.textView);
        textView.setOnClickListener(this);

        // TODO Время выполнения
    }

    @Override
    public void onClick(View v) {
    }
}
