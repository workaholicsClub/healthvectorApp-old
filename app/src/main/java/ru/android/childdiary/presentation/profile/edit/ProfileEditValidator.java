package ru.android.childdiary.presentation.profile.edit;

import android.text.TextUtils;

import org.joda.time.LocalDate;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.core.ValidationResult;
import ru.android.childdiary.utils.DoubleUtils;

class ProfileEditValidator {
    private ProfileEditActivity activity;

    public ProfileEditValidator(ProfileEditActivity activity) {
        this.activity = activity;
    }

    public ValidationResult validate() {
        ValidationResult result = new ValidationResult();
        validateName(result);
        validateSex(result);
        validateBirthDate(result);
        validateBirthHeight(result);
        validateBirthWeight(result);
        return result;
    }

    private void validateName(ValidationResult result) {
        boolean shouldFocus = result.isValid();
        boolean valid = true;

        String name = activity.editTextChildName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            valid = false;
            result.failWithMessage(activity.getString(R.string.validate_child_name_empty));
        }

        if (!valid) {
            activity.editTextChildName.setBackgroundResource(R.drawable.name_edit_text_background_error);
            int bottom = activity.getResources().getDimensionPixelSize(R.dimen.name_edit_text_padding_bottom);
            activity.editTextChildName.setPadding(0, 0, 0, bottom);
            if (shouldFocus) {
                activity.editTextChildName.requestFocus();
            }
        }
    }

    private void validateSex(ValidationResult result) {
        boolean valid = true;

        Sex sex = activity.spinnerSexAdapter.getSexSpinnerPosition(activity.spinnerSex);
        if (sex == null) {
            valid = false;
            result.failWithMessage(activity.getString(R.string.validate_child_sex_empty));
        }

        if (!valid) {
            activity.spinnerSex.setBackgroundResource(R.drawable.spinner_background_error);
        }
    }

    private void validateBirthDate(ValidationResult result) {
        boolean valid = true;

        LocalDate birthDate = activity.editedChild.getBirthDate();
        if (birthDate == null) {
            valid = false;
            result.failWithMessage(activity.getString(R.string.validate_child_birth_date_empty));
        }

        if (!valid) {
            activity.textViewDate.setBackgroundResource(R.drawable.spinner_background_error);
        }
    }

    private void validateBirthHeight(ValidationResult result) {
        boolean shouldFocus = result.isValid();
        boolean valid = true;

        String birthHeightString = activity.editTextBirthHeight.getText().toString().trim();
        Double birthHeight = DoubleUtils.parse(birthHeightString);
        if (birthHeight == null) {
            valid = false;
            result.failWithMessage(activity.getString(R.string.validate_child_birth_height_empty));
        }

        if (!valid) {
            activity.editTextBirthHeight.setBackgroundResource(R.drawable.spinner_background_error);
            if (shouldFocus) {
                activity.editTextBirthHeight.requestFocus();
            }
        }
    }

    private void validateBirthWeight(ValidationResult result) {
        boolean shouldFocus = result.isValid();
        boolean valid = true;

        String birthWeightString = activity.editTextBirthWeight.getText().toString().trim();
        Double birthWeight = DoubleUtils.parse(birthWeightString);
        if (birthWeight == null) {
            valid = false;
            result.failWithMessage(activity.getString(R.string.validate_child_birth_weight_empty));
        }

        if (!valid) {
            activity.editTextBirthWeight.setBackgroundResource(R.drawable.spinner_background_error);
            if (shouldFocus) {
                activity.editTextBirthWeight.requestFocus();
            }
        }
    }
}
