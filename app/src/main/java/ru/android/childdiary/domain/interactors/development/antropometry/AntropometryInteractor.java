package ru.android.childdiary.domain.interactors.development.antropometry;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.development.antropometry.AntropometryDataRepository;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.antropometry.validation.AntropometryValidationException;
import ru.android.childdiary.domain.interactors.development.antropometry.validation.AntropometryValidationResult;
import ru.android.childdiary.domain.interactors.development.antropometry.validation.AntropometryValidator;
import ru.android.childdiary.presentation.core.bindings.FieldValueChangeEventsObservable;
import ru.android.childdiary.utils.ObjectUtils;

public class AntropometryInteractor {
    private final AntropometryRepository antropometryRepository;
    private final AntropometryValidator antropometryValidator;

    @Inject
    public AntropometryInteractor(AntropometryDataRepository antropometryRepository,
                                  AntropometryValidator antropometryValidator) {
        this.antropometryRepository = antropometryRepository;
        this.antropometryValidator = antropometryValidator;
    }

    public Observable<List<Antropometry>> getAll(@NonNull Child child) {
        return antropometryRepository.getAll(child);
    }

    public Observable<Antropometry> add(@NonNull Child child, @NonNull Antropometry item) {
        item = item.toBuilder().child(child).build();
        return validate(item)
                .flatMap(antropometryRepository::add);
    }

    public Observable<Antropometry> update(@NonNull Antropometry item) {
        return validate(item)
                .flatMap(antropometryRepository::update);
    }

    public Observable<Antropometry> delete(@NonNull Antropometry item) {
        return antropometryRepository.delete(item);
    }

    private Observable<Antropometry> validate(@NonNull Antropometry antropometry) {
        return Observable.defer(() -> {
            List<AntropometryValidationResult> results = antropometryValidator.validate(antropometry);
            if (!antropometryValidator.isValid(results)) {
                return Observable.error(new AntropometryValidationException(results));
            }
            return Observable.just(antropometry);
        });
    }

    public Observable<Boolean> controlDoneButton(
            @NonNull FieldValueChangeEventsObservable<Double> heightObservable,
            @NonNull FieldValueChangeEventsObservable<Double> weightObservable) {
        return Observable.combineLatest(
                heightObservable,
                weightObservable,
                (heightEvent, weightEvent) -> ObjectUtils.isPositive(heightEvent.getValue())
                        || ObjectUtils.isPositive(weightEvent.getValue()))
                .distinctUntilChanged();
    }

    public Observable<List<AntropometryValidationResult>> controlFields(
            @NonNull FieldValueChangeEventsObservable<Double> heightObservable,
            @NonNull FieldValueChangeEventsObservable<Double> weightObservable) {
        return Observable.combineLatest(
                heightObservable,
                weightObservable,
                (heightEvent, weightEvent) -> Antropometry.builder()
                        .height(heightEvent.getValue())
                        .weight(weightEvent.getValue())
                        .build())
                .map(antropometryValidator::validate);
    }
}
