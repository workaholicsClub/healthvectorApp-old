package ru.android.childdiary.data.cloud;

import com.google.api.services.drive.Drive;

import javax.inject.Inject;

public class BackupService {
    private final Drive drive;

    @Inject
    public BackupService(Drive drive) {
        this.drive = drive;
    }

    public void prepare() {

    }

    public void upload() {

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

    private List<String> getDataFromApi() throws IOException {
        // Get a list of up to 10 files.
        List<String> fileInfo = new ArrayList<String>();
        FileList result = mService.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        if (files != null) {
            for (File file : files) {
                fileInfo.add(String.format("%s (%s)\n",
                        file.getName(), file.getId()));
            }
        }
        return fileInfo;
    }

    @Override
    protected void onPreExecute() {
        mOutputText.setText("");
        mProgress.show();
    }

    @Override
    protected void onPostExecute(List<String> output) {
        mProgress.hide();
        if (output == null || output.size() == 0) {
            mOutputText.setText("No results returned.");
        } else {
            output.add(0, "Data retrieved using the Drive API:");
            mOutputText.setText(TextUtils.join("\n", output));
        }
    }

    @Override
    protected void onCancelled() {
        mProgress.hide();
        if (mLastError != null) {
            if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                showGooglePlayServicesAvailabilityErrorDialog(
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
