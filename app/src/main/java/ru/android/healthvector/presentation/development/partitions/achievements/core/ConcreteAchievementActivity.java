package ru.android.healthvector.presentation.development.partitions.achievements.core;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokenautocomplete.FilteredArrayAdapter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.LocalDate;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import lombok.AccessLevel;
import lombok.Getter;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.AchievementType;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.achievement.data.ConcreteAchievement;
import ru.android.healthvector.domain.dictionaries.achievements.data.Achievement;
import ru.android.healthvector.presentation.core.BaseMvpActivity;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.core.bindings.RxFieldValueView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldAchievementTypeView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldDateView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldEditTextWithImageAutocompleteView;
import ru.android.healthvector.presentation.core.fields.widgets.FieldNoteWithPhotoView;
import ru.android.healthvector.presentation.core.images.ImagePickerDialogArguments;
import ru.android.healthvector.presentation.core.images.ImagePickerDialogFragment;
import ru.android.healthvector.presentation.core.images.review.ImageReviewActivity;
import ru.android.healthvector.presentation.core.widgets.CustomDatePickerDialog;
import ru.android.healthvector.presentation.development.partitions.achievements.dialogs.AchievementTypeDialogArguments;
import ru.android.healthvector.presentation.development.partitions.achievements.dialogs.AchievementTypeDialogFragment;
import ru.android.healthvector.presentation.medical.adapters.core.StringFilteredAdapter;
import ru.android.healthvector.utils.ObjectUtils;
import ru.android.healthvector.utils.ui.ResourcesUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;

public abstract class ConcreteAchievementActivity<V extends ConcreteAchievementView> extends BaseMvpActivity
        implements ConcreteAchievementView, DatePickerDialog.OnDateSetListener,
        FieldNoteWithPhotoView.PhotoListener, ImagePickerDialogFragment.Listener,
        AchievementTypeDialogFragment.Listener {
    private static final String TAG_DATE_PICKER = "DATE_PICKER";
    private static final String TAG_IMAGE_PICKER = "IMAGE_PICKER";
    private static final String TAG_ACHIEVEMENT_TYPE_PICKER = "ACHIEVEMENT_TYPE_PICKER";

    private static final int REQUEST_IMAGE_REVIEW = 1;

    @BindView(R.id.buttonAdd)
    protected Button buttonAdd;

    @BindView(R.id.achievementNameView)
    protected FieldEditTextWithImageAutocompleteView achievementNameView;

    @BindView(R.id.achievementTypeView)
    protected FieldAchievementTypeView achievementTypeView;

    @BindView(R.id.dateView)
    protected FieldDateView dateView;

    @BindView(R.id.noteWithPhotoView)
    protected FieldNoteWithPhotoView noteWithPhotoView;

    private Child child;
    @Getter(AccessLevel.PROTECTED)
    private ConcreteAchievement item;
    private boolean isValidationStarted;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        child = (Child) getIntent().getSerializableExtra(ExtraConstants.EXTRA_CHILD);
        item = (ConcreteAchievement) getIntent().getSerializableExtra(ExtraConstants.EXTRA_ITEM);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        achievementTypeView.setFieldDialogListener(view -> {
            hideKeyboardAndClearFocus(achievementNameView);
            AchievementTypeDialogFragment dialogFragment = new AchievementTypeDialogFragment();
            dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_ACHIEVEMENT_TYPE_PICKER,
                    AchievementTypeDialogArguments.builder()
                            .sex(getSex())
                            .achievementType(achievementTypeView.getValue() == null
                                    ? AchievementType.HEARING_AND_VISION
                                    : achievementTypeView.getValue())
                            .build());
        });
        setupEditTextView(achievementNameView);
        setupEditTextView(noteWithPhotoView);
        dateView.setFieldDialogListener(v -> showDatePicker(TAG_DATE_PICKER, dateView.getValue(),
                child.getBirthDate(), null));

        noteWithPhotoView.setPhotoListener(this);

        if (savedInstanceState == null) {
            getPresenter().init(child);
            showConcreteAchievement(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_REVIEW) {
            if (resultCode == RESULT_OK) {
                noteWithPhotoView.setImageFileName(null);
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ConcreteAchievement concreteAchievement = (ConcreteAchievement) savedInstanceState.getSerializable(ExtraConstants.EXTRA_ITEM);
        if (concreteAchievement != null) {
            showConcreteAchievement(concreteAchievement);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ExtraConstants.EXTRA_ITEM, buildConcreteAchievement());
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        setupToolbarLogo(ResourcesUtils.getChildIconForToolbar(this, child));
        setupToolbarTitle(child.getName());
        noteWithPhotoView.setSex(getSex());
    }

    @Override
    protected void setContentViewBeforeBind() {
        LayoutInflater inflater = LayoutInflater.from(this);
        ViewGroup detailsView = ButterKnife.findById(this, R.id.detailsView);
        inflater.inflate(R.layout.activity_concrete_achievement, detailsView);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof DatePickerDialog) {
            ((DatePickerDialog) fragment).setOnDateSetListener(this);
        }
    }

    protected void showDatePicker(String tag, @Nullable LocalDate date,
                                  @Nullable LocalDate minDate, @Nullable LocalDate maxDate) {
        DatePickerDialog dpd = CustomDatePickerDialog.create(this, this, date, getSex(),
                minDate, maxDate);
        dpd.show(getFragmentManager(), tag);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        LocalDate date = LocalDate.fromCalendarFields(calendar);
        dateView.setValue(date);
    }

    @Override
    public void onAchievementTypeSet(@NonNull AchievementType achievementType) {
        achievementTypeView.setValue(achievementType);
    }

    protected abstract ConcreteAchievementPresenter<V> getPresenter();

    protected void showConcreteAchievement(@NonNull ConcreteAchievement concreteAchievement) {
        achievementNameView.setText(concreteAchievement.getName());
        achievementTypeView.setValue(concreteAchievement.getAchievementType());
        dateView.setValue(concreteAchievement.getDate());
        noteWithPhotoView.setText(concreteAchievement.getNote());
        noteWithPhotoView.setImageFileName(concreteAchievement.getImageFileName());
    }

    protected final ConcreteAchievement buildConcreteAchievement() {
        return buildConcreteAchievement(item.toBuilder());
    }

    private ConcreteAchievement buildConcreteAchievement(@NonNull ConcreteAchievement.ConcreteAchievementBuilder builder) {
        if (ObjectUtils.isFalse(builder.build().getIsPredefined())) {
            builder.nameUser(achievementNameView.getText());
        }
        builder
                .achievementType(achievementTypeView.getValue())
                .date(dateView.getValue())
                .note(noteWithPhotoView.getText())
                .imageFileName(noteWithPhotoView.getImageFileName());
        return builder.build();
    }

    @Override
    public void showAchievements(@NonNull List<Achievement> achievements) {
        FilteredArrayAdapter<String> adapter = new StringFilteredAdapter(this, Observable.fromIterable(achievements)
                .filter(item -> !TextUtils.isEmpty(item.getName()))
                .map(Achievement::getName)
                .toList()
                .blockingGet(), StringFilteredAdapter.FilterType.CONTAINS);
        achievementNameView.setFilteredAdapter(adapter);
    }

    @Override
    public final void validationFailed() {
        if (!isValidationStarted) {
            isValidationStarted = true;
            unsubscribeOnDestroy(getPresenter().listenForFieldsUpdate(
                    achievementNameView.textObservable(),
                    RxFieldValueView.valueChangeEvents(achievementTypeView)
            ));
        }
    }

    @Override
    public void showValidationErrorMessage(String msg) {
        showToast(msg);
    }

    @Override
    public void achievementNameValidated(boolean valid) {
        achievementNameView.validated(valid);
    }

    @Override
    public void requestPhotoAdd() {
        ImagePickerDialogFragment dialogFragment = new ImagePickerDialogFragment();
        dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_IMAGE_PICKER,
                ImagePickerDialogArguments.builder()
                        .sex(getSex())
                        .showDeleteItem(false)
                        .showCircleFrame(false)
                        .build());
    }

    @Override
    public void requestPhotoReview() {
        Intent intent = ImageReviewActivity.getIntent(this, noteWithPhotoView.getImageFileName(), getSex(), false);
        startActivityForResult(intent, REQUEST_IMAGE_REVIEW);
    }

    @Override
    public void requestPhotoDelete() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.delete_photo_confirmation_dialog_title)
                .setPositiveButton(R.string.delete,
                        (dialog, which) -> noteWithPhotoView.setImageFileName(null))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onSetImage(@Nullable String relativeFileName) {
        noteWithPhotoView.setImageFileName(relativeFileName);
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
        ConcreteAchievement newConcreteAchievement = buildConcreteAchievement();
        if (ObjectUtils.contentEquals(newConcreteAchievement, item)) {
            finish();
            return;
        }
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.save_changes_dialog_title)
                .setPositiveButton(R.string.save,
                        (dialog, which) -> upsert(newConcreteAchievement))
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> finish())
                .show();
    }

    protected abstract void upsert(@NonNull ConcreteAchievement concreteAchievement);
}
