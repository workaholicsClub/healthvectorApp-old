package ru.android.childdiary.domain.interactors.development.antropometry;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.child.Child;

public interface AntropometryRepository {
    Observable<List<Antropometry>> getAll(@NonNull Child child);

    Observable<Antropometry> add(@NonNull Antropometry antropometry);

    Observable<Antropometry> update(@NonNull Antropometry antropometry);

    Observable<Antropometry> delete(@NonNull Antropometry antropometry);

    Observable<List<Double>> getWeightLow(@Nullable Sex sex);

    Observable<List<Double>> getWeightHigh(@Nullable Sex sex);

    Observable<List<Double>> getHeightLow(@Nullable Sex sex);

    Observable<List<Double>> getHeightHigh(@Nullable Sex sex);
}
