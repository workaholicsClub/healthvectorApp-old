package ru.android.childdiary.presentation.profile.edit;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
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
import icepick.State;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.di.modules.ApplicationModule;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.ValidationResult;
import ru.android.childdiary.utils.DoubleUtils;
import ru.android.childdiary.utils.KeyboardUtils;
import ru.android.childdiary.utils.StringUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;
import ru.android.childdiary.utils.ui.WidgetUtils;

public class ProfileEditActivity extends BaseMvpActivity<ProfileEditPresenter> implements ProfileEditView,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, ImagePickerDialogFragment.Listener {
    private static final String TAG_TIME_PICKER = "TIME_PICKER";
    private static final String TAG_DATE_PICKER = "DATE_PICKER";
    private static final String TAG_IMAGE_PICKER = "IMAGE_PICKER";

    private final ProfileEditValidator validator = new ProfileEditValidator(this);

    @InjectPresenter
    ProfileEditPresenter presenter;

    @Inject
    @Named(ApplicationModule.DATE_FORMATTER)
    DateTimeFormatter dateFormatter;

    @Inject
    @Named(ApplicationModule.TIME_FORMATTER)
    DateTimeFormatter timeFormatter;

    @BindView(R.id.rootView)
    View rootView;

    @BindView(R.id.topPanel)
    View topPanel;

    @BindView(R.id.buttonDone)
    Button buttonDone;

    @BindView(R.id.imageViewPhoto)
    ImageView imageViewPhoto;

    @BindView(R.id.textViewAddPhoto)
    TextView textViewAddPhoto;

    @BindView(R.id.editTextChildName)
    EditText editTextName;

    @BindView(R.id.spinnerSex)
    Spinner spinnerSex;
    SpinnerSexAdapter spinnerSexAdapter;

    @BindView(R.id.textViewDate)
    TextView textViewDate;

    @BindView(R.id.textViewTime)
    TextView textViewTime;

    @BindView(R.id.editTextBirthHeight)
    EditText editTextBirthHeight;

    @BindView(R.id.editTextBirthWeight)
    EditText editTextBirthWeight;

    @BindView(R.id.dummy)
    View dummy;

    @State
    Child editedChild;

    @State
    boolean isValidationStarted;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        Child child = getIntent().getParcelableExtra(ExtraConstants.EXTRA_CHILD);
        if (savedInstanceState == null) {
            editedChild = child == null ? Child.builder().build() : Child.getBuilder(child).build();
        }

        getSupportActionBar().setTitle(child == null ? R.string.add_child : R.string.edit_child_long);
        buttonDone.setText(child == null ? R.string.add : R.string.save);
        buttonDone.setOnClickListener(v -> {
            hideKeyboardAndClearFocus();
            isValidationStarted = true;
            ValidationResult result = validator.validateAll();
            if (result.isValid()) {
                if (child == null) {
                    presenter.addChild(editedChild);
                } else {
                    presenter.updateChild(editedChild);
                }
            } else {
                showToast(result.toString());
            }
        });

        setupEditTextViews();
        setupSpinnerSex();
        setupImage();
        setupDate();
        setupTime();
    }

    private void setupEditTextViews() {
        editTextName.setText(editedChild.getName());

        editTextBirthHeight.setText(DoubleUtils.heightReview(this, editedChild.getHeight()));
        editTextBirthWeight.setText(DoubleUtils.weightReview(this, editedChild.getWeight()));

        editTextName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                editTextName.setSelection(editTextName.getText().length());
            } else {
                String name = editTextName.getText().toString().trim();
                editedChild = editedChild.getBuilder(editedChild).name(name).build();
            }
        });
        editTextBirthHeight.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                editTextBirthHeight.setText(DoubleUtils.heightEdit(editedChild.getHeight()));
                editTextBirthHeight.setSelection(editTextBirthHeight.getText().length());
            } else {
                Double height = DoubleUtils.parseHeight(editTextBirthHeight.getText().toString().trim());
                editedChild = editedChild.getBuilder(editedChild).height(height).build();
                editTextBirthHeight.setText(DoubleUtils.heightReview(this, editedChild.getHeight()));
            }
        });
        editTextBirthWeight.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                editTextBirthWeight.setText(DoubleUtils.weightEdit(editedChild.getWeight()));
                editTextBirthWeight.setSelection(editTextBirthWeight.getText().length());
            } else {
                Double weight = DoubleUtils.parseWeight(editTextBirthWeight.getText().toString().trim());
                editedChild = editedChild.getBuilder(editedChild).weight(weight).build();
                editTextBirthWeight.setText(DoubleUtils.weightReview(this, editedChild.getWeight()));
            }
        });

        editTextBirthWeight.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboardAndClearFocus();
                return true;
            }
            return false;
        });

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isValidationStarted) {
                    validator.validateName(false);
                }
            }
        });
        editTextBirthHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isValidationStarted) {
                    validator.validateBirthHeight(false);
                }
            }
        });
        editTextBirthWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isValidationStarted) {
                    validator.validateBirthWeight(false);
                }
            }
        });
    }

    private void hideKeyboardAndClearFocus() {
        View focusedView = rootView.findFocus();
        KeyboardUtils.hideKeyboard(this, focusedView);
        focusedView.clearFocus();
        dummy.requestFocus();
    }

    private void setupSpinnerSex() {
        spinnerSexAdapter = new SpinnerSexAdapter(this, editedChild.getSex() == null);
        spinnerSex.setAdapter(spinnerSexAdapter);
        spinnerSexAdapter.setSexSpinnerPosition(spinnerSex, editedChild.getSex());
        spinnerSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Sex sex = spinnerSexAdapter.getSexSpinnerPosition(spinnerSex);
                if (sex != null && spinnerSexAdapter.hideDefault()) {
                    spinnerSexAdapter.setSexSpinnerPosition(spinnerSex, sex);
                }
                editedChild = editedChild.getBuilder(editedChild).sex(sex).build();
                changeThemeIfNeeded(sex);
                if (isValidationStarted) {
                    validator.validateSex(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void themeChangedCustom() {
        topPanel.setBackgroundResource(ThemeUtils.getHeaderDrawableRes(this, sex));
        buttonDone.setBackgroundResource(ThemeUtils.getButtonBackgroundRes(this, sex));
    }

    private void setupImage() {
        String imageFileName = editedChild.getImageFileName();
        if (imageFileName == null) {
            imageViewPhoto.setImageDrawable(ThemeUtils.getDrawable(this, R.color.white));
        } else {
            imageViewPhoto.setImageDrawable(Drawable.createFromPath(imageFileName));
        }
        textViewAddPhoto.setVisibility(imageFileName == null ? View.VISIBLE : View.GONE);
    }

    private void setupDate() {
        LocalDate birthDate = editedChild.getBirthDate();
        textViewDate.setText(StringUtils.print(birthDate, dateFormatter, getString(R.string.date)));
        WidgetUtils.setupTextView(textViewDate, birthDate != null);
        if (isValidationStarted) {
            validator.validateBirthDate(false);
        }
    }

    private void setupTime() {
        LocalTime birthTime = editedChild.getBirthTime();
        textViewTime.setText(StringUtils.print(birthTime, timeFormatter, getString(R.string.time)));
        WidgetUtils.setupTextView(textViewTime, birthTime != null);
    }

    @OnClick(R.id.imageViewPhoto)
    void onPhotoClick() {
        ImagePickerDialogFragment imagePicker = new ImagePickerDialogFragment();
        imagePicker.showAllowingStateLoss(getSupportFragmentManager(), TAG_DATE_PICKER, sex);
    }

    @OnClick(R.id.textViewDate)
    void onDateClick() {
        LocalDate birthDate = editedChild.getBirthDate();
        Calendar calendar = Calendar.getInstance();
        if (birthDate != null) {
            calendar.setTime(birthDate.toDate());
        }
        DatePickerDialog dpd = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dpd.vibrate(false);
        WidgetUtils.setupDatePicker(this, dpd, sex);
        dpd.show(getFragmentManager(), TAG_DATE_PICKER);
    }

    @OnClick(R.id.textViewTime)
    void onTimeClick() {
        LocalTime birthTime = editedChild.getBirthTime();
        LocalTime time = birthTime == null ? LocalTime.now() : birthTime;
        boolean is24HourFormat = DateFormat.is24HourFormat(this);
        TimePickerDialog tpd = TimePickerDialog.newInstance(this,
                time.getHourOfDay(), time.getMinuteOfHour(), DateFormat.is24HourFormat(this));
        tpd.vibrate(false);
        WidgetUtils.setupTimePicker(this, tpd, sex);
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
    public void onSetImage(@Nullable File resultFile) {
        String imageFileName = resultFile == null ? null : resultFile.getAbsolutePath();
        editedChild = Child.getBuilder(editedChild).imageFileName(imageFileName).build();
        setupImage();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        LocalDate birthDate = LocalDate.fromCalendarFields(calendar);
        editedChild = Child.getBuilder(editedChild).birthDate(birthDate).build();
        setupDate();
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        LocalTime birthTime = new LocalTime(hourOfDay, minute);
        editedChild = Child.getBuilder(editedChild).birthTime(birthTime).build();
        setupTime();
    }

    @Override
    public void childAdded(@NonNull Child child) {
        finish();
    }

    @Override
    public void childUpdated(@NonNull Child child) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_close:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
