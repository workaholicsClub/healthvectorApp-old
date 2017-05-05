package ru.android.childdiary.presentation.medical.pickers.adapters.doctors;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.childdiary.utils.ObjectUtils;

public class DoctorAdapter extends BaseRecyclerViewAdapter<Doctor, DoctorViewHolder> {
    public DoctorAdapter(Context context) {
        super(context);
    }

    @Override
    public boolean areItemsTheSame(Doctor oldItem, Doctor newItem) {
        return ObjectUtils.equals(oldItem.getId(), newItem.getId());
    }

    @Override
    protected DoctorViewHolder createViewHolder(ViewGroup parent) {
        View v = inflater.inflate(R.layout.picker_item, parent, false);
        return new DoctorViewHolder(v);
    }
}
