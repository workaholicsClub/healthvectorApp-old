package ru.android.childdiary.domain.interactors.core.settings;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalTime;

import io.reactivex.Observable;

public interface SettingsRepository {
    Observable<LocalTime> getStartTime();

    void setStartTime(@NonNull LocalTime startTime);

    Observable<LocalTime> getStartTimeOnce();

    Observable<LocalTime> getFinishTime();

    void setFinishTime(@NonNull LocalTime finishTime);

    Observable<LocalTime> getFinishTimeOnce();

    Observable<String> getAccountName();

    void setAccountName(@Nullable String accountName);

    Observable<Boolean> getIsCloudShown();

    void setIsCloudShown(boolean value);
}
