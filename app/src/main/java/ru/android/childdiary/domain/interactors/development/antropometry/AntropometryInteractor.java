package ru.android.childdiary.domain.interactors.development.antropometry;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.childdiary.data.repositories.development.antropometry.AntropometryDataRepository;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.antropometry.requests.AntropometryListRequest;
import ru.android.childdiary.domain.interactors.development.antropometry.requests.HasAntropometryChartDataResponse;
import ru.android.childdiary.domain.interactors.development.antropometry.requests.WhoNormRequest;
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

    public Observable<List<Antropometry>> getAntropometryList(@NonNull Child child) {
        return antropometryRepository.getAntropometryList(AntropometryListRequest.builder()
                .child(child)
                .build());
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

    public Observable<List<AntropometryPoint>> getWeights(@NonNull Child child) {
        return antropometryRepository.getAntropometryList(AntropometryListRequest.builder()
                .child(child)
                .ascending(true)
                .startFromBirthday(true)
                .build())
                .map(this::mapWeights);
    }

    private List<AntropometryPoint> mapWeights(@NonNull List<Antropometry> antropometryList) {
        return Observable.fromIterable(antropometryList)
                .filter(antropometry -> ObjectUtils.isPositive(antropometry.getWeight()))
                .map(antropometry -> AntropometryPoint.builder()
                        .value(antropometry.getWeight())
                        .date(antropometry.getDate())
                        .build())
                .toList()
                .blockingGet();
    }

    public Observable<List<AntropometryPoint>> getHeights(@NonNull Child child) {
        return antropometryRepository.getAntropometryList(AntropometryListRequest.builder()
                .child(child)
                .ascending(true)
                .startFromBirthday(true)
                .build())
                .map(this::mapHeights);
    }

    private List<AntropometryPoint> mapHeights(@NonNull List<Antropometry> antropometryList) {
        return Observable.fromIterable(antropometryList)
                .filter(antropometry -> ObjectUtils.isPositive(antropometry.getHeight()))
                .map(antropometry -> AntropometryPoint.builder()
                        .value(antropometry.getHeight())
                        .date(antropometry.getDate())
                        .build())
                .toList()
                .blockingGet();
    }

    public Observable<List<AntropometryPoint>> getWeightLow(@NonNull WhoNormRequest request) {
        return antropometryRepository.getWeightLow(request.getChild().getSex())
                .map(values -> mapToAntropometryPoints(request.getChild().getBirthDate(),
                        request.getPoints(),
                        values));
    }

    public Observable<List<AntropometryPoint>> getWeightHigh(@NonNull WhoNormRequest request) {
        return antropometryRepository.getWeightHigh(request.getChild().getSex())
                .map(values -> mapToAntropometryPoints(request.getChild().getBirthDate(),
                        request.getPoints(),
                        values));
    }

    public Observable<List<AntropometryPoint>> getHeightLow(@NonNull WhoNormRequest request) {
        return antropometryRepository.getHeightLow(request.getChild().getSex())
                .map(values -> mapToAntropometryPoints(request.getChild().getBirthDate(),
                        request.getPoints(),
                        values));
    }

    public Observable<List<AntropometryPoint>> getHeightHigh(@NonNull WhoNormRequest request) {
        return antropometryRepository.getHeightHigh(request.getChild().getSex())
                .map(values -> mapToAntropometryPoints(request.getChild().getBirthDate(),
                        request.getPoints(),
                        values));
    }

    private List<AntropometryPoint> mapToAntropometryPoints(@Nullable LocalDate birthDate,
                                                            @NonNull List<AntropometryPoint> points,
                                                            @NonNull List<Double> values) {
        if (birthDate == null) {
            return Collections.emptyList();
        }
        List<AntropometryPoint> result = new ArrayList<>();
        for (int i = 0; i < points.size(); ++i) {
            LocalDate date = points.get(i).getDate();
            int index = Days.daysBetween(birthDate, date).getDays();
            if (index >= 0 && index < values.size()) {
                AntropometryPoint point = AntropometryPoint.builder()
                        .value(values.get(index))
                        .date(date)
                        .build();
                result.add(point);
            }
        }
        return result;
    }

    public Single<HasAntropometryChartDataResponse> hasChartData(@NonNull Child child) {
        return getAntropometryList(child)
                .first(Collections.emptyList())
                .flatMapObservable(Observable::fromIterable)
                .filter(antropometry -> ObjectUtils.isPositive(antropometry.getWeight())
                        || ObjectUtils.isPositive(antropometry.getHeight()))
                .count()
                .map(count -> count > 0)
                .map(hasData -> HasAntropometryChartDataResponse.builder()
                        .child(child)
                        .hasData(hasData)
                        .build());
    }
}
