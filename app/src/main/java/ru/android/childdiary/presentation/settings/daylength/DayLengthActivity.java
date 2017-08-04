package ru.android.childdiary.presentation.settings.daylength;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.LocalTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDayTimeView;
import ru.android.childdiary.presentation.core.widgets.CustomTimePickerDialog;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class DayLengthActivity extends BaseMvpActivity implements TimePickerDialog.OnTimeSetListener {
    private static final String TAG_TIME_PICKER_START = "TIME_PICKER_START";
    private static final String TAG_TIME_PICKER_FINISH = "TIME_PICKER_FINISH";

    @BindView(R.id.buttonAdd)
    Button buttonAdd;

    @BindView(R.id.rootView)
    View rootView;

    @BindView(R.id.dayStartTimeView)
    FieldDayTimeView dayStartTimeView;

    @BindView(R.id.dayFinishTimeView)
    FieldDayTimeView dayFinishTimeView;

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
        dayStartTimeView.setFieldDialogListener(v -> showTimePicker(TAG_TIME_PICKER_START, dayStartTimeView.getValue()));
        dayFinishTimeView.setFieldDialogListener(v -> showTimePicker(TAG_TIME_PICKER_FINISH, dayFinishTimeView.getValue()));
    }

    @Override
    protected void setContentViewBeforeBind() {
        LayoutInflater inflater = LayoutInflater.from(this);
        detailsView = ButterKnife.findById(this, R.id.detailsView);
        inflater.inflate(R.layout.activity_day_length, detailsView);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), true));
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof TimePickerDialog) {
            ((TimePickerDialog) fragment).setOnTimeSetListener(this);
        }
    }

    private void showTimePicker(String tag, @Nullable LocalTime time) {
        TimePickerDialog tpd = CustomTimePickerDialog.create(this, this, time, getSex());
        tpd.show(getFragmentManager(), tag);
    }

    @Override
    public final void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        LocalTime time = new LocalTime(hourOfDay, minute);
        switch (view.getTag()) {
            case TAG_TIME_PICKER_START:
                dayStartTimeView.setValue(time);
                break;
            case TAG_TIME_PICKER_FINISH:
                dayFinishTimeView.setValue(time);
                break;
        }
    }
}
