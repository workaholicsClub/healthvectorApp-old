package ru.android.childdiary.domain.interactors.child;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.core.Validator;

@Singleton
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
                if (child.getBirthHeight() == null) {
                    result.addMessage(context.getString(R.string.validate_child_birth_height_empty));
                }
                break;
            case BIRTH_WEIGHT:
                if (child.getBirthWeight() == null) {
                    result.addMessage(context.getString(R.string.validate_child_birth_weight_empty));
                }
                break;

        }
    }
}
