package ru.android.childdiary.presentation.medical.filter.medicines;

import android.support.annotation.StringRes;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.presentation.medical.filter.core.MedicalFilterDialogFragment;

public class MedicineTakingFilterDialogFragment
        extends MedicalFilterDialogFragment<MedicineTaking, MedicineTakingFilterDialogArguments> {
    @Override
    @StringRes
    protected int getFilterByItemTextResId() {
        return R.string.filter_by_medicine;
    }

    @Override
    @StringRes
    protected int getAutoCompleteHintResId() {
        return R.string.medicine;
    }
}
