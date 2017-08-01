package ru.android.childdiary.domain.interactors.medical.validation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.R;
import ru.android.childdiary.data.repositories.medical.DoctorVisitDataRepository;
import ru.android.childdiary.domain.core.validation.EventFieldType;
import ru.android.childdiary.domain.core.validation.EventValidationException;
import ru.android.childdiary.domain.core.validation.EventValidationResult;
import ru.android.childdiary.domain.core.validation.ValidationException;
import ru.android.childdiary.domain.core.validation.Validator;
import ru.android.childdiary.domain.interactors.medical.DoctorVisitRepository;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;

public class DoctorValidator extends Validator<Doctor, EventValidationResult> {
    private final Context context;
    private final DoctorVisitRepository doctorVisitRepository;

    @Inject
    public DoctorValidator(Context context, DoctorVisitDataRepository doctorVisitRepository) {
        this.context = context;
        this.doctorVisitRepository = doctorVisitRepository;
    }

    @Override
    public List<EventValidationResult> validate(@NonNull Doctor doctor) {
        List<EventValidationResult> results = new ArrayList<>();

        EventValidationResult result;

        if (TextUtils.isEmpty(doctor.getName())) {
            result = new EventValidationResult(EventFieldType.DOCTOR_NAME);
            result.addMessage(context.getString(R.string.enter_doctor_name));
            results.add(result);
        } else {
            result = new EventValidationResult(EventFieldType.DOCTOR_NAME);
            results.add(result);

            long count = Observable.fromIterable(
                    doctorVisitRepository.getDoctors()
                            .blockingFirst())
                    .filter(d -> !TextUtils.isEmpty(d.getName()))
                    .map(Doctor::getName)
                    .filter(name -> name.equals(doctor.getName()))
                    .count()
                    .blockingGet();

            if (count > 0) {
                result = new EventValidationResult(null);
                result.addMessage(context.getString(R.string.the_value_already_exists));
                results.add(result);
            }
        }

        return results;
    }

    @Override
    protected ValidationException createException(@NonNull List<EventValidationResult> results) {
        return new EventValidationException(results);
    }
}
