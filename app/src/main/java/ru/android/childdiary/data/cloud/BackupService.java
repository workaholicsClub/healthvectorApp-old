package ru.android.childdiary.data.cloud;

import com.google.api.services.drive.Drive;

import javax.inject.Inject;

import io.reactivex.Single;

public class BackupService {
    private final Drive drive;

    @Inject
    public BackupService(Drive drive) {
        this.drive = drive;
    }

    public Single<java.io.File> prepare() {
        return Single.fromCallable(() -> null);
    }

    public Single<Boolean> upload() {
        return Single.fromCallable(() -> true);
    }

    /*
    @Override
    protected List<String> doInBackground(Void... params) {
        try {
            File fileMetadata = new File();
            fileMetadata.setName("config.json");
            fileMetadata.setParents(Collections.singletonList("appDataFolder"));
            java.io.File filePath = new java.io.File(getFilesDir(), "config.json");
            filePath.createNewFile();
            FileContent mediaContent = new FileContent("application/json", filePath);
            File file = mService.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            System.out.println("File ID: " + file.getId());
            return new ArrayList<>(Collections.singletonList(file.getId()));
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    @Override
    protected void onCancelled() {
        mProgress.hide();
        if (mLastError != null) {
            if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                showPlayServicesErrorDialog(
                        ((GooglePlayServicesAvailabilityIOException) mLastError)
                                .getConnectionStatusCode());
            } else if (mLastError instanceof UserRecoverableAuthIOException) {
                startActivityForResult(
                        ((UserRecoverableAuthIOException) mLastError).getIntent(),
                        REQUEST_AUTHORIZATION);
            } else {
                mOutputText.setText("The following error occurred:\n"
                        + mLastError.getMessage());
            }
        } else {
            mOutputText.setText("Request cancelled.");
        }
    }*/
}
