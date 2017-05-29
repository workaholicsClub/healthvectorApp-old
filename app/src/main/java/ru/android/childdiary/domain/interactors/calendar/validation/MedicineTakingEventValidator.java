package ru.android.childdiary.domain.interactors.calendar.validation;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.core.validation.Validator;
import ru.android.childdiary.domain.interactors.calendar.events.MedicineTakingEvent;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;

public class MedicineTakingEventValidator extends Validator<MedicineTakingEvent, CalendarValidationResult> {
    private final Context context;

    @Inject
    public MedicineTakingEventValidator(Context context) {
        this.context = context;
    }

    @Override
    public List<CalendarValidationResult> validate(@NonNull MedicineTakingEvent event) {
        List<CalendarValidationResult> results = new ArrayList<>();

        Medicine medicine = event.getMedicine();
        if (medicine == null || medicine.getId() == null) {
            CalendarValidationResult result = new CalendarValidationResult();
            result.addMessage(context.getString(R.string.validate_medicine_taking_medicine_empty));
            results.add(result);
        }

        return results;
    }
}
