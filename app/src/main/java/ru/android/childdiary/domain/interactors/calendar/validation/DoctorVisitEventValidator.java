package ru.android.childdiary.domain.interactors.calendar.validation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.core.validation.ValidationException;
import ru.android.childdiary.domain.core.validation.Validator;
import ru.android.childdiary.domain.interactors.calendar.events.DoctorVisitEvent;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;

public class DoctorVisitEventValidator extends Validator<DoctorVisitEvent, CalendarValidationResult> {
    private final Context context;

    @Inject
    public DoctorVisitEventValidator(Context context) {
        this.context = context;
    }

    @Override
    public List<CalendarValidationResult> validate(@NonNull DoctorVisitEvent event) {
        List<CalendarValidationResult> results = new ArrayList<>();

        CalendarValidationResult result;

        result = new CalendarValidationResult(CalendarFieldType.DOCTOR_VISIT_EVENT_NAME);
        if (TextUtils.isEmpty(event.getName())) {
            result.addMessage(context.getString(R.string.validate_doctor_visit_name_empty));
        }
        results.add(result);

        Doctor doctor = event.getDoctor();
        if (doctor == null || doctor.getId() == null) {
            result = new CalendarValidationResult();
            result.addMessage(context.getString(R.string.validate_doctor_visit_doctor_empty));
            results.add(result);
        }

        return results;
    }

    @Override
    protected ValidationException createException(@NonNull List<CalendarValidationResult> results) {
        return new CalendarValidationException(results);
    }
}
