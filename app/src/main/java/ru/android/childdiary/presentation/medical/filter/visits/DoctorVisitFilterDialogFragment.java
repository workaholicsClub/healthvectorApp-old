package ru.android.childdiary.presentation.medical.filter.visits;

import android.support.annotation.StringRes;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.presentation.medical.filter.core.MedicalFilterDialogFragment;

public class DoctorVisitFilterDialogFragment
        extends MedicalFilterDialogFragment<DoctorVisit, DoctorVisitFilterDialogArguments> {
    @Override
    @StringRes
    protected int getFilterByItemTextResId() {
        return R.string.filter_by_doctor;
    }

    @Override
    @StringRes
    protected int getAutoCompleteHintResId() {
        return R.string.doctor;
    }
}
