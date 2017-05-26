package ru.android.childdiary.domain.interactors.medical.validation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.R;
import ru.android.childdiary.data.repositories.medical.MedicineTakingDataRepository;
import ru.android.childdiary.domain.core.validation.Validator;
import ru.android.childdiary.domain.interactors.medical.MedicineTakingRepository;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;

public class MedicineValidator extends Validator<Medicine, MedicalValidationResult> {
    private final Context context;
    private final MedicineTakingRepository medicineTakingRepository;

    @Inject
    public MedicineValidator(Context context, MedicineTakingDataRepository medicineTakingRepository) {
        this.context = context;
        this.medicineTakingRepository = medicineTakingRepository;
    }

    @Override
    public List<MedicalValidationResult> validate(@NonNull Medicine medicine) {
        List<MedicalValidationResult> results = new ArrayList<>();

        MedicalValidationResult result;

        if (TextUtils.isEmpty(medicine.getName())) {
            result = new MedicalValidationResult(MedicalFieldType.MEDICINE_NAME);
            result.addMessage(context.getString(R.string.enter_medicine_name));
            results.add(result);
        } else {
            result = new MedicalValidationResult(MedicalFieldType.MEDICINE_NAME);
            results.add(result);

            long count = Observable.fromIterable(medicineTakingRepository.getMedicines().blockingFirst())
                    .filter(d -> !TextUtils.isEmpty(d.getName()))
                    .map(Medicine::getName)
                    .filter(name -> name.equals(medicine.getName()))
                    .count()
                    .blockingGet();

            if (count > 0) {
                result = new MedicalValidationResult(null);
                result.addMessage(context.getString(R.string.the_value_already_exists));
                results.add(result);
            }
        }

        return results;
    }
}
