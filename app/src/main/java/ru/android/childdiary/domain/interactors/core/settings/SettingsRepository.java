package ru.android.childdiary.domain.interactors.core.settings;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalTime;

import io.reactivex.Observable;

public interface SettingsRepository {
    // start time
    Observable<LocalTime> getStartTime();

    void setStartTime(@NonNull LocalTime startTime);

    Observable<LocalTime> getStartTimeOnce();

    // finish time
    Observable<LocalTime> getFinishTime();

    void setFinishTime(@NonNull LocalTime finishTime);

    Observable<LocalTime> getFinishTimeOnce();

    // account name
    Observable<String> getAccountName();

    void setAccountName(@Nullable String accountName);

    Observable<String> getAccountNameOnce();

    void removeAccount();

    // is app intro shown
    Observable<Boolean> getIsAppIntroShown();

    void setIsAppIntroShown(boolean value);

    Observable<Boolean> getIsAppIntroShownOnce();

    // is cloud shown
    Observable<Boolean> getIsCloudShown();

    void setIsCloudShown(boolean value);

    Observable<Boolean> getIsCloudShownOnce();
}
