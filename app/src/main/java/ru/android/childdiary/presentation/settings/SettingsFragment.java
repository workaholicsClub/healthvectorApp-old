package ru.android.childdiary.presentation.settings;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.common.GoogleApiAvailability;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import lombok.AccessLevel;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.cloud.CloudOperationActivity;
import ru.android.childdiary.presentation.cloud.CloudOperationType;
import ru.android.childdiary.presentation.core.AppPartitionArguments;
import ru.android.childdiary.presentation.core.BaseMvpFragment;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.permissions.RequestPermissionInfo;
import ru.android.childdiary.presentation.settings.adapters.SettingsAdapter;
import ru.android.childdiary.presentation.settings.adapters.items.BaseSettingsItem;
import ru.android.childdiary.presentation.settings.adapters.items.DelimiterSettingsItem;
import ru.android.childdiary.presentation.settings.adapters.items.GroupSettingsItem;
import ru.android.childdiary.presentation.settings.adapters.items.IntentSettingsItem;
import ru.android.childdiary.utils.ui.AccountChooserPicker;
import ru.android.childdiary.utils.ui.ThemeUtils;

import static android.app.Activity.RESULT_OK;

public class SettingsFragment extends BaseMvpFragment implements SettingsView,
        IntentSettingsItem.Listener {
    private static final String TAG_PROGRESS_DIALOG_AUTHORIZE = "TAG_PROGRESS_DIALOG_AUTHORIZE";
    private static final String TAG_PROGRESS_DIALOG_RESTORE = "TAG_PROGRESS_DIALOG_RESTORE";
    private static final String TAG_PROGRESS_DIALOG_BACKUP = "TAG_PROGRESS_DIALOG_BACKUP";

    private static final int REQUEST_ACCOUNT_PICKER = 1000;
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1001;
    private static final int REQUEST_AUTHORIZATION = 1002;
    private static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    @Inject
    AccountChooserPicker accountChooserPicker;

    @InjectPresenter
    SettingsPresenter presenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private SettingsAdapter settingsAdapter;

    @Getter(AccessLevel.PROTECTED)
    private LocalDate selectedDate;

    @Getter(AccessLevel.PROTECTED)
    private Child child;

    @Nullable
    @Getter(AccessLevel.PROTECTED)
    private Sex sex;

    private IntentSettingsItem accountItem, backupItem, restoreItem;

    @Override
    protected void injectFragment(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        AppPartitionArguments arguments = (AppPartitionArguments) getArguments().getSerializable(ExtraConstants.EXTRA_APP_PARTITION_ARGUMENTS);
        selectedDate = arguments.getSelectedDate();
        child = arguments.getChild();
        sex = child.getSex();
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupUi();
        themeChanged();
    }

    private void setupUi() {
        accountItem = IntentSettingsItem.builder()
                .id(Intention.ACCOUNT.ordinal())
                .title(getString(R.string.settings_account))
                .iconRes(R.drawable.ic_settings_account)
                .listener(this)
                .enabled(true)
                .build();
        backupItem = IntentSettingsItem.builder()
                .id(Intention.BACKUP.ordinal())
                .title(getString(R.string.settings_backup_data))
                .iconRes(R.drawable.ic_settings_backup_data)
                .listener(this)
                .enabled(false)
                .build();
        restoreItem = IntentSettingsItem.builder()
                .id(Intention.RESTORE.ordinal())
                .title(getString(R.string.settings_restore_data))
                .iconRes(R.drawable.ic_settings_restore_data)
                .listener(this)
                .enabled(false)
                .build();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        settingsAdapter = new SettingsAdapter(getContext());
        settingsAdapter.setItems(generateItems());
        recyclerView.setAdapter(settingsAdapter);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
    }

    private void themeChanged() {
        logger.debug("setup theme: " + sex);
        settingsAdapter.setSex(sex);
    }

    @Override
    public void showSelectedDate(@NonNull LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    @Override
    public void showChild(@NonNull Child child) {
        this.child = child;
        if (sex != child.getSex()) {
            logger.debug("theme switched");
            sex = child.getSex();
            themeChanged();
        }
    }

    @Override
    public void showSelectedAccount(@NonNull String accountName) {
        accountItem = accountItem.toBuilder().subtitle(accountName).build();
        boolean enabled = !TextUtils.isEmpty(accountName);
        backupItem = backupItem.toBuilder().enabled(enabled).build();
        restoreItem = restoreItem.toBuilder().enabled(enabled).build();
        settingsAdapter.updateItem(accountItem);
        settingsAdapter.updateItem(backupItem);
        settingsAdapter.updateItem(restoreItem);
    }

    private List<BaseSettingsItem> generateItems() {
        List<BaseSettingsItem> items = new ArrayList<>();
        int id = 1000;
        // TODO: generate child profiles items with special ids
        items.add(GroupSettingsItem.builder()
                .id(++id)
                .title(getString(R.string.settings_notifications))
                .build());
        items.add(IntentSettingsItem.builder()
                .id(Intention.NOTIFICATIONS.ordinal())
                .title(getString(R.string.settings_setup_notifications))
                .iconRes(R.drawable.ic_notify_time)
                .listener(this)
                .enabled(true)
                .build());
        items.add(DelimiterSettingsItem.builder()
                .id(++id)
                .build());
        items.add(GroupSettingsItem.builder()
                .id(++id)
                .title(getString(R.string.settings_data_control))
                .build());
        items.add(accountItem);
        items.add(backupItem);
        items.add(restoreItem);
        items.add(DelimiterSettingsItem.builder()
                .id(++id)
                .build());
        items.add(GroupSettingsItem.builder()
                .id(++id)
                .title(getString(R.string.settings_additional))
                .build());
        items.add(IntentSettingsItem.builder()
                .id(Intention.DAY_LENGTH.ordinal())
                .title(getString(R.string.settings_day_length))
                .iconRes(R.drawable.ic_length)
                .listener(this)
                .enabled(true)
                .build());
        items.add(IntentSettingsItem.builder()
                .id(Intention.FOOD_MEASURE.ordinal())
                .title(getString(R.string.settings_food_measure))
                .iconRes(R.drawable.ic_food_measure)
                .listener(this)
                .enabled(true)
                .build());
        items.add(IntentSettingsItem.builder()
                .id(Intention.FOOD.ordinal())
                .title(getString(R.string.settings_food))
                .iconRes(R.drawable.ic_food)
                .listener(this)
                .enabled(true)
                .build());
        return items;
    }

    @Override
    public void onClick(IntentSettingsItem item) {
        Intention intention = Intention.values()[item.getId()];
        switch (intention) {
            case NOTIFICATIONS:
                break;
            case ACCOUNT:
                presenter.bindAccount();
                break;
            case BACKUP: {
                Intent intent = CloudOperationActivity.getIntent(getContext(),
                        CloudOperationType.BACKUP, getSex());
                startActivity(intent);
                getActivity().finish();
                break;
            }
            case RESTORE: {
                Intent intent = CloudOperationActivity.getIntent(getContext(),
                        CloudOperationType.RESTORE, getSex());
                startActivity(intent);
                getActivity().finish();
                break;
            }
            case DAY_LENGTH:
                break;
            case FOOD_MEASURE:
                break;
            case FOOD:
                break;
            default:
                throw new IllegalArgumentException("Unsupported intention");
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    getActivity(),
                    connectionStatusCode,
                    REQUEST_GOOGLE_PLAY_SERVICES);
            dialog.show();
        } else {
            new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
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
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
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
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.no_backup_found)
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> presenter.moveNext())
                .show();
    }

    @Override
    public void failedToCheckBackupAvailability() {
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.google_drive_error_dialog_text)
                .setPositiveButton(R.string.try_again,
                        (DialogInterface dialog, int which) -> presenter.checkIsBackupAvailable())
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> presenter.moveNext())
                .show();
    }

    @Override
    public void showRestoreLoading(boolean loading) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void restoreSucceeded() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void failedToRestore() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void showBackupLoading(boolean loading) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void backupSucceeded() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void failedToBackup() {
        throw new UnsupportedOperationException("Not implemented");
    }

    private enum Intention {
        NOTIFICATIONS, ACCOUNT, BACKUP, RESTORE, DAY_LENGTH, FOOD_MEASURE, FOOD
    }
}
