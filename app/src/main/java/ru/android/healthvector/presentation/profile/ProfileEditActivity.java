package ru.android.healthvector.presentation.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
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
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import icepick.State;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.presentation.core.BaseMvpActivity;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.core.images.ImagePickerDialogArguments;
import ru.android.healthvector.presentation.core.images.ImagePickerDialogFragment;
import ru.android.healthvector.presentation.core.widgets.CustomEditText;
import ru.android.healthvector.presentation.core.widgets.RegExpInputFilter;
import ru.android.healthvector.presentation.profile.adapters.SexAdapter;
import ru.android.healthvector.presentation.profile.dialogs.DatePickerDialogArguments;
import ru.android.healthvector.presentation.profile.dialogs.DatePickerDialogFragment;
import ru.android.healthvector.presentation.profile.dialogs.TimePickerDialogArguments;
import ru.android.healthvector.presentation.profile.dialogs.TimePickerDialogFragment;
import ru.android.healthvector.utils.ObjectUtils;
import ru.android.healthvector.utils.strings.DateUtils;
import ru.android.healthvector.utils.strings.DoubleUtils;
import ru.android.healthvector.utils.strings.StringUtils;
import ru.android.healthvector.utils.ui.ResourcesUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;
import ru.android.healthvector.utils.ui.WidgetsUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ProfileEditActivity extends BaseMvpActivity implements ProfileEditView,
        DatePickerDialogFragment.Listener, TimePickerDialogFragment.Listener, ImagePickerDialogFragment.Listener,
        AdapterView.OnItemClickListener, PopupWindow.OnDismissListener {
    private static final String TAG_TIME_PICKER = "TIME_PICKER";
    private static final String TAG_DATE_PICKER = "DATE_PICKER";
    private static final String TAG_IMAGE_PICKER = "IMAGE_PICKER";

    private final List<OnUpdateChildListener> onUpdateChildListeners = new ArrayList<>();

    @InjectPresenter
    ProfileEditPresenter presenter;

    @BindView(R.id.topPanel)
    View topPanel;

    @BindView(R.id.buttonAdd)
    Button buttonAdd;

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

    @BindDimen(R.dimen.name_edit_text_padding_bottom)
    int editTextBottomPadding;

    @State
    boolean isButtonDoneEnabled;

    @State
    Child editedChild = Child.NULL;

    private Child child;
    private boolean isValidationStarted;

    private ListPopupWindow popupWindow;

    public static Intent getIntent(Context context, @Nullable Child child) {
        return new Intent(context, ProfileEditActivity.class)
                .putExtra(ExtraConstants.EXTRA_CHILD, child);
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
        child = (Child) getIntent().getSerializableExtra(ExtraConstants.EXTRA_CHILD);
        if (savedInstanceState == null && child != null) {
            editedChild = child.toBuilder().build();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        buttonAdd.setVisibility(child == null ? VISIBLE : GONE);
        buttonAdd.setOnClickListener(v -> presenter.addChild(editedChild));

        setupTextViews();
        setupSex();
        setupImage();
        setupDate();
        setupTime();
        setupTracker();

        trackScreen("PROFILE");
        unsubscribeOnDestroy(presenter.listenForDoneButtonUpdate(new ChildObservable(this)));
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(child == null ? R.string.add_profile : R.string.profile);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        topPanel.setBackgroundResource(ThemeUtils.getColorPrimaryRes(getSex()));
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), isButtonDoneEnabled));
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideKeyboardAndClearFocus();
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
        hideKeyboardAndClearFocus();
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
        Drawable photo = ResourcesUtils.getChildIconForProfile(this, editedChild);
        imageViewPhoto.setImageDrawable(photo);
        textViewPhoto.setVisibility(TextUtils.isEmpty(editedChild.getImageFileName()) ? VISIBLE : GONE);
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
        ImagePickerDialogFragment dialogFragment = new ImagePickerDialogFragment();
        dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_IMAGE_PICKER,
                ImagePickerDialogArguments.builder()
                        .sex(getSex())
                        .showDeleteItem(!TextUtils.isEmpty(editedChild.getImageFileName()))
                        .showCircleFrame(true)
                        .build());
    }

    @OnClick(R.id.textViewDateWrapper)
    void onDateClick() {
        LocalDate birthDate = editedChild.getBirthDate();
        DatePickerDialogFragment dialogFragment = new DatePickerDialogFragment();
        dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_DATE_PICKER,
                DatePickerDialogArguments.builder()
                        .sex(getSex())
                        .title(getString(R.string.birth_date))
                        .date(birthDate)
                        .minDate(null)
                        .maxDate(LocalDate.now())
                        .build());
        hideKeyboardAndClearFocus();
    }

    @OnClick(R.id.textViewTimeWrapper)
    void onTimeClick() {
        LocalTime birthTime = editedChild.getBirthTime();
        TimePickerDialogFragment dialogFragment = new TimePickerDialogFragment();
        dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_TIME_PICKER,
                TimePickerDialogArguments.builder()
                        .sex(getSex())
                        .title(getString(R.string.birth_time))
                        .time(birthTime)
                        .build());
        hideKeyboardAndClearFocus();
    }

    @Override
    public void onSetImage(@Nullable String relativeFileName) {
        updateChild(editedChild.toBuilder().imageFileName(relativeFileName).build());
        setupImage();
    }

    @Override
    public void onDatePick(String tag, @NonNull LocalDate date) {
        updateChild(editedChild.toBuilder().birthDate(date).build());
        setupDate();
    }

    @Override
    public void onTimePick(String tag, @NonNull LocalTime time) {
        updateChild(editedChild.toBuilder().birthTime(time).build());
        setupTime();
    }

    @Override
    public void childAdded(@NonNull Child child) {
        setResult(RESULT_OK, new Intent().putExtra(ExtraConstants.EXTRA_CHILD, child));
        finish();
    }

    @Override
    public void childUpdated(@NonNull Child child) {
        finish();
    }

    @Override
    public void setButtonDoneEnabled(boolean enabled) {
        isButtonDoneEnabled = enabled;
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), isButtonDoneEnabled));
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

        saveChangesOrExit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        removeToolbarMargin();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.close, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_close:
                saveChangesOrExit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveChangesOrExit() {
        if (ObjectUtils.isEmpty(editedChild)) {
            finish();
            return;
        }
        if (child != null && ObjectUtils.contentEquals(editedChild, child)) {
            finish();
            return;
        }
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.save_changes_dialog_title)
                .setPositiveButton(R.string.save,
                        (dialog, which) -> {
                            if (child == null) {
                                presenter.addChild(editedChild);
                            } else {
                                presenter.updateChild(editedChild);
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> finish())
                .show();
    }
}
