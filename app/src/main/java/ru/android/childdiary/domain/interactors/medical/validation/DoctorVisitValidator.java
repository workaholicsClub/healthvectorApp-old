package ru.android.childdiary.domain.interactors.medical.validation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;

public class DoctorVisitValidator extends MedicalValidator<DoctorVisit> {
    @Inject
    public DoctorVisitValidator(Context context) {
        super(context);
    }

    @Override
    public List<MedicalValidationResult> validate(@NonNull DoctorVisit doctorVisit) {
        List<MedicalValidationResult> results = new ArrayList<>();

        validate(results, doctorVisit.getRepeatParameters());

        if (TextUtils.isEmpty(doctorVisit.getName())) {
            MedicalValidationResult result = new MedicalValidationResult(MedicalFieldType.DOCTOR_VISIT_NAME);
            result.addMessage(context.getString(R.string.validate_doctor_visit_name_empty));
            results.add(result);
        }

        if (doctorVisit.getDoctor() == null) {
            MedicalValidationResult result = new MedicalValidationResult();
            result.addMessage(context.getString(R.string.validate_doctor_visit_doctor_empty));
            results.add(result);
        }

        return results;
    }
}
