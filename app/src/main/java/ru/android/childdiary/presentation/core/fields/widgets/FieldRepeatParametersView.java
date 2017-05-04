package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.childdiary.R;

public class FieldRepeatParametersView extends LinearLayout implements FieldSpinnerView.FieldSpinnerListener {
    @BindView(R.id.frequencyView)
    FieldFrequencyView frequencyView;

    @BindView(R.id.periodicityView)
    FieldPeriodicityView periodicityView;

    @BindView(R.id.lengthView)
    FieldLengthView lengthView;

    // TODO times

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
        setOrientation(VERTICAL);
        inflate(getContext(), R.layout.field_repeat_parameters, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        frequencyView.setFieldSpinnerListener(this);
        periodicityView.setFieldSpinnerListener(this);
        lengthView.setFieldSpinnerListener(this);
    }

    public void updateFrequency(List<Integer> items) {
        frequencyView.updateAdapter(items);
    }

    public void updatePeriodicity(List<Integer> items) {
        periodicityView.updateAdapter(items);
    }

    public void updateLength(List<Integer> items) {
        lengthView.updateAdapter(items);
    }

    @Override
    public void onSpinnerItemClick(FieldSpinnerView view, Object item) {
        if (view == frequencyView) {
            frequencyView.setValue((Integer) item);
        } else if (view == periodicityView) {
            periodicityView.setValue((Integer) item);
        } else if (view == lengthView) {
            lengthView.setValue((Integer) item);
        }
    }

    public interface FieldRepeatParametersListener {
    }
}
