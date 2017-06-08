package ru.android.childdiary.domain.interactors.development.antropometry;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.interactors.child.Child;

public interface AntropometryRepository {
    Observable<List<Antropometry>> getAll(@NonNull Child child);

    Observable<Antropometry> add(@NonNull Antropometry antropometry);

    Observable<Antropometry> update(@NonNull Antropometry antropometry);

    Observable<Antropometry> delete(@NonNull Antropometry antropometry);
}
