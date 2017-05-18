package ru.android.childdiary.domain.interactors.medical.validation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.utils.ObjectUtils;

public class DoctorVisitValidator extends MedicalValidator<DoctorVisit> {
    @Inject
    public DoctorVisitValidator(Context context) {
        super(context);
    }

    @Override
    public List<MedicalValidationResult> validate(@NonNull DoctorVisit doctorVisit) {
        List<MedicalValidationResult> results = new ArrayList<>();

        MedicalValidationResult result;

        result = new MedicalValidationResult(MedicalFieldType.DOCTOR_VISIT_NAME);
        if (TextUtils.isEmpty(doctorVisit.getName())) {
            result.addMessage(context.getString(R.string.validate_doctor_visit_name_empty));
        }
        results.add(result);

        Doctor doctor = doctorVisit.getDoctor();
        if (doctor == null || doctor.getId() == null) {
            result = new MedicalValidationResult();
            result.addMessage(context.getString(R.string.validate_doctor_visit_doctor_empty));
            results.add(result);
        }

        if (ObjectUtils.isTrue(doctorVisit.getIsExported())) {
            validate(results, doctorVisit.getRepeatParameters());
        }

        return results;
    }
}
