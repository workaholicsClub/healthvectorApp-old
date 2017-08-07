package ru.android.childdiary.domain.interactors.core.settings;

import android.support.annotation.NonNull;

import org.joda.time.LocalTime;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.childdiary.data.repositories.core.settings.SettingsDataRepository;

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
}
