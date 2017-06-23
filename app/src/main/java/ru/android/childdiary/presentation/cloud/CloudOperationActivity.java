package ru.android.childdiary.presentation.cloud;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.common.GoogleApiAvailability;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.permissions.RequestPermissionInfo;
import ru.android.childdiary.presentation.main.AppPartition;
import ru.android.childdiary.presentation.main.MainActivity;
import ru.android.childdiary.utils.ui.AccountChooserPicker;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class CloudOperationActivity extends BaseMvpActivity implements CloudOperationView {
    private static final int REQUEST_ACCOUNT_PICKER = 1000;
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1001;
    private static final int REQUEST_AUTHORIZATION = 1002;
    private static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String TAG_PROGRESS_DIALOG_AUTHORIZE = "TAG_PROGRESS_DIALOG_AUTHORIZE";

    @Inject
    AccountChooserPicker accountChooserPicker;

    @InjectPresenter
    CloudOperationPresenter presenter;

    @BindView(R.id.authorizeView)
    View authorizeView;

    @BindView(R.id.operationView)
    View operationView;

    @BindView(R.id.operationInProcessView)
    View operationInProcessView;

    @BindView(R.id.textViewAuthorizationCause)
    TextView textViewAuthorizationCause;

    @BindView(R.id.buttonAuthorize)
    Button buttonAuthorize;

    @BindView(R.id.textViewOperationTitle)
    TextView textViewOperationTitle;

    @BindView(R.id.textViewOperation)
    TextView textViewOperation;

    @BindView(R.id.buttonDone)
    Button buttonDone;

    @BindView(R.id.textViewOperationInProcessTitle)
    TextView textViewOperationInProcessTitle;

    @BindView(R.id.textViewOperationInProcess)
    TextView getTextViewOperationInProcess;

    private CloudOperationType operationType;

    public static Intent getIntent(Context context,
                                   @NonNull CloudOperationType operationType,
                                   @Nullable Sex sex) {
        Intent intent = new Intent(context, CloudOperationActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_TYPE, operationType);
        intent.putExtra(ExtraConstants.EXTRA_SEX, sex);
        return intent;
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        operationType = (CloudOperationType) getIntent().getSerializableExtra(ExtraConstants.EXTRA_TYPE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_operation);

        switch (operationType) {
            case BACKUP:
                textViewAuthorizationCause.setText(R.string.authorize_account_to_backup_data);
                textViewOperationTitle.setText(R.string.backup_data);
                textViewOperation.setText(R.string.backup_data_placeholder);
                buttonDone.setText(R.string.backup);
                textViewOperationInProcessTitle.setText(R.string.backup_data_in_process);
                getTextViewOperationInProcess.setText(R.string.backup_data_placeholder);
                break;
            case RESTORE:
                textViewAuthorizationCause.setText(R.string.authorize_account_to_restore_data);
                textViewOperationTitle.setText(R.string.restore_data);
                textViewOperation.setText(R.string.restore_data_placeholder);
                buttonDone.setText(R.string.restore);
                textViewOperationInProcessTitle.setText(R.string.restore_data_in_process);
                getTextViewOperationInProcess.setText(R.string.restore_data_placeholder);
                break;
        }
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
        switch (operationType) {
            case BACKUP:
                setupToolbarTitle(R.string.backup_data);
                break;
            case RESTORE:
                setupToolbarTitle(R.string.restore_data);
                break;
        }
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        buttonAuthorize.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), true));
        buttonDone.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), true));
    }

    @OnClick(R.id.buttonAuthorize)
    void onAuthorizeClick() {
        presenter.bindAccount();
    }

    @OnClick(R.id.buttonDone)
    void onDoneClick() {
        switch (operationType) {
            case BACKUP:
                presenter.backup();
                break;
            case RESTORE:
                presenter.restore();
                break;
        }
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
    public void chooseAccount(@Nullable String selectedAccountName) {
        accountChooserPicker.show(this, REQUEST_ACCOUNT_PICKER, selectedAccountName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK) {
                    String accountName = null;
                    if (data != null && data.getExtras() != null) {
                        accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    }
                    presenter.accountChosen(accountName);
                }
                break;
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == RESULT_OK) {
                    presenter.continueAfterErrorResolved();
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    presenter.continueAfterErrorResolved();
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
    public void requestAuthorization(Intent intent) {
        startActivityForResult(intent, REQUEST_AUTHORIZATION);
    }

    @Override
    public void connectionUnavailable() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.bind_account_connection_unavailable_dialog_title)
                .setMessage(R.string.bind_account_connection_unavailable_dialog_text)
                .setPositiveButton(R.string.try_again,
                        (DialogInterface dialog, int which) -> presenter.continueAfterErrorResolved())
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> presenter.moveNext())
                .show();
    }

    @Override
    public void showCheckBackupAvailabilityLoading(boolean loading) {
        if (loading) {
            showProgress(TAG_PROGRESS_DIALOG_AUTHORIZE,
                    getString(R.string.please_wait),
                    getString(R.string.check_is_backup_available_loading));
        } else {
            hideProgress(TAG_PROGRESS_DIALOG_AUTHORIZE);
        }
    }

    @Override
    public void checkBackupAvailabilitySucceeded(boolean isBackupAvailable) {
    }

    @Override
    public void noBackupFound() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.no_backup_found)
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> presenter.moveNext())
                .show();
    }

    @Override
    public void failedToCheckBackupAvailability() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.google_drive_error_dialog_text)
                .setPositiveButton(R.string.try_again,
                        (DialogInterface dialog, int which) -> presenter.checkIsBackupAvailable())
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> presenter.moveNext())
                .show();
    }

    @Override
    public void showRestoreLoading(boolean loading) {
    }

    @Override
    public void restoreSucceeded() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.restore_success_dialog_text)
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> presenter.moveNext())
                .show();
    }

    @Override
    public void failedToRestore() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.restore_error_dialog_text)
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> presenter.moveNext())
                .show();
    }

    @Override
    public void showBackupLoading(boolean loading) {
    }

    @Override
    public void backupSucceeded() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.backup_success_dialog_text)
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> presenter.moveNext())
                .show();
    }

    @Override
    public void failedToBackup() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.backup_error_dialog_text)
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> presenter.moveNext())
                .show();
    }

    @Override
    public void navigateToMain() {
        Intent intent = MainActivity.getIntent(this, AppPartition.SETTINGS, getSex());
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        presenter.moveNext();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            presenter.moveNext();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
