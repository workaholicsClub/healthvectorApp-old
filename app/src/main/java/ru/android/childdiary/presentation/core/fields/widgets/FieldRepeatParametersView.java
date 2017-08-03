package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import org.joda.time.LocalTime;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import lombok.Setter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.calendar.data.core.LengthValue;
import ru.android.childdiary.domain.interactors.calendar.data.core.LinearGroups;
import ru.android.childdiary.domain.interactors.calendar.data.core.PeriodicityType;
import ru.android.childdiary.domain.interactors.calendar.data.core.RepeatParameters;
import ru.android.childdiary.presentation.core.bindings.FieldValueChangeEventsObservable;
import ru.android.childdiary.presentation.core.bindings.RxFieldValueView;

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
    @State
    PeriodicityType lastSelectedPeriodicityType;

    @Nullable
    @State
    LengthValue lastSelectedLengthValue;

    @Nullable
    private RepeatParameters repeatParameters;
    private boolean readOnly;

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
        //noinspection unchecked
        frequencyView.setFieldSpinnerListener(this);
        //noinspection unchecked
        periodicityView.setFieldSpinnerListener(this);
        lengthView.setFieldDialogListener(this);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        return Icepick.saveInstanceState(this, super.onSaveInstanceState());
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(Icepick.restoreInstanceState(this, state));
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
            setupFrequency(number);

            boolean once = number != null && number == 0;
            if (once) {
                // Однократно
                periodicityView.setValue(null);
                lengthView.setValue(null);
            } else {
                periodicityView.setValue(lastSelectedPeriodicityType);
                lengthView.setValue(lastSelectedLengthValue);
            }

            setupReadOnly();
        } else if (view == periodicityView) {
            PeriodicityType periodicityType = (PeriodicityType) item;
            periodicityView.setValue(periodicityType);

            lastSelectedPeriodicityType = periodicityView.getValue();
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
        LinearGroups linearGroups = timesView.getValue();
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
        setupFrequency(number);

        timesView.setValue(linearGroups);
        periodicityView.setValue(periodicity);
        lengthView.setValue(length);

        lastSelectedPeriodicityType = periodicityView.getValue();
        lastSelectedLengthValue = lengthView.getValue();

        setupReadOnly();
    }

    private void setupFrequency(@Nullable Integer number) {
        frequencyView.setValue(number);
        int timesViewVisibility = number != null && number > 1 ? VISIBLE : GONE;
        timesTitle.setVisibility(timesViewVisibility);
        timesView.setVisibility(timesViewVisibility);
        timesView.setNumber(number);
    }

    @Nullable
    public LengthValue getLengthValue() {
        return lengthView.getValue();
    }

    public void setLengthValue(@Nullable LengthValue lengthValue) {
        lengthView.setValue(lengthValue);

        lastSelectedLengthValue = lengthView.getValue();
    }

    public void setTimeLimits(@NonNull LocalTime startTime, @NonNull LocalTime finishTime) {
        timesView.setStartTime(startTime);
        timesView.setFinishTime(finishTime);
    }

    public boolean dismissPopupWindow() {
        return frequencyView.dismissPopupWindow()
                || periodicityView.dismissPopupWindow();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        setupReadOnly();
    }

    private void setupReadOnly() {
        frequencyView.setReadOnly(readOnly);
        timesView.setReadOnly(readOnly);
        if (readOnly) {
            periodicityView.setReadOnly(true);
            lengthView.setReadOnly(true);
        } else {
            Integer number = frequencyView.getValue();
            boolean once = number != null && number == 0;
            periodicityView.setReadOnly(once);
            lengthView.setReadOnly(once);
        }
    }

    public void setFieldTimesListener(FieldTimesView.FieldTimesListener fieldTimesListener) {
        timesView.setFieldTimesListener(fieldTimesListener);
    }

    public void setTime(int i, LocalTime time) {
        timesView.setTime(i, time);
    }

    public FieldValueChangeEventsObservable<LinearGroups> linearGroupsObservable() {
        return RxFieldValueView.valueChangeEvents(timesView);
    }

    public FieldValueChangeEventsObservable<PeriodicityType> periodicityTypeObservable() {
        return RxFieldValueView.valueChangeEvents(periodicityView);
    }

    public FieldValueChangeEventsObservable<LengthValue> lengthValueObservable() {
        return RxFieldValueView.valueChangeEvents(lengthView);
    }

    public interface Listener {
        void showLengthValueDialog();
    }
}
