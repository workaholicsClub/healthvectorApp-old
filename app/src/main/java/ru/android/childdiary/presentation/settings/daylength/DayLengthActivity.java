package ru.android.childdiary.presentation.settings.daylength;

import android.content.Context;
import android.content.Intent;
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

import org.joda.time.LocalTime;

import butterknife.BindView;
import butterknife.OnClick;
import icepick.State;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDayTimeView;
import ru.android.childdiary.presentation.profile.dialogs.TimePickerDialogArguments;
import ru.android.childdiary.presentation.profile.dialogs.TimePickerDialogFragment;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class DayLengthActivity extends BaseMvpActivity implements DayLengthView, TimePickerDialogFragment.Listener {
    private static final String TAG_TIME_PICKER_START = "TIME_PICKER_START";
    private static final String TAG_TIME_PICKER_FINISH = "TIME_PICKER_FINISH";

    @InjectPresenter
    DayLengthPresenter presenter;

    @BindView(R.id.buttonAdd)
    Button buttonAdd;

    @BindView(R.id.dayStartTimeView)
    FieldDayTimeView dayStartTimeView;

    @BindView(R.id.dayFinishTimeView)
    FieldDayTimeView dayFinishTimeView;

    @Nullable
    @State
    LocalTime startTime, finishTime;

    private ViewGroup detailsView;

    public static Intent getIntent(Context context, @Nullable Sex sex) {
        return new Intent(context, DayLengthActivity.class)
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
        dayStartTimeView.setFieldDialogListener(v -> {
            LocalTime maxTime = finishTime == null ? null : finishTime.minusHours(1);
            showTimePicker(TAG_TIME_PICKER_START, getString(R.string.day_start),
                    startTime, null, maxTime);
        });
        dayFinishTimeView.setFieldDialogListener(v -> {
            LocalTime minTime = startTime == null ? null : startTime.plusHours(1);
            showTimePicker(TAG_TIME_PICKER_FINISH, getString(R.string.day_finish),
                    finishTime, minTime, null);
        });
        if (savedInstanceState != null) {
            dayStartTimeView.setValue(startTime);
            dayFinishTimeView.setValue(finishTime);
        }
    }

    @Override
    protected void setContentViewBeforeBind() {
        LayoutInflater inflater = LayoutInflater.from(this);
        detailsView = findViewById(R.id.detailsView);
        inflater.inflate(R.layout.activity_day_length, detailsView);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.settings_day_length);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), true));
    }

    private void showTimePicker(String tag,
                                @Nullable String title,
                                @Nullable LocalTime time,
                                @Nullable LocalTime minTime, @Nullable LocalTime maxTime) {
        TimePickerDialogFragment dialogFragment = new TimePickerDialogFragment();
        dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), tag,
                TimePickerDialogArguments.builder()
                        .sex(getSex())
                        .title(title)
                        .time(time)
                        .minTime(minTime)
                        .maxTime(maxTime)
                        .build());
    }

    @Override
    public void onTimePick(String tag, @NonNull LocalTime time) {
        switch (tag) {
            case TAG_TIME_PICKER_START:
                startTime = time;
                dayStartTimeView.setValue(time);
                break;
            case TAG_TIME_PICKER_FINISH:
                finishTime = time;
                dayFinishTimeView.setValue(time);
                break;
        }
    }

    @Override
    public void showStartTime(@NonNull LocalTime time) {
        startTime = time;
        dayStartTimeView.setValue(time);
    }

    @Override
    public void showFinishTime(@NonNull LocalTime time) {
        finishTime = time;
        dayFinishTimeView.setValue(time);
    }

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

    @OnClick(R.id.buttonAdd)
    void onSaveClick() {
        presenter.forceSave(startTime, finishTime);
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
        presenter.save(startTime, finishTime);
    }
}
