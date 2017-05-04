package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;

public class FieldMedicineView extends FieldDialogView<Medicine> {
    public FieldMedicineView(Context context) {
        super(context);
    }

    public FieldMedicineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldMedicineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_medicine;
    }

    @Override
    protected String getTextForValue(@Nullable Medicine value) {
        return value == null ? null : value.getName();
    }
}
