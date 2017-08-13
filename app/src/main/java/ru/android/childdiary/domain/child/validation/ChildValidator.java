package ru.android.childdiary.domain.child.validation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.core.validation.core.ValidationException;
import ru.android.childdiary.domain.core.validation.core.Validator;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.utils.ObjectUtils;

public class ChildValidator extends Validator<Child, ChildValidationResult> {
    private final Context context;

    @Inject
    public ChildValidator(Context context) {
        this.context = context;
    }

    @Override
    public List<ChildValidationResult> validate(@NonNull Child child) {
        List<ChildValidationResult> results = new ArrayList<>();

        for (ChildFieldType field : ChildFieldType.values()) {
            ChildValidationResult result = new ChildValidationResult(field);
            validateField(result, child);
            results.add(result);
        }

        return results;
    }

    private void validateField(@NonNull ChildValidationResult result, @NonNull Child child) {
        if (result.getFieldType() == null) {
            return;
        }
        switch (result.getFieldType()) {
            case IMAGE:
                // необязательное поле
                break;
            case NAME:
                if (TextUtils.isEmpty(child.getName())) {
                    result.addMessage(context.getString(R.string.validate_child_name_empty));
                }
                break;
            case SEX:
                if (child.getSex() == null) {
                    result.addMessage(context.getString(R.string.validate_child_sex_empty));
                }
                break;
            case BIRTH_DATE:
                if (child.getBirthDate() == null) {
                    result.addMessage(context.getString(R.string.validate_child_birth_date_empty));
                }
                break;
            case BIRTH_TIME:
                // необязательное поле
                break;
            case BIRTH_HEIGHT:
                if (!ObjectUtils.isPositive(child.getBirthHeight())) {
                    result.addMessage(context.getString(R.string.validate_child_birth_height_empty));
                }
                break;
            case BIRTH_WEIGHT:
                if (!ObjectUtils.isPositive(child.getBirthWeight())) {
                    result.addMessage(context.getString(R.string.validate_child_birth_weight_empty));
                }
                break;
        }
    }

    @Override
    protected ValidationException createException(@NonNull List<ChildValidationResult> results) {
        return new ChildValidationException(results);
    }
}
