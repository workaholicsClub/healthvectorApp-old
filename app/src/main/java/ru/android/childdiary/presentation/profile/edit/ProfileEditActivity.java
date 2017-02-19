package ru.android.childdiary.presentation.profile.edit;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.io.File;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;

public class ProfileEditActivity extends BaseActivity<ProfileEditPresenter> implements ProfileEditView,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, ImagePickerDialogFragment.Listener {
    private static final String TAG_TIME_PICKER = "TIME_PICKER";
    private static final String TAG_DATE_PICKER = "DATE_PICKER";
    private static final String TAG_IMAGE_PICKER = "IMAGE_PICKER";

    @InjectPresenter
    ProfileEditPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.buttonDone)
    Button buttonDone;

    @BindView(R.id.textViewAddPhoto)
    TextView textViewAddPhoto;

    @BindView(R.id.imageViewPhoto)
    ImageView imageViewPhoto;

    @BindView(R.id.editChildName)
    EditText editChildName;

    @BindView(R.id.buttonDate)
    Button buttonDate;

    @BindView(R.id.buttonTime)
    Button buttonTime;

    @BindView(R.id.editBirthHeight)
    EditText editBirthHeight;

    @BindView(R.id.editBirthWeight)
    EditText editBirthWeight;

    @BindView(R.id.spinnerSex)
    Spinner spinnerSex;

    @State
    Uri imageFileUri;

    @State
    LocalDate birthDate;

    @State
    LocalTime birthTime;

    private Child child;

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
        child = getIntent().getParcelableExtra(ExtraConstants.EXTRA_CHILD);

        setTheme(getPreferredTheme(child));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex_variants, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerSex.setAdapter(adapter);
        setSexSpinnerPosition(child);

        if (child == null) {
            actionBar.setTitle(getString(R.string.add_child));
            buttonDone.setText(getString(R.string.add));
        } else {
            actionBar.setTitle(getString(R.string.update_child));
            buttonDone.setText(getString(R.string.save));
        }

        Icepick.restoreInstanceState(this, savedInstanceState);

        setupImage();
        setupDate();
        setupTime();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    private void setSexSpinnerPosition(Child child) {
        int position = -1;
        if (child != null) {
            position = child.getSex() == Sex.MALE ? 0 : 1;
        }
        spinnerSex.setSelection(position);
    }

    private Sex getSexFromSpinnerPosition() {
        return spinnerSex.getSelectedItemPosition() == 0 ? Sex.MALE : Sex.FEMALE;
    }

    @OnClick(R.id.buttonDone)
    void onDoneClick() {
        Child.ChildBuilder builder = Child.getBuilder(child);
        fill(builder);
        if (child == null) {
            presenter.addChild(builder.build());
        } else {
            presenter.updateChild(builder.build());
        }
    }

    private void fill(Child.ChildBuilder builder) {
        // TODO: validation
        String name = editChildName.getText().toString();
        double height = Double.parseDouble(editBirthHeight.getText().toString());
        double weight = Double.parseDouble(editBirthWeight.getText().toString());
        Sex sex = getSexFromSpinnerPosition();
        String imageFileName = imageFileUri == null ? null : imageFileUri.getPath();
        builder.name(name)
                .birthDate(birthDate)
                .birthTime(birthTime)
                .sex(sex)
                .imageFileName(imageFileName)
                .height(height)
                .weight(weight);
    }

    @Override
    public void childAdded(Child child) {
        finish();
    }

    @Override
    public void childUpdated(Child child) {
        finish();
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

    @OnClick(R.id.imageViewPhoto)
    void onPhotoClick() {
        ImagePickerDialogFragment imagePicker = new ImagePickerDialogFragment();
        imagePicker.show(getSupportFragmentManager(), TAG_DATE_PICKER);
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
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        birthDate = LocalDate.fromCalendarFields(calendar);
        setupDate();
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        birthTime = new LocalTime(hourOfDay, minute);
        setupTime();
    }

    @Override
    public void onSetImage(File resultFile) {
        imageFileUri = Uri.fromFile(resultFile);
        setupImage();
    }

    private void setupDate() {
        buttonDate.setText(birthDate == null ? getString(R.string.date) : birthDate.toString());
    }

    private void setupTime() {
        buttonTime.setText(birthTime == null ? getString(R.string.time) : birthTime.toString());
    }

    private void setupImage() {
        imageViewPhoto.setImageURI(imageFileUri);
        textViewAddPhoto.setVisibility(imageFileUri == null ? View.VISIBLE : View.GONE);
    }
}
