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

    public ValidationResult validateAll() {
        ValidationResult result = new ValidationResult();
        result.addResult(validateName(result.isValid()));
        result.addResult(validateSex(result.isValid()));
        result.addResult(validateBirthDate(result.isValid()));
        result.addResult(validateBirthHeight(result.isValid()));
        result.addResult(validateBirthWeight(result.isValid()));
        return result;
    }

    public ValidationResult validateName(boolean shouldFocus) {
        ValidationResult result = new ValidationResult();

        String name = activity.editTextName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            result.addMessage(activity.getString(R.string.validate_child_name_empty));
        }

        if (result.isValid()) {
            activity.editTextName.setBackgroundResource(R.drawable.name_edit_text_background);
            int bottom = activity.getResources().getDimensionPixelSize(R.dimen.name_edit_text_padding_bottom);
            activity.editTextName.setPadding(0, 0, 0, bottom);
        } else {
            activity.editTextName.setBackgroundResource(R.drawable.name_edit_text_background_error);
            int bottom = activity.getResources().getDimensionPixelSize(R.dimen.name_edit_text_padding_bottom);
            activity.editTextName.setPadding(0, 0, 0, bottom);
            if (shouldFocus) {
                activity.editTextName.requestFocus();
            }
        }

        return result;
    }

    public ValidationResult validateSex(boolean shouldFocus) {
        ValidationResult result = new ValidationResult();

        Sex sex = activity.spinnerSexAdapter.getSexSpinnerPosition(activity.spinnerSex);
        if (sex == null) {
            result.addMessage(activity.getString(R.string.validate_child_sex_empty));
        }

        if (result.isValid()) {
            activity.spinnerSex.setBackgroundResource(R.drawable.spinner_background);
        } else {
            activity.spinnerSex.setBackgroundResource(R.drawable.spinner_background_error);
        }

        return result;
    }

    public ValidationResult validateBirthDate(boolean shouldFocus) {
        ValidationResult result = new ValidationResult();

        LocalDate birthDate = activity.editedChild.getBirthDate();
        if (birthDate == null) {
            result.addMessage(activity.getString(R.string.validate_child_birth_date_empty));
        }

        if (result.isValid()) {
            activity.textViewDate.setBackgroundResource(R.drawable.spinner_background);
        } else {
            activity.textViewDate.setBackgroundResource(R.drawable.spinner_background_error);
        }

        return result;
    }

    public ValidationResult validateBirthHeight(boolean shouldFocus) {
        ValidationResult result = new ValidationResult();

        String birthHeightString = activity.editTextBirthHeight.getText().toString().trim();
        Double birthHeight = DoubleUtils.parse(birthHeightString);
        if (birthHeight == null) {
            result.addMessage(activity.getString(R.string.validate_child_birth_height_empty));
        }

        if (result.isValid()) {
            activity.editTextBirthHeight.setBackgroundResource(R.drawable.spinner_background);
        } else {
            activity.editTextBirthHeight.setBackgroundResource(R.drawable.spinner_background_error);
            if (shouldFocus) {
                activity.editTextBirthHeight.requestFocus();
            }
        }

        return result;
    }

    public ValidationResult validateBirthWeight(boolean shouldFocus) {
        ValidationResult result = new ValidationResult();

        String birthWeightString = activity.editTextBirthWeight.getText().toString().trim();
        Double birthWeight = DoubleUtils.parse(birthWeightString);
        if (birthWeight == null) {
            result.addMessage(activity.getString(R.string.validate_child_birth_weight_empty));
        }

        if (result.isValid()) {
            activity.editTextBirthWeight.setBackgroundResource(R.drawable.spinner_background);
        } else {
            activity.editTextBirthWeight.setBackgroundResource(R.drawable.spinner_background_error);
            if (shouldFocus) {
                activity.editTextBirthWeight.requestFocus();
            }
        }

        return result;
    }
}
