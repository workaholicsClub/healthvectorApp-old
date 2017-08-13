package ru.android.childdiary.domain.cloud;

import android.text.TextUtils;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import javax.inject.Inject;

import io.reactivex.Single;
import ru.android.childdiary.data.cloud.BackupService;
import ru.android.childdiary.data.cloud.RestoreService;
import ru.android.childdiary.data.repositories.core.settings.SettingsDataRepository;
import ru.android.childdiary.domain.cloud.exceptions.AccountNameNotSpecifiedException;
import ru.android.childdiary.domain.core.settings.SettingsRepository;

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

    public Single<Boolean> checkIsBackupAvailable() {
        return updateCredential()
                .flatMap(accountName -> restoreService.checkIsBackupAvailable());
    }

    private Single<Boolean> updateCredential() {
        return Single.fromCallable(() -> {
            String accountName = settingsRepository.getAccountName().blockingFirst();
            if (TextUtils.isEmpty(accountName)) {
                throw new AccountNameNotSpecifiedException("Specify account name");
            }
            credential.setSelectedAccountName(accountName);
            return true;
        });
    }

    public Single<Boolean> backup() {
        return updateCredential()
                .flatMap(accountName -> backupService.prepare())
                .flatMap(backupService::upload);
    }

    public Single<Boolean> restore() {
        return updateCredential()
                .flatMap(accountName -> restoreService.download())
                .flatMap(restoreService::restore);
    }
}
