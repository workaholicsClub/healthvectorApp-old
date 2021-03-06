package ru.android.healthvector.domain.development.antropometry;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.healthvector.data.repositories.development.antropometry.AntropometryDataRepository;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.core.requests.HasDataResponse;
import ru.android.healthvector.domain.development.antropometry.data.Antropometry;
import ru.android.healthvector.domain.development.antropometry.data.AntropometryPoint;
import ru.android.healthvector.domain.development.antropometry.requests.AntropometryListRequest;
import ru.android.healthvector.domain.development.antropometry.requests.WhoNormRequest;
import ru.android.healthvector.domain.development.antropometry.validation.AntropometryValidationResult;
import ru.android.healthvector.domain.development.antropometry.validation.AntropometryValidator;
import ru.android.healthvector.presentation.core.bindings.FieldValueChangeEventsObservable;
import ru.android.healthvector.utils.ObjectUtils;

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

    public Observable<Antropometry> add(@NonNull Antropometry item) {
        return antropometryValidator.validateObservable(item)
                .flatMap(antropometryRepository::add);
    }

    public Observable<Antropometry> update(@NonNull Antropometry item) {
        return antropometryValidator.validateObservable(item)
                .flatMap(antropometryRepository::update);
    }

    public Observable<Antropometry> delete(@NonNull Antropometry item) {
        return antropometryRepository.delete(item);
    }

    public Observable<Boolean> controlDoneButton(
            @NonNull FieldValueChangeEventsObservable<Double> heightObservable,
            @NonNull FieldValueChangeEventsObservable<Double> weightObservable) {
        return Observable.combineLatest(
                heightObservable,
                weightObservable,
                (heightEvent, weightEvent) -> Antropometry.builder()
                        .height(heightEvent.getValue())
                        .weight(weightEvent.getValue())
                        .build())
                .map(antropometryValidator::validateOnUi)
                .map(antropometryValidator::isValid)
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
                .map(antropometryValidator::validateOnUi);
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

    private List<AntropometryPoint> mapToAntropometryPoints(@NonNull LocalDate birthDate,
                                                            @NonNull List<AntropometryPoint> points,
                                                            @NonNull List<Double> values) {
        LocalDate minDate = extractMinDate(points);
        LocalDate maxDate = extractMaxDate(points);
        if (minDate == null || maxDate == null) {
            return Collections.emptyList();
        }
        List<AntropometryPoint> result = new ArrayList<>();
        for (int i = 0; i < values.size(); ++i) {
            Double value = values.get(i);
            LocalDate date = birthDate.plusDays(i);
            if ((date.isAfter(minDate) || date.isEqual(minDate))
                    && (date.isBefore(maxDate) || date.isEqual(maxDate))) {
                result.add(AntropometryPoint.builder()
                        .value(value)
                        .date(date)
                        .build());
            }
        }
        return result;
    }

    @Nullable
    private LocalDate extractMinDate(@NonNull List<AntropometryPoint> points) {
        return points.isEmpty() ? null : points.get(0).getDate();
    }

    @Nullable
    private LocalDate extractMaxDate(@NonNull List<AntropometryPoint> points) {
        return points.isEmpty() ? null : points.get(points.size() - 1).getDate();
    }

    public Single<HasDataResponse> hasChartData(@NonNull Child child) {
        return getAntropometryList(child)
                .first(Collections.emptyList())
                .flatMapObservable(Observable::fromIterable)
                .filter(antropometry -> ObjectUtils.isPositive(antropometry.getWeight())
                        || ObjectUtils.isPositive(antropometry.getHeight()))
                .count()
                .map(count -> count > 0)
                .map(hasData -> HasDataResponse.builder()
                        .child(child)
                        .hasData(hasData)
                        .build());
    }

    public Observable<Antropometry> getDefaultAntropometry(@NonNull Child child) {
        return Observable.just(Antropometry.builder()
                .child(child)
                .date(getDefaultDate(child))
                .build());
    }

    private LocalDate getDefaultDate(@NonNull Child child) {
        LocalDate today = LocalDate.now();
        if (child.getBirthDate() == null) {
            return today;
        }
        LocalDate minDate = child.getBirthDate().plusDays(1);
        if (today.isBefore(minDate)) {
            return minDate;
        }
        return today;
    }
}
