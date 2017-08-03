package ru.android.childdiary.presentation.medical.filter.medicines;

import android.content.Context;
import android.util.AttributeSet;

import ru.android.childdiary.domain.interactors.dictionaries.medicines.Medicine;
import ru.android.childdiary.presentation.medical.filter.core.BaseTokenCompleteTextView;

public class MedicineTokenCompleteTextView extends BaseTokenCompleteTextView<Medicine> {
    public MedicineTokenCompleteTextView(Context context) {
        super(context);
    }

    public MedicineTokenCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MedicineTokenCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected Medicine defaultObject(String completionText) {
        return Medicine.NULL;
    }

    @Override
    protected String getTextForValue(Medicine item) {
        return item.getName();
    }
}
