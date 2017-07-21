package ru.android.childdiary.domain.interactors.development.antropometry.validation;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.data.repositories.development.antropometry.AntropometryDataRepository;
import ru.android.childdiary.domain.core.validation.Validator;
import ru.android.childdiary.domain.interactors.development.antropometry.Antropometry;
import ru.android.childdiary.domain.interactors.development.antropometry.AntropometryRepository;
import ru.android.childdiary.utils.ObjectUtils;

public class AntropometryValidator extends Validator<Antropometry, AntropometryValidationResult> {
    private final Context context;
    private final AntropometryRepository antropometryRepository;

    @Inject
    public AntropometryValidator(Context context, AntropometryDataRepository antropometryRepository) {
        this.context = context;
        this.antropometryRepository = antropometryRepository;
    }

    @Override
    public List<AntropometryValidationResult> validate(@NonNull Antropometry antropometry) {
        List<AntropometryValidationResult> results = new ArrayList<>();

        for (AntropometryFieldType field : AntropometryFieldType.values()) {
            AntropometryValidationResult result = new AntropometryValidationResult(field);
            validateField(result, antropometry);
            results.add(result);
        }

        return results;
    }

    private void validateField(@NonNull AntropometryValidationResult result, @NonNull Antropometry antropometry) {
        if (result.getFieldType() == null) {
            return;
        }
        switch (result.getFieldType()) {
            case DATE:
                // TODO
                break;
            case HEIGHT_WEIGHT:
                if (!ObjectUtils.isPositive(antropometry.getHeight())
                        || !ObjectUtils.isPositive(antropometry.getWeight())) {
                    result.addMessage(context.getString(R.string.validation_antropometry_empty));
                }
                break;
        }
    }
}
