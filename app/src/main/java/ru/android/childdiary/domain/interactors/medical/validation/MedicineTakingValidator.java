package ru.android.childdiary.domain.interactors.medical.validation;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;

public class MedicineTakingValidator extends MedicalValidator<MedicineTaking> {
    @Inject
    public MedicineTakingValidator(Context context) {
        super(context);
    }

    @Override
    public List<MedicalValidationResult> validate(@NonNull MedicineTaking medicineTaking) {
        List<MedicalValidationResult> results = new ArrayList<>();

        if (medicineTaking.getMedicine() == null) {
            MedicalValidationResult result = new MedicalValidationResult();
            result.addMessage(context.getString(R.string.validate_medicine_taking_medicine_empty));
            results.add(result);
        }

        validate(results, medicineTaking.getRepeatParameters());

        return results;
    }
}
