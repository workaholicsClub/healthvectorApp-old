package ru.android.childdiary.presentation.splash;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;

import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BaseMvpActivity;

public class SplashActivity extends BaseMvpActivity implements SplashView,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 9000;

    private final ResultCallback<DriveFolder.DriveFileResult> fileCallback = (@NonNull DriveFolder.DriveFileResult result) -> {
        if (!result.getStatus().isSuccess()) {
            showToast("Error while trying to create the file");
            return;
        }

        showToast("Created a file in App Folder: " + result.getDriveFile().getDriveId());
    };

    @InjectPresenter
    SplashPresenter presenter;

    private GoogleApiClient googleApiClient;

    private final ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback = (@NonNull DriveApi.DriveContentsResult result) -> {
        if (!result.getStatus().isSuccess()) {
            showToast("Error while trying to create new file contents");
            return;
        }

        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle("appconfig.txt")
                .setMimeType("text/plain")
                .build();
        Drive.DriveApi.getAppFolder(googleApiClient)
                .createFile(googleApiClient, changeSet, result.getDriveContents())
                .setResultCallback(fileCallback);
    };

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_APPFOLDER)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESOLVE_CONNECTION_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    googleApiClient.connect();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void navigateToMain() {
        // Intent intent = MainActivity.getIntent(this);
        // startActivity(intent);
        // finish();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Drive.DriveApi.newDriveContents(googleApiClient)
                .setResultCallback(driveContentsCallback);
    }

    @Override
    public void onConnectionSuspended(int cause) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                // Unable to resolve, message user appropriately
            }
        } else {
            GoogleApiAvailability.getInstance().getErrorDialog(this, connectionResult.getErrorCode(), 0).show();
        }
    }
}
