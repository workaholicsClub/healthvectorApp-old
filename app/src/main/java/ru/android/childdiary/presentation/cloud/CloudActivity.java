package ru.android.childdiary.presentation.cloud;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import javax.inject.Inject;

import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.permissions.RequestPermissionInfo;
import ru.android.childdiary.presentation.main.MainActivity;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class CloudActivity extends BaseMvpActivity implements CloudView {
    private static final int REQUEST_ACCOUNT_PICKER = 1000;
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1001;
    private static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1002;

    private static final String TAG_PROGRESS_DIALOG_BACKUP_LOADING = "TAG_PROGRESS_DIALOG_BACKUP_LOADING";
    private static final String TAG_PROGRESS_DIALOG_BACKUP_RESTORING = "TAG_PROGRESS_DIALOG_BACKUP_RESTORING";

    @InjectPresenter
    CloudPresenter presenter;

    // используется здесь только для создания диалога выбора аккаунта
    // TODO: диалог выбора аккаунта переделать на более красивый
    @Inject
    GoogleAccountCredential credential;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, CloudActivity.class);
        return intent;
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);
    }

    @OnClick(R.id.buttonLater)
    void onLaterClick() {
        presenter.moveNext();
    }

    @OnClick(R.id.buttonBindAccount)
    void bindAccount() {
        presenter.bindAccount();
    }

    @Override
    public void requestPermission() {
        RequestPermissionInfo permissionInfo = RequestPermissionInfo.builder()
                .permission(Manifest.permission.GET_ACCOUNTS)
                .requestCode(REQUEST_PERMISSION_GET_ACCOUNTS)
                .title(getString(R.string.request_get_accounts_permission_title))
                .text(getString(R.string.request_get_accounts_permission_text))
                .build();
        permissionHelper.requestPermission(permissionInfo, getSex());
    }

    @Override
    public void permissionGranted(RequestPermissionInfo permissionInfo) {
        super.permissionGranted(permissionInfo);
        if (permissionInfo.getRequestCode() == REQUEST_PERMISSION_GET_ACCOUNTS) {
            presenter.permissionGranted();
        }
    }

    @Override
    public void permissionDenied(RequestPermissionInfo permissionInfo) {
        super.permissionDenied(permissionInfo);
        if (permissionInfo.getRequestCode() == REQUEST_PERMISSION_GET_ACCOUNTS) {
            presenter.permissionDenied();
        }
    }

    @Override
    public void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == RESULT_OK) {
                    presenter.playServicesResolved();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK) {
                    String accountName = null;
                    if (data != null && data.getExtras() != null) {
                        accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    }
                    presenter.accountChosen(accountName);
                }
                break;
        }
    }

    @Override
    public void showPlayServicesErrorDialog(int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            Dialog dialog = apiAvailability.getErrorDialog(
                    this,
                    connectionStatusCode,
                    REQUEST_GOOGLE_PLAY_SERVICES);
            dialog.show();
        } else {
            new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                    .setMessage(R.string.user_unrecoverable_error_dialog_text)
                    .setPositiveButton(R.string.ok,
                            (dialog, which) -> presenter.moveNext())
                    .show();
        }
    }

    @Override
    public void connectionUnavailable() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.bind_account_connection_unavailable_dialog_title)
                .setMessage(R.string.bind_account_connection_unavailable_dialog_text)
                .setPositiveButton(R.string.try_again,
                        (DialogInterface dialog, int which) -> presenter.bindAccount())
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> presenter.moveNext())
                .show();
    }

    @Override
    public void showBackupLoading(boolean loading) {
        if (loading) {
            showProgress(TAG_PROGRESS_DIALOG_BACKUP_LOADING,
                    getString(R.string.please_wait),
                    getString(R.string.backup_loading));
        } else {
            hideProgress(TAG_PROGRESS_DIALOG_BACKUP_LOADING);
        }
    }

    @Override
    public void foundBackup() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.found_backup_dialog_title)
                .setMessage(R.string.found_backup_dialog_text)
                .setPositiveButton(R.string.restore,
                        (DialogInterface dialog, int which) -> presenter.restoreFromBackup())
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> presenter.moveNext())
                .show();
    }

    @Override
    public void failedToCheckBackupAvailability() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.google_drive_error_dialog_text)
                .setPositiveButton(R.string.try_again,
                        (DialogInterface dialog, int which) -> presenter.bindAccount())
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> presenter.moveNext())
                .show();
    }

    @Override
    public void showBackupRestoring(boolean loading) {
        if (loading) {
            showProgress(TAG_PROGRESS_DIALOG_BACKUP_RESTORING,
                    getString(R.string.please_wait),
                    getString(R.string.backup_restoring));
        } else {
            hideProgress(TAG_PROGRESS_DIALOG_BACKUP_RESTORING);
        }
    }

    @Override
    public void backupRestored() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.backup_restoring_success_dialog_text)
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> presenter.moveNext())
                .show();
    }

    @Override
    public void failedToRestoreBackup() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.backup_restoring_error_dialog_text)
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> presenter.moveNext())
                .show();
    }

    @Override
    public void navigateToMain() {
        Intent intent = MainActivity.getIntent(this);
        startActivity(intent);
        finish();
    }
}
