package ru.android.childdiary.domain.interactors.core.settings;

import android.support.annotation.NonNull;

import org.joda.time.LocalTime;

import io.reactivex.Observable;

public interface SettingsRepository {
    Observable<LocalTime> getStartTime();

    void setStartTime(@NonNull LocalTime startTime);

    Observable<LocalTime> getStartTimeOnce();

    Observable<LocalTime> getFinishTime();

    void setFinishTime(@NonNull LocalTime finishTime);

    Observable<LocalTime> getFinishTimeOnce();

    Observable<LocalTime> setStartTimeObservable(@NonNull LocalTime startTime);

    Observable<LocalTime> setFinishTimeObservable(@NonNull LocalTime finishTime);
}
