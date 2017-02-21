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
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.di.modules.ApplicationModule;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.utils.UiUtils;
import ru.android.childdiary.utils.Utils;

public class ProfileEditActivity extends BaseMvpActivity<ProfileEditPresenter> implements ProfileEditView,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, ImagePickerDialogFragment.Listener {
    private static final String TAG_TIME_PICKER = "TIME_PICKER";
    private static final String TAG_DATE_PICKER = "DATE_PICKER";
    private static final String TAG_IMAGE_PICKER = "IMAGE_PICKER";

    @InjectPresenter
    ProfileEditPresenter presenter;

    @Inject
    @Named(ApplicationModule.DATE_FORMATTER)
    DateTimeFormatter dateFormatter;

    @Inject
    @Named(ApplicationModule.TIME_FORMATTER)
    DateTimeFormatter timeFormatter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.buttonDone)
    Button buttonDone;

    @BindView(R.id.textViewAddPhoto)
    TextView textViewAddPhoto;

    @BindView(R.id.imageViewPhoto)
    ImageView imageViewPhoto;

    @BindView(R.id.editTextChildName)
    EditText editTextChildName;

    @BindView(R.id.buttonDate)
    TextView buttonDate;

    @BindView(R.id.buttonTime)
    TextView buttonTime;

    @BindView(R.id.editTextBirthHeight)
    EditText editTextBirthHeight;

    @BindView(R.id.editTextBirthWeight)
    EditText editTextBirthWeight;

    @BindView(R.id.spinnerSex)
    Spinner spinnerSex;

    @BindView(R.id.dummy)
    View dummy;

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
        setTheme(UiUtils.getPreferredTheme(child));
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
            actionBar.setTitle(getString(R.string.edit_child));
            buttonDone.setText(getString(R.string.save));
        }

        Icepick.restoreInstanceState(this, savedInstanceState);

        if (savedInstanceState == null && child != null) {
            String imageFileName = child.getImageFileName();
            imageFileUri = imageFileName == null ? null : Uri.fromFile(new File(imageFileName));
            birthDate = child.getBirthDate();
            birthTime = child.getBirthTime();

            editTextChildName.setText(child.getName());
            editTextBirthHeight.setText(Double.toString(child.getHeight()));
            editTextBirthWeight.setText(Double.toString(child.getWeight()));

            editTextChildName.setSelection(editTextChildName.getText().length());
        }

        editTextBirthWeight.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                editTextBirthHeight.setSelection(editTextBirthHeight.getText().length());
            }
        });
        editTextBirthWeight.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                editTextBirthWeight.setSelection(editTextBirthWeight.getText().length());
            }
        });
        editTextBirthWeight.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Utils.hideKeyboard(this, v);
                v.clearFocus();
                dummy.requestFocus();
                return true;
            }
            return false;
        });

        setupImage();
        setupDate();
        setupTime();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        // необязательные параметры: время и дата рождения
        try {
            String name = editTextChildName.getText().toString();
            double height = Double.parseDouble(editTextBirthHeight.getText().toString());
            double weight = Double.parseDouble(editTextBirthWeight.getText().toString());
            Sex sex = getSexFromSpinnerPosition();
            String imageFileName = imageFileUri == null ? null : imageFileUri.getPath();
            builder.name(name)
                    .birthDate(birthDate)
                    .birthTime(birthTime)
                    .sex(sex)
                    .imageFileName(imageFileName)
                    .height(height)
                    .weight(weight);
        } catch (Exception e) {
            logger.error("validation failed", e);
        }
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
        Calendar calendar = Calendar.getInstance();
        if (birthDate != null) {
            calendar.setTime(birthDate.toDate());
        }
        DatePickerDialog dpd = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dpd.vibrate(false);
        dpd.show(getFragmentManager(), TAG_DATE_PICKER);
    }

    @OnClick(R.id.buttonTime)
    void onTimeClick() {
        LocalTime time = birthTime == null ? LocalTime.now() : birthTime;
        boolean is24HourFormat = DateFormat.is24HourFormat(this);
        TimePickerDialog tpd = TimePickerDialog.newInstance(this,
                time.getHourOfDay(), time.getMinuteOfHour(), DateFormat.is24HourFormat(this));
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
    public void onSetImage(@Nullable File resultFile) {
        imageFileUri = resultFile == null ? null : Uri.fromFile(resultFile);
        setupImage();
    }

    private void setupDate() {
        buttonDate.setText(birthDate == null
                ? getString(R.string.date)
                : birthDate.toString(dateFormatter));
    }

    private void setupTime() {
        buttonTime.setText(birthTime == null
                ? getString(R.string.time)
                : birthTime.toString(timeFormatter));
    }

    private void setupImage() {
        if (imageFileUri == null) {
            imageViewPhoto.setImageDrawable(getResources().getDrawable(android.R.color.white));
        } else {
            imageViewPhoto.setImageURI(imageFileUri);
        }
        textViewAddPhoto.setVisibility(imageFileUri == null ? View.VISIBLE : View.GONE);
    }
}
