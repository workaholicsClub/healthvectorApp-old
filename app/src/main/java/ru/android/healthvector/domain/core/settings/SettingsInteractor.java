package ru.android.healthvector.domain.core.settings;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalTime;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.healthvector.data.repositories.core.settings.SettingsDataRepository;

public class SettingsInteractor {
    private final SettingsRepository settingsRepository;

    @Inject
    public SettingsInteractor(SettingsDataRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public Observable<LocalTime> getStartTime() {
        return settingsRepository.getStartTime();
    }

    public Observable<LocalTime> getFinishTime() {
        return settingsRepository.getFinishTime();
    }

    public Observable<LocalTime> getStartTimeOnce() {
        return settingsRepository.getStartTimeOnce();
    }

    public Observable<LocalTime> getFinishTimeOnce() {
        return settingsRepository.getFinishTimeOnce();
    }

    public Single<Boolean> setStartTime(@NonNull LocalTime startTime, @NonNull LocalTime finishTime) {
        return Single.fromCallable(() -> {
            settingsRepository.setStartTime(startTime);
            settingsRepository.setFinishTime(finishTime);
            return true;
        });
    }

    public Observable<String> getAccountName() {
        return settingsRepository.getAccountName();
    }

    public void setAccountName(@Nullable String accountName) {
        settingsRepository.setAccountName(accountName);
    }

    public Observable<String> getAccountNameOnce() {
        return settingsRepository.getAccountNameOnce();
    }

    public void removeAccount() {
        settingsRepository.removeAccount();
    }

    public Observable<Boolean> getIsCloudShownOnce() {
        return settingsRepository.getIsCloudShownOnce();
    }

    public void setIsCloudShown(boolean value) {
        settingsRepository.setIsCloudShown(value);
    }

    public Observable<Boolean> getIsAppIntroShownOnce() {
        return settingsRepository.getIsAppIntroShownOnce();
    }

    public void setIsAppIntroShown(boolean value) {
        settingsRepository.setIsAppIntroShown(value);
    }
}
