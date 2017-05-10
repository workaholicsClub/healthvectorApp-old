package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import org.joda.time.LocalTime;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.core.LengthValue;
import ru.android.childdiary.domain.interactors.core.LinearGroups;
import ru.android.childdiary.domain.interactors.core.PeriodicityType;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;

public class FieldRepeatParametersView extends LinearLayout
        implements FieldSpinnerView.FieldSpinnerListener, FieldReadOnly {
    @BindView(R.id.frequencyView)
    FieldFrequencyView frequencyView;

    @BindView(R.id.periodicityView)
    FieldPeriodicityView periodicityView;

    @BindView(R.id.lengthView)
    FieldLengthView lengthView;

    @BindView(R.id.timesTitle)
    View timesTitle;

    @BindView(R.id.timesView)
    FieldTimesView timesView;

    @Nullable
    private RepeatParameters repeatParameters;

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
        // TODO dialog listener lengthView.setFieldSpinnerListener(this);
    }

    public void updateFrequency(List<Integer> items) {
        frequencyView.updateAdapter(items);
    }

    public void updatePeriodicity(List<PeriodicityType> items) {
        periodicityView.updateAdapter(items);
    }

    @Override
    public void onSpinnerItemClick(FieldSpinnerView view, Object item) {
        if (view == frequencyView) {
            Integer number = (Integer) item;
            frequencyView.setValue(number);
            timesView.setNumber(number);
            timesTitle.setVisibility(number > 0 ? VISIBLE : GONE);
        } else if (view == periodicityView) {
            periodicityView.setValue((PeriodicityType) item);
        }
    }

    public void setRepeatParameters(@Nullable RepeatParameters repeatParameters) {
        this.repeatParameters = repeatParameters;
        LinearGroups linearGroups = repeatParameters == null ? null : repeatParameters.getFrequency();
        PeriodicityType periodicity = repeatParameters == null ? null : repeatParameters.getPeriodicity();
        LengthValue length = repeatParameters == null ? null : repeatParameters.getLength();
        frequencyView.setValue(linearGroups == null ? null : linearGroups.getTimes().size());
        timesView.setLinearGroups(linearGroups);
        periodicityView.setValue(periodicity);
        lengthView.setValue(length);
    }

    public RepeatParameters getRepeatParameters() {
        LinearGroups linearGroups = timesView.getLinearGroups();
        PeriodicityType periodicity = periodicityView.getValue();
        LengthValue length = lengthView.getValue();
        RepeatParameters.RepeatParametersBuilder builder = repeatParameters == null
                ? RepeatParameters.builder() : repeatParameters.toBuilder();
        return builder
                .frequency(linearGroups)
                .periodicity(periodicity)
                .length(length)
                .build();
    }

    public void setStartTime(LocalTime startTime) {
        timesView.setStartTime(startTime);
    }

    public void setFinishTime(LocalTime finishTime) {
        timesView.setFinishTime(finishTime);
    }

    public boolean dismissPopupWindow() {
        return frequencyView.dismissPopupWindow()
                || periodicityView.dismissPopupWindow();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        frequencyView.setReadOnly(readOnly);
        timesView.setReadOnly(readOnly);
        periodicityView.setReadOnly(readOnly);
        lengthView.setReadOnly(readOnly);
    }
}
