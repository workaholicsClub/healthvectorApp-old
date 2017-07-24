package ru.android.childdiary.data.repositories.development.antropometry;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.development.antropometry.Antropometry;
import ru.android.childdiary.domain.interactors.development.antropometry.AntropometryRepository;
import ru.android.childdiary.domain.interactors.development.antropometry.requests.AntropometryListRequest;

@Singleton
public class AntropometryDataRepository implements AntropometryRepository {
    private final AntropometryDbService antropometryDbService;

    @Inject
    public AntropometryDataRepository(AntropometryDbService antropometryDbService) {
        this.antropometryDbService = antropometryDbService;
    }

    @Override
    public Observable<List<Antropometry>> getAntropometryList(@NonNull AntropometryListRequest request) {
        return antropometryDbService.getAntropometryList(request);
    }

    @Override
    public Observable<Antropometry> add(@NonNull Antropometry antropometry) {
        return antropometryDbService.add(antropometry);
    }

    @Override
    public Observable<Antropometry> update(@NonNull Antropometry antropometry) {
        return antropometryDbService.update(antropometry);
    }

    @Override
    public Observable<Antropometry> delete(@NonNull Antropometry antropometry) {
        return antropometryDbService.delete(antropometry);
    }

    @Override
    public Observable<List<Double>> getWeightLow(@Nullable Sex sex) {
        return Observable.fromCallable(() -> {
            if (sex == null) {
                return Collections.emptyList();
            }
            switch (sex) {
                case MALE:
                    return Arrays.asList(WhoNorms.WEIGHT_LOW_BOY);
                case FEMALE:
                    return Arrays.asList(WhoNorms.WEIGHT_LOW_BOY);
            }
            return Collections.emptyList();
        });
    }

    @Override
    public Observable<List<Double>> getWeightHigh(@Nullable Sex sex) {
        return Observable.fromCallable(() -> {
            if (sex == null) {
                return Collections.emptyList();
            }
            switch (sex) {
                case MALE:
                    return Arrays.asList(WhoNorms.WEIGHT_HIGH_BOY);
                case FEMALE:
                    return Arrays.asList(WhoNorms.WEIGHT_HIGH_GIRL);
            }
            return Collections.emptyList();
        });
    }

    @Override
    public Observable<List<Double>> getHeightLow(@Nullable Sex sex) {
        return Observable.fromCallable(() -> {
            if (sex == null) {
                return Collections.emptyList();
            }
            switch (sex) {
                case MALE:
                    return Arrays.asList(WhoNorms.HEIGHT_LOW_BOY);
                case FEMALE:
                    return Arrays.asList(WhoNorms.HEIGHT_LOW_GIRL);
            }
            return Collections.emptyList();
        });
    }

    @Override
    public Observable<List<Double>> getHeightHigh(@Nullable Sex sex) {
        return Observable.fromCallable(() -> {
            if (sex == null) {
                return Collections.emptyList();
            }
            switch (sex) {
                case MALE:
                    return Arrays.asList(WhoNorms.HEIGHT_HIGH_BOY);
                case FEMALE:
                    return Arrays.asList(WhoNorms.HEIGHT_HIGH_GIRL);
            }
            return Collections.emptyList();
        });
    }
}
