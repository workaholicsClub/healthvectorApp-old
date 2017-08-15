package ru.android.childdiary.presentation.settings.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.widgets.FieldCheckBoxView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldNotifyTimeView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldTextViewWithImageView;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class NotificationActivity extends BaseMvpActivity implements NotificationView {
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

    // TODO Melody

    @BindView(R.id.checkBoxViewVibration)
    FieldCheckBoxView checkBoxViewVibration;

    private ViewGroup detailsView;

    public static Intent getIntent(Context context, @Nullable Sex sex) {
        return new Intent(context, NotificationActivity.class)
                .putExtra(ExtraConstants.EXTRA_SEX, sex);
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
    }

    /*
        @Override
        public void exit(boolean saved) {
            finish();
        }

        @Override
        public void confirmSave(@NonNull LocalTime startTime, @NonNull LocalTime finishTime) {
            new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                    .setTitle(R.string.save_changes_dialog_title)
                    .setPositiveButton(R.string.save,
                            (dialog, which) -> presenter.forceSave(startTime, finishTime))
                    .setNegativeButton(R.string.cancel,
                            (dialog, which) -> exit(false))
                    .show();
        }
    */

    @OnClick(R.id.buttonAdd)
    void onSaveClick() {
        // TODO presenter.forceSave(dayStartTimeView.getValue(), dayFinishTimeView.getValue());
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
        finish();
        // TODO presenter.save(dayStartTimeView.getValue(), dayFinishTimeView.getValue());
    }
}
