package ru.android.childdiary.domain.interactors.calendar.validation;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.core.Validator;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.utils.ValueUtils;

public class PumpEventValidator extends Validator<PumpEvent, CalendarValidationResult> {
    private final Context context;

    @Inject
    public PumpEventValidator(Context context) {
        this.context = context;
    }

    @Override
    public List<CalendarValidationResult> validate(@NonNull PumpEvent event) {
        List<CalendarValidationResult> results = new ArrayList<>();

        switch (event.getBreast()) {
            case LEFT:
                if (!ValueUtils.hasValue(event.getLeftAmountMl())
                        && ValueUtils.hasValue(event.getRightAmountMl())) {
                    CalendarValidationResult result = new CalendarValidationResult();
                    result.addMessage(context.getString(R.string.validate_event_pump_fill_left_amount));
                    results.add(result);
                }
                break;
            case RIGHT:
                if (!ValueUtils.hasValue(event.getRightAmountMl())
                        && ValueUtils.hasValue(event.getLeftAmountMl())) {
                    CalendarValidationResult result = new CalendarValidationResult();
                    result.addMessage(context.getString(R.string.validate_event_pump_fill_right_amount));
                    results.add(result);
                }
                break;
        }

        return results;
    }
}
