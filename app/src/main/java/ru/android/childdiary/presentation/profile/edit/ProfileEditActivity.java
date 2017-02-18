package ru.android.childdiary.presentation.profile.edit;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;

// TODO: доработать
public class ProfileEditActivity extends BaseActivity<ProfileEditPresenter> implements ProfileEditView, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private static final String TAG_TIME_PICKER = "TIME_PICKER";
    private static final String TAG_DATE_PICKER = "DATE_PICKER";
    private static final String TAG_IMAGE_PICKER = "IMAGE_PICKER";

    @InjectPresenter
    ProfileEditPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.buttonDone)
    Button buttonDone;

    @BindView(R.id.spinnerSex)
    Spinner spinnerSex;

    public static Intent getIntent(Context context, @Nullable Child child) {
        Intent intent = new Intent(context, ProfileEditActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_CHILD, child);
        return intent;
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Child child = getIntent().getParcelableExtra(ExtraConstants.EXTRA_CHILD);

        setTheme(getPreferredTheme(child));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getTitle(child));
        actionBar.setSubtitle(getSubtitle(child));
        actionBar.setDisplayHomeAsUpEnabled(true);

        buttonDone.setText(getButtonDoneText(child));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex_variants, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerSex.setAdapter(adapter);
        spinnerSex.setSelection(getSexSpinnerPosition(child));
    }

    private String getTitle(Child child) {
        return child == null ? getString(R.string.add_child) : child.getName();
    }

    private String getSubtitle(Child child) {
        return child == null ? "" : "age";
    }

    private String getButtonDoneText(Child child) {
        return child == null ? getString(R.string.add) : getString(R.string.save);
    }

    private int getSexSpinnerPosition(Child child) {
        if (child == null) {
            return -1;
        }

        return child.getSex() == Sex.MALE ? 0 : 1;
    }

    @OnClick(R.id.imageViewPhoto)
    void onPhotoClick() {
        ImagePickerDialogFragment imagePicker = new ImagePickerDialogFragment();
        imagePicker.show(getSupportFragmentManager(), TAG_DATE_PICKER);
    }

    @OnClick(R.id.buttonDate)
    void onDateClick() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.vibrate(false);
        dpd.show(getFragmentManager(), TAG_DATE_PICKER);
    }

    @OnClick(R.id.buttonTime)
    void onTimeClick() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(this)
        );
        tpd.vibrate(false);
        tpd.show(getFragmentManager(), TAG_TIME_PICKER);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof TimePickerDialog) {
            ((TimePickerDialog) fragment).setOnTimeSetListener(this);
        } else if (fragment instanceof DatePickerDialog) {
            ((DatePickerDialog) fragment).setOnDateSetListener(this);
        }
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        LocalTime localTime = new LocalTime(hourOfDay, minute);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        LocalDate localDate = LocalDate.fromCalendarFields(calendar);
    }
}
