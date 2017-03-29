package ru.android.childdiary.presentation.profile.edit;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ListPopupWindow;
import android.text.InputFilter;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import icepick.State;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;
import ru.android.childdiary.presentation.core.widgets.RegExpInputFilter;
import ru.android.childdiary.presentation.profile.edit.adapters.SexAdapter;
import ru.android.childdiary.presentation.profile.edit.image.ImagePickerDialogFragment;
import ru.android.childdiary.utils.DateUtils;
import ru.android.childdiary.utils.DoubleUtils;
import ru.android.childdiary.utils.KeyboardUtils;
import ru.android.childdiary.utils.StringUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public class ProfileEditActivity extends BaseMvpActivity implements ProfileEditView,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, ImagePickerDialogFragment.Listener,
        AdapterView.OnItemClickListener, PopupWindow.OnDismissListener {
    private static final String TAG_TIME_PICKER = "TIME_PICKER";
    private static final String TAG_DATE_PICKER = "DATE_PICKER";
    private static final String TAG_IMAGE_PICKER = "IMAGE_PICKER";

    private final List<OnUpdateChildListener> onUpdateChildListeners = new ArrayList<>();

    @InjectPresenter
    ProfileEditPresenter presenter;

    @BindView(R.id.rootView)
    View rootView;

    @BindView(R.id.topPanel)
    View topPanel;

    @BindView(R.id.buttonDone)
    Button buttonDone;

    @BindView(R.id.imageViewPhoto)
    ImageView imageViewPhoto;

    @BindView(R.id.textViewPhoto)
    TextView textViewPhoto;

    @BindView(R.id.editTextChildName)
    CustomEditText editTextName;

    @BindView(R.id.textViewSex)
    TextView textViewSex;

    @BindView(R.id.textViewDate)
    TextView textViewBirthDate;

    @BindView(R.id.textViewTime)
    TextView textViewBirthTime;

    @BindView(R.id.editTextBirthHeight)
    CustomEditText editTextBirthHeight;

    @BindView(R.id.editTextBirthWeight)
    CustomEditText editTextBirthWeight;

    @BindView(R.id.dummy)
    View dummy;

    @BindDimen(R.dimen.name_edit_text_padding_bottom)
    int editTextBottomPadding;

    @State
    boolean isButtonDoneEnabled;

    @State
    Child editedChild = Child.NULL;

    private boolean isValidationStarted;

    private ListPopupWindow popupWindow;

    public static Intent getIntent(Context context, @Nullable Child child) {
        Intent intent = new Intent(context, ProfileEditActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_CHILD, child);
        return intent;
    }

    void addOnUpdateChildListener(OnUpdateChildListener listener) {
        onUpdateChildListeners.add(listener);
    }

    void removeOnUpdateChildListener(OnUpdateChildListener listener) {
        onUpdateChildListeners.remove(listener);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        Child child = (Child) getIntent().getSerializableExtra(ExtraConstants.EXTRA_CHILD);
        if (savedInstanceState == null && child != null) {
            editedChild = child.toBuilder().build();
        }

        setupToolbarTitle(child == null ? R.string.add_profile : R.string.profile);
        buttonDone.setText(child == null ? R.string.add : R.string.save);
        buttonDone.setOnClickListener(v -> {
            hideKeyboardAndClearFocus(rootView.findFocus());
            if (child == null) {
                presenter.addChild(editedChild);
            } else {
                if (child.equals(editedChild)) {
                    finish();
                } else {
                    presenter.updateChild(editedChild);
                }
            }
        });

        setupTextViews();
        setupSex();
        setupImage();
        setupDate();
        setupTime();

        unsubscribeOnDestroy(presenter.listenForDoneButtonUpdate(new ChildObservable(this)));
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        topPanel.setBackgroundResource(ThemeUtils.getColorPrimaryRes(getSex()));
        buttonDone.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), isButtonDoneEnabled));
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideKeyboardAndClearFocus(rootView.findFocus());
    }

    private void updateChild(Child child) {
        editedChild = child;
        for (OnUpdateChildListener listener : onUpdateChildListeners) {
            listener.onUpdateChild(child);
        }
    }

    private void setupTextViews() {
        editTextBirthHeight.setFilters(new InputFilter[]{new RegExpInputFilter.HeightInputFilter()});
        editTextBirthWeight.setFilters(new InputFilter[]{new RegExpInputFilter.WeightInputFilter()});

        editTextName.setText(editedChild.getName());
        editTextBirthHeight.setText(DoubleUtils.heightReview(this, editedChild.getBirthHeight()));
        editTextBirthWeight.setText(DoubleUtils.weightReview(this, editedChild.getBirthWeight()));

        unsubscribeOnDestroy(RxTextView.afterTextChangeEvents(editTextName).subscribe(textViewAfterTextChangeEvent -> {
            String name = editTextName.getText().toString().trim();
            updateChild(editedChild.toBuilder().name(name).build());
        }));
        unsubscribeOnDestroy(RxTextView.afterTextChangeEvents(editTextBirthHeight).subscribe(textViewAfterTextChangeEvent -> {
            Double height = DoubleUtils.parse(editTextBirthHeight.getText().toString());
            updateChild(editedChild.toBuilder().birthHeight(height).build());
        }));
        unsubscribeOnDestroy(RxTextView.afterTextChangeEvents(editTextBirthWeight).subscribe(textViewAfterTextChangeEvent -> {
            Double weight = DoubleUtils.parse(editTextBirthWeight.getText().toString());
            updateChild(editedChild.toBuilder().birthWeight(weight).build());
        }));

        unsubscribeOnDestroy(RxView.focusChanges(editTextName).subscribe((hasFocus) -> {
            if (hasFocus) {
                editTextName.setSelection(editTextName.getText().length());
            }
        }));
        unsubscribeOnDestroy(RxView.focusChanges(editTextBirthHeight).subscribe(hasFocus -> {
            if (hasFocus) {
                editTextBirthHeight.setText(DoubleUtils.heightEdit(editedChild.getBirthHeight()));
                editTextBirthHeight.setSelection(editTextBirthHeight.getText().length());
            } else {
                editTextBirthHeight.setText(DoubleUtils.heightReview(this, editedChild.getBirthHeight()));
            }
        }));
        unsubscribeOnDestroy(RxView.focusChanges(editTextBirthWeight).subscribe(hasFocus -> {
            if (hasFocus) {
                editTextBirthWeight.setText(DoubleUtils.weightEdit(editedChild.getBirthWeight()));
                editTextBirthWeight.setSelection(editTextBirthWeight.getText().length());
            } else {
                editTextBirthWeight.setText(DoubleUtils.weightReview(this, editedChild.getBirthWeight()));
            }
        }));
        unsubscribeOnDestroy(RxTextView.editorActionEvents(editTextBirthWeight).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_DONE) {
                hideKeyboardAndClearFocus(editTextBirthWeight);
            }
        }));
        editTextName.setOnKeyboardHiddenListener(this::hideKeyboardAndClearFocus);
        editTextBirthHeight.setOnKeyboardHiddenListener(this::hideKeyboardAndClearFocus);
        editTextBirthWeight.setOnKeyboardHiddenListener(this::hideKeyboardAndClearFocus);
    }

    private void hideKeyboardAndClearFocus(View view) {
        KeyboardUtils.hideKeyboard(this, view);
        view.clearFocus();
        dummy.requestFocus();
    }

    private void setupSex() {
        Sex sex = editedChild.getSex();
        changeThemeIfNeeded(sex);
        textViewSex.setText(StringUtils.sex(this, sex, getString(R.string.select_sex)));
        WidgetsUtils.setupTextView(textViewSex, sex != null);
    }

    @OnClick(R.id.textViewSexWrapper)
    void onSexClick(View v) {
        dismissPopupWindow();
        ListAdapter adapter = new SexAdapter(this);
        View anchor = v;
        int width = textViewSex.getWidth();
        int gravity = Gravity.START;

        popupWindow = new ListPopupWindow(this);
        popupWindow.setWidth(width);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setAdapter(adapter);
        popupWindow.setAnchorView(anchor);
        popupWindow.setDropDownGravity(gravity);
        popupWindow.setOnItemClickListener(this);
        popupWindow.setOnDismissListener(this);
        popupWindow.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismissPopupWindow();
        Sex sex = ((SexAdapter) parent.getAdapter()).getItem(position);
        updateChild(editedChild.toBuilder().sex(sex).build());
        setupSex();
    }

    @Override
    public void onDismiss() {
        dismissPopupWindow();
    }

    private boolean dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            return true;
        }
        popupWindow = null;
        return false;
    }

    private void setupImage() {
        String imageFileName = editedChild.getImageFileName();
        if (imageFileName == null) {
            imageViewPhoto.setImageDrawable(ContextCompat.getDrawable(this, R.color.white));
        } else {
            imageViewPhoto.setImageDrawable(Drawable.createFromPath(imageFileName));
        }
        textViewPhoto.setVisibility(imageFileName == null ? View.VISIBLE : View.GONE);
    }

    private void setupDate() {
        LocalDate birthDate = editedChild.getBirthDate();
        textViewBirthDate.setText(DateUtils.date(this, birthDate, getString(R.string.date)));
        WidgetsUtils.setupTextView(textViewBirthDate, birthDate != null);
    }

    private void setupTime() {
        LocalTime birthTime = editedChild.getBirthTime();
        textViewBirthTime.setText(DateUtils.time(this, birthTime, getString(R.string.time)));
        WidgetsUtils.setupTextView(textViewBirthTime, birthTime != null);
    }

    @OnClick(R.id.imageViewPhoto)
    void onPhotoClick() {
        ImagePickerDialogFragment imagePicker = new ImagePickerDialogFragment();
        imagePicker.showAllowingStateLoss(getSupportFragmentManager(), TAG_DATE_PICKER, editedChild);
    }

    @OnClick(R.id.textViewDateWrapper)
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
        WidgetsUtils.setupDatePicker(this, dpd, getSex());
        dpd.setMaxDate(Calendar.getInstance());
        dpd.show(getFragmentManager(), TAG_DATE_PICKER);
        hideKeyboardAndClearFocus(rootView.findFocus());
    }

    @OnClick(R.id.textViewTimeWrapper)
    void onTimeClick() {
        LocalTime birthTime = editedChild.getBirthTime();
        LocalTime time = birthTime == null ? LocalTime.now() : birthTime;
        TimePickerDialog tpd = TimePickerDialog.newInstance(this,
                time.getHourOfDay(), time.getMinuteOfHour(), DateFormat.is24HourFormat(this));
        tpd.vibrate(false);
        WidgetsUtils.setupTimePicker(this, tpd, getSex());
        tpd.show(getFragmentManager(), TAG_TIME_PICKER);
        hideKeyboardAndClearFocus(rootView.findFocus());
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
        updateChild(editedChild.toBuilder().imageFileName(imageFileName).build());
        setupImage();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        LocalDate birthDate = LocalDate.fromCalendarFields(calendar);
        updateChild(editedChild.toBuilder().birthDate(birthDate).build());
        setupDate();
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        LocalTime birthTime = new LocalTime(hourOfDay, minute);
        updateChild(editedChild.toBuilder().birthTime(birthTime).build());
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
    public void setButtonDoneEnabled(boolean enabled) {
        isButtonDoneEnabled = enabled;
        buttonDone.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), isButtonDoneEnabled));
    }

    @Override
    public void validationFailed() {
        if (!isValidationStarted) {
            isValidationStarted = true;
            unsubscribeOnDestroy(presenter.listenForFieldsUpdate(new ChildObservable(this)));
        }
    }

    @Override
    public void showValidationErrorMessage(String msg) {
        showToast(msg);
    }

    @Override
    public void nameValidated(boolean valid) {
        viewValidated(editTextName, valid,
                R.drawable.name_edit_text_background, R.drawable.name_edit_text_background_error);
        editTextName.setPadding(0, 0, 0, editTextBottomPadding);
    }

    @Override
    public void sexValidated(boolean valid) {
        viewValidated(textViewSex, valid);
    }

    @Override
    public void birthDateValidated(boolean valid) {
        viewValidated(textViewBirthDate, valid);
    }

    @Override
    public void birthHeightValidated(boolean valid) {
        viewValidated(editTextBirthHeight, valid);
    }

    @Override
    public void birthWeightValidated(boolean valid) {
        viewValidated(editTextBirthWeight, valid);
    }

    private void viewValidated(View view, boolean valid) {
        viewValidated(view, valid, R.drawable.edit_text_background, R.drawable.edit_text_background_error);
    }

    private void viewValidated(View view, boolean valid,
                               @DrawableRes int background, @DrawableRes int backgroundError) {
        view.setBackgroundResource(valid ? background : backgroundError);
    }

    @Override
    public void onBackPressed() {
        boolean processed = dismissPopupWindow();
        if (processed) {
            return;
        }

        super.onBackPressed();
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
