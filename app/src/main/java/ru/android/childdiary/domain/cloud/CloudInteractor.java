package ru.android.childdiary.domain.cloud;

import android.support.annotation.Nullable;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.childdiary.data.cloud.BackupService;
import ru.android.childdiary.data.cloud.RestoreService;
import ru.android.childdiary.data.repositories.core.settings.SettingsDataRepository;
import ru.android.childdiary.domain.interactors.core.settings.SettingsRepository;

public class CloudInteractor {
    private final GoogleAccountCredential credential;
    private final SettingsRepository settingsRepository;
    private final BackupService backupService;
    private final RestoreService restoreService;

    @Inject
    public CloudInteractor(GoogleAccountCredential credential,
                           SettingsDataRepository settingsRepository,
                           BackupService backupService,
                           RestoreService restoreService) {
        this.credential = credential;
        this.settingsRepository = settingsRepository;
        this.backupService = backupService;
        this.restoreService = restoreService;
    }

    public Observable<String> getAccountNameOnce() {
        return settingsRepository.getAccountNameOnce();
    }

    public void setAccountName(@Nullable String accountName) {
        settingsRepository.setAccountName(accountName);
    }

    public Observable<Boolean> getIsCloudShownOnce() {
        return settingsRepository.getIsCloudShownOnce();
    }

    public void setIsCloudShown(boolean value) {
        settingsRepository.setIsCloudShown(value);
    }

    public Single<Boolean> checkIsBackupAvailable() {
        return updateCredential()
                .flatMap(accountName -> restoreService.checkIsBackupAvailable());
    }

    private Single<Boolean> updateCredential() {
        return Single.fromCallable(() -> {
            String accountName = settingsRepository.getAccountName().blockingFirst();
            credential.setSelectedAccountName(accountName);
            return true;
        });
    }

    public void download() {

    }

    public void restore() {

    }

    public void prepare() {

    }

    public void upload() {

    }
}
