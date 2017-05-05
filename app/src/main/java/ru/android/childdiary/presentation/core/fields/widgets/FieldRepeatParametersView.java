package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import org.joda.time.LocalTime;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.childdiary.R;
import ru.android.childdiary.data.entities.calendar.events.core.LinearGroups;
import ru.android.childdiary.domain.interactors.calendar.events.core.RepeatParameters;

public class FieldRepeatParametersView extends LinearLayout implements FieldSpinnerView.FieldSpinnerListener {
    @BindView(R.id.frequencyView)
    FieldFrequencyView frequencyView;

    @BindView(R.id.periodicityView)
    FieldPeriodicityView periodicityView;

    @BindView(R.id.lengthView)
    FieldLengthView lengthView;

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
            Integer number = (Integer) item;
            frequencyView.setValue(number);
            timesView.setNumber(number);
        } else if (view == periodicityView) {
            periodicityView.setValue((Integer) item);
        } else if (view == lengthView) {
            lengthView.setValue((Integer) item);
        }
    }

    public void setRepeatParameters(@Nullable RepeatParameters repeatParameters) {
        this.repeatParameters = repeatParameters;
        LinearGroups linearGroups = repeatParameters == null ? null : repeatParameters.getLinearGroups();
        Integer periodicity = repeatParameters == null ? null : repeatParameters.getPeriodicityInMinutes();
        Integer length = repeatParameters == null ? null : repeatParameters.getLengthInMinutes();
        frequencyView.setValue(linearGroups == null ? null : linearGroups.getTimes().size());
        timesView.setLinearGroups(linearGroups);
        periodicityView.setValue(periodicity);
        lengthView.setValue(length);
    }

    public RepeatParameters getRepeatParameters() {
        LinearGroups linearGroups = timesView.getLinearGroups();
        Integer periodicity = periodicityView.getValue();
        Integer length = lengthView.getValue();
        RepeatParameters.RepeatParametersBuilder builder = repeatParameters == null
                ? RepeatParameters.builder() : repeatParameters.toBuilder();
        return builder
                .periodicityInMinutes(periodicity)
                .lengthInMinutes(length)
                .linearGroups(linearGroups)
                .build();
    }

    public void setStartTime(LocalTime startTime) {
        timesView.setStartTime(startTime);
    }

    public void setFinishTime(LocalTime finishTime) {
        timesView.setFinishTime(finishTime);
    }
}
