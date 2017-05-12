package ru.android.childdiary.domain.interactors.medical.validation;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.childdiary.domain.core.Validator;
import ru.android.childdiary.domain.interactors.calendar.validation.CalendarValidationResult;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;

public class DoctorVisitValidator extends Validator<DoctorVisit, CalendarValidationResult> {
    private final Context context;

    @Inject
    public DoctorVisitValidator(Context context) {
        this.context = context;
    }

    @Override
    public List<CalendarValidationResult> validate(@NonNull DoctorVisit doctorVisit) {
        List<CalendarValidationResult> results = new ArrayList<>();
        return results;
    }
}
