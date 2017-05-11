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
import lombok.Setter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.core.LengthValue;
import ru.android.childdiary.domain.interactors.core.LinearGroups;
import ru.android.childdiary.domain.interactors.core.PeriodicityType;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;

public class FieldRepeatParametersView extends LinearLayout
        implements FieldSpinnerView.FieldSpinnerListener, FieldDialogView.FieldDialogListener, FieldReadOnly {
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

    @Nullable
    @Setter
    private Listener listener;

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
        lengthView.setFieldDialogListener(this);
    }

    public void setSex(@Nullable Sex sex) {
        timesView.setSex(sex);
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
            timesTitle.setVisibility(number != null && number > 0 ? VISIBLE : GONE);
            timesView.setNumber(number);
        } else if (view == periodicityView) {
            periodicityView.setValue((PeriodicityType) item);
        }
    }

    @Override
    public void requestValueChange(FieldDialogView view) {
        if (view == lengthView) {
            if (listener != null) {
                listener.showLengthValueDialog();
            }
        }
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

    public void setRepeatParameters(@Nullable RepeatParameters repeatParameters) {
        this.repeatParameters = repeatParameters;
        LinearGroups linearGroups = repeatParameters == null ? null : repeatParameters.getFrequency();
        PeriodicityType periodicity = repeatParameters == null ? null : repeatParameters.getPeriodicity();
        LengthValue length = repeatParameters == null ? null : repeatParameters.getLength();

        Integer number = linearGroups == null ? null : linearGroups.getTimes().size();
        frequencyView.setValue(number);
        timesTitle.setVisibility(number != null && number > 0 ? VISIBLE : GONE);
        timesView.setNumber(number);

        timesView.setLinearGroups(linearGroups);
        periodicityView.setValue(periodicity);
        lengthView.setValue(length);
    }

    @Nullable
    public LengthValue getLengthValue() {
        return lengthView.getValue();
    }

    public void setLengthValue(@Nullable LengthValue lengthValue) {
        lengthView.setValue(lengthValue);
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

    public void setFieldTimesListener(FieldTimesView.FieldTimesListener fieldTimesListener) {
        timesView.setFieldTimesListener(fieldTimesListener);
    }

    public void setTime(int i, LocalTime time) {
        timesView.setTime(i, time);
    }

    public interface Listener {
        void showLengthValueDialog();
    }
}
