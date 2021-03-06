package ru.android.healthvector.domain.development.antropometry.validation;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.healthvector.R;
import ru.android.healthvector.data.repositories.development.antropometry.AntropometryDataRepository;
import ru.android.healthvector.domain.core.validation.core.ValidationException;
import ru.android.healthvector.domain.core.validation.core.Validator;
import ru.android.healthvector.domain.development.antropometry.AntropometryRepository;
import ru.android.healthvector.domain.development.antropometry.data.Antropometry;
import ru.android.healthvector.domain.development.antropometry.requests.AntropometryListRequest;
import ru.android.healthvector.utils.ObjectUtils;

public class AntropometryValidator extends Validator<Antropometry, AntropometryValidationResult> {
    private static final int MAX_HEIGHT = 200;
    private static final int MAX_WEIGHT = 150;

    private final Context context;
    private final AntropometryRepository antropometryRepository;

    @Inject
    public AntropometryValidator(Context context, AntropometryDataRepository antropometryRepository) {
        this.context = context;
        this.antropometryRepository = antropometryRepository;
    }

    @Override
    public List<AntropometryValidationResult> validate(@NonNull Antropometry antropometry) {
        List<AntropometryValidationResult> results = validateOnUi(antropometry);

        if (antropometry.getChild() != null && antropometry.getChild().getId() != null) {
            boolean isUnique = antropometryRepository.getAntropometryList(AntropometryListRequest.builder()
                    .child(antropometry.getChild())
                    .build())
                    .first(Collections.emptyList())
                    .flatMapObservable(Observable::fromIterable)
                    .filter(a -> ObjectUtils.equals(a.getDate(), antropometry.getDate())
                            && !ObjectUtils.equals(a.getId(), antropometry.getId()))
                    .count()
                    .map(count -> count == 0)
                    .blockingGet();
            if (!isUnique) {
                AntropometryValidationResult result = new AntropometryValidationResult(AntropometryFieldType.DATE);
                result.addMessage(context.getString(R.string.validation_antropometry_date_not_unique));
                results.add(result);
            }
        }

        return results;
    }

    @Override
    public List<AntropometryValidationResult> validateOnUi(@NonNull Antropometry antropometry) {
        List<AntropometryValidationResult> results = new ArrayList<>();

        AntropometryValidationResult result;

        result = new AntropometryValidationResult(AntropometryFieldType.HEIGHT_WEIGHT);
        if (!ObjectUtils.isPositive(antropometry.getHeight())
                && !ObjectUtils.isPositive(antropometry.getWeight())) {
            result.addMessage(context.getString(R.string.validation_antropometry_empty));
        }
        results.add(result);

        result = new AntropometryValidationResult(AntropometryFieldType.HEIGHT);
        if (antropometry.getHeight() != null
                && (antropometry.getHeight() < 0 || antropometry.getHeight() > MAX_HEIGHT)) {
            result.addMessage(context.getString(R.string.validation_antropometry_invalid_height));
        }
        results.add(result);

        result = new AntropometryValidationResult(AntropometryFieldType.WEIGHT);
        if (antropometry.getWeight() != null
                && (antropometry.getWeight() < 0 || antropometry.getWeight() > MAX_WEIGHT)) {
            result.addMessage(context.getString(R.string.validation_antropometry_invalid_weight));
        }
        results.add(result);

        return results;
    }

    @Override
    protected ValidationException createException(@NonNull List<AntropometryValidationResult> results) {
        return new AntropometryValidationException(results);
    }

    @Override
    protected Antropometry fix(@NonNull Antropometry item) {
        if (!ObjectUtils.isPositive(item.getHeight())) {
            item = item.toBuilder().height(null).build();
        }
        if (!ObjectUtils.isPositive(item.getWeight())) {
            item = item.toBuilder().weight(null).build();
        }
        return item;
    }
}
