package ru.android.childdiary.domain.medical.validation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.core.validation.EventFieldType;
import ru.android.childdiary.domain.core.validation.EventValidationException;
import ru.android.childdiary.domain.core.validation.EventValidationResult;
import ru.android.childdiary.domain.core.validation.EventValidator;
import ru.android.childdiary.domain.core.validation.core.ValidationException;
import ru.android.childdiary.domain.medical.data.DoctorVisit;
import ru.android.childdiary.domain.dictionaries.doctors.data.Doctor;
import ru.android.childdiary.utils.ObjectUtils;

public class DoctorVisitValidator extends EventValidator<DoctorVisit> {
    @Inject
    public DoctorVisitValidator(Context context) {
        super(context);
    }

    @Override
    public List<EventValidationResult> validate(@NonNull DoctorVisit doctorVisit) {
        List<EventValidationResult> results = new ArrayList<>();

        EventValidationResult result;

        result = new EventValidationResult(EventFieldType.DOCTOR_VISIT_NAME);
        if (TextUtils.isEmpty(doctorVisit.getName())) {
            result.addMessage(context.getString(R.string.validate_doctor_visit_name_empty));
        }
        results.add(result);

        Doctor doctor = doctorVisit.getDoctor();
        if (doctor == null || doctor.getId() == null) {
            result = new EventValidationResult();
            result.addMessage(context.getString(R.string.validate_doctor_visit_doctor_empty));
            results.add(result);
        }

        if (ObjectUtils.isTrue(doctorVisit.getIsExported())) {
            validate(results, doctorVisit.getRepeatParameters());
        }

        return results;
    }

    @Override
    protected ValidationException createException(@NonNull List<EventValidationResult> results) {
        return new EventValidationException(results);
    }
}
