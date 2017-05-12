package ru.android.childdiary.domain.interactors.medical.validation;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.childdiary.domain.core.Validator;
import ru.android.childdiary.domain.interactors.calendar.validation.CalendarValidationResult;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;

public class MedicineTakingValidator extends Validator<MedicineTaking, CalendarValidationResult> {
    private final Context context;

    @Inject
    public MedicineTakingValidator(Context context) {
        this.context = context;
    }

    @Override
    public List<CalendarValidationResult> validate(@NonNull MedicineTaking medicineTaking) {
        List<CalendarValidationResult> results = new ArrayList<>();
        return results;
    }
}
