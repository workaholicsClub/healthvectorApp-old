package ru.android.childdiary.domain.interactors.dictionaries.medicines;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.R;
import ru.android.childdiary.data.repositories.medical.MedicineTakingDataRepository;
import ru.android.childdiary.domain.interactors.core.validation.EventFieldType;
import ru.android.childdiary.domain.interactors.core.validation.EventValidationException;
import ru.android.childdiary.domain.interactors.core.validation.EventValidationResult;
import ru.android.childdiary.domain.interactors.core.validation.ValidationException;
import ru.android.childdiary.domain.interactors.core.validation.Validator;
import ru.android.childdiary.domain.interactors.medical.MedicineTakingRepository;
import ru.android.childdiary.domain.interactors.dictionaries.medicines.Medicine;

public class MedicineValidator extends Validator<Medicine, EventValidationResult> {
    private final Context context;
    private final MedicineTakingRepository medicineTakingRepository;

    @Inject
    public MedicineValidator(Context context, MedicineTakingDataRepository medicineTakingRepository) {
        this.context = context;
        this.medicineTakingRepository = medicineTakingRepository;
    }

    @Override
    public List<EventValidationResult> validate(@NonNull Medicine medicine) {
        List<EventValidationResult> results = new ArrayList<>();

        EventValidationResult result;

        if (TextUtils.isEmpty(medicine.getName())) {
            result = new EventValidationResult(EventFieldType.MEDICINE_NAME);
            result.addMessage(context.getString(R.string.enter_medicine_name));
            results.add(result);
        } else {
            result = new EventValidationResult(EventFieldType.MEDICINE_NAME);
            results.add(result);

            long count = Observable.fromIterable(
                    medicineTakingRepository.getMedicines()
                            .blockingFirst())
                    .filter(d -> !TextUtils.isEmpty(d.getName()))
                    .map(Medicine::getName)
                    .filter(name -> name.equals(medicine.getName()))
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
