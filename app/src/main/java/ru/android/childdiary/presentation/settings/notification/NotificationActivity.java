package ru.android.childdiary.presentation.settings.notification;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.calendar.data.core.EventNotification;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.dialogs.TimeDialogArguments;
import ru.android.childdiary.presentation.core.fields.dialogs.TimeDialogFragment;
import ru.android.childdiary.presentation.core.fields.widgets.FieldCheckBoxView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldNotificationSoundView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldNotifyTimeView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldTextViewWithImageView;
import ru.android.childdiary.presentation.core.permissions.RequestPermissionInfo;
import ru.android.childdiary.presentation.settings.notification.ringtones.RingtonePickerDialogArguments;
import ru.android.childdiary.presentation.settings.notification.ringtones.RingtonePickerDialogFragment;
import ru.android.childdiary.utils.strings.StringUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class NotificationActivity extends BaseMvpActivity implements NotificationView,
        TimeDialogFragment.Listener, FieldCheckBoxView.FieldCheckBoxListener,
        RingtonePickerDialogFragment.Listener {
    private static final String TAG_NOTIFY_TIME_DIALOG = "TAG_NOTIFY_TIME_DIALOG";
    private static final String TAG_RINGTONE_PICKER = "TAG_RINGTONE_PICKER";
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 101;

    @InjectPresenter
    NotificationPresenter presenter;

    @BindView(R.id.buttonAdd)
    Button buttonAdd;

    @BindView(R.id.eventTypeNameView)
    FieldTextViewWithImageView eventTypeNameView;

    @BindView(R.id.checkBoxViewDontNotify)
    FieldCheckBoxView checkBoxViewDontNotify;

    @BindView(R.id.notifyTimeView)
    FieldNotifyTimeView notifyTimeView;

    @BindView(R.id.notificationSoundView)
    FieldNotificationSoundView notificationSoundView;

    @BindView(R.id.checkBoxViewVibration)
    FieldCheckBoxView checkBoxViewVibration;

    private ViewGroup detailsView;
    private EventType eventType;

    public static Intent getIntent(Context context, @Nullable Sex sex, @NonNull EventType eventType) {
        return new Intent(context, NotificationActivity.class)
                .putExtra(ExtraConstants.EXTRA_SEX, sex)
                .putExtra(ExtraConstants.EXTRA_TYPE, eventType);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        buttonAdd.setText(R.string.save);

        eventType = (EventType) getIntent().getSerializableExtra(ExtraConstants.EXTRA_TYPE);
        presenter.init(eventType);
        eventTypeNameView.setText(StringUtils.eventType(this, eventType));
        checkBoxViewDontNotify.setFieldCheckBoxListener(this);
        notifyTimeView.setFieldDialogListener(v -> {
            TimeDialogFragment dialogFragment = new TimeDialogFragment();
            dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_NOTIFY_TIME_DIALOG,
                    TimeDialogArguments.builder()
                            .sex(getSex())
                            .minutes(notifyTimeView.getValueInt())
                            .showDays(true)
                            .showHours(true)
                            .showMinutes(true)
                            .title(getString(R.string.notify_time_dialog_title))
                            .build());
        });
        notificationSoundView.setFieldDialogListener(v -> requestPermissionAndPickRingtone());
    }

    private void requestPermissionAndPickRingtone() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            RequestPermissionInfo permissionInfo = RequestPermissionInfo.builder()
                    .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .requestCode(REQUEST_PERMISSION_READ_EXTERNAL_STORAGE)
                    .title(getString(R.string.request_read_external_storage_permission_title))
                    .text(getString(R.string.request_read_external_storage_permission_text))
                    .build();
            permissionHelper.requestPermission(permissionInfo, getSex());
        } else {
            pickRingtone();
        }
    }

    @Override
    public void permissionGranted(RequestPermissionInfo permissionInfo) {
        super.permissionGranted(permissionInfo);
        if (permissionInfo.getRequestCode() == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            pickRingtone();
        }
    }

    private void pickRingtone() {
        RingtonePickerDialogFragment dialogFragment = new RingtonePickerDialogFragment();
        dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_RINGTONE_PICKER,
                RingtonePickerDialogArguments.builder()
                        .sex(getSex())
                        .soundUri(Uri.parse("sdfsdf"))//notificationSoundView.getValue())
                        .build());
    }

    @Override
    public void onRingtoneSelected(String ringtoneName, Uri ringtoneUri) {
        notificationSoundView.setValue(ringtoneUri);
    }

    @Override
    protected void setContentViewBeforeBind() {
        LayoutInflater inflater = LayoutInflater.from(this);
        detailsView = findViewById(R.id.detailsView);
        inflater.inflate(R.layout.activity_notification, detailsView);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.settings_setup_notification);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), true));
        checkBoxViewDontNotify.setSex(getSex());
        checkBoxViewVibration.setSex(getSex());
    }

    private EventNotification buildEventNotification() {
        return EventNotification.builder()
                .eventType(eventType)
                .minutes(notifyTimeView.getValue())
                .sound(notificationSoundView.getValue())
                .dontNotify(checkBoxViewDontNotify.isChecked())
                .vibration(checkBoxViewVibration.isChecked())
                .build();
    }

    @Override
    public void showNotificationSettings(@NonNull EventNotification eventNotification) {
        checkBoxViewDontNotify.setChecked(eventNotification.isDontNotify());
        notifyTimeView.setValue(eventNotification.getMinutes());
        notificationSoundView.setValue(eventNotification.getSound());
        checkBoxViewVibration.setChecked(eventNotification.isVibration());
    }

    @Override
    public void exit(@NonNull EventNotification eventNotification) {
        finish();
    }

    @Override
    public void confirmSave(@NonNull EventNotification eventNotification) {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.save_changes_dialog_title)
                .setPositiveButton(R.string.save,
                        (dialog, which) -> presenter.forceSave(eventNotification))
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> exit(eventNotification))
                .show();
    }

    @OnClick(R.id.buttonAdd)
    void onSaveClick() {
        presenter.forceSave(buildEventNotification());
    }

    @Override
    public void onChecked() {
        boolean enabled = !checkBoxViewDontNotify.isChecked();
        notifyTimeView.setEnabled(enabled);
        notificationSoundView.setEnabled(enabled);
        checkBoxViewVibration.setEnabled(enabled);
    }

    @Override
    public void onSetTime(String tag, int minutes) {
        notifyTimeView.setValue(minutes);
    }

    @Override
    public void onBackPressed() {
        saveChangesOrExit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveChangesOrExit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveChangesOrExit() {
        presenter.save(buildEventNotification());
    }
}
