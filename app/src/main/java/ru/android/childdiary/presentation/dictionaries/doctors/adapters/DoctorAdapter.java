package ru.android.childdiary.presentation.dictionaries.doctors.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.dictionaries.doctors.data.Doctor;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.childdiary.utils.ObjectUtils;

public class DoctorAdapter extends SwipeViewAdapter<
        Doctor,
        DoctorViewHolder,
        DoctorSwipeActionListener,
        DoctorActionListener> implements DoctorSwipeActionListener {
    public DoctorAdapter(Context context,
                         @NonNull DoctorActionListener itemActionListener,
                         @Nullable FabController fabController) {
        super(context, itemActionListener, fabController);
    }

    @Override
    protected DoctorViewHolder createUserViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.picker_item, parent, false);
        return new DoctorViewHolder(v, sex, itemActionListener, this);
    }

    @Override
    public boolean areItemsTheSame(Doctor oldItem, Doctor newItem) {
        return ObjectUtils.equals(oldItem.getId(), newItem.getId());
    }

    @Override
    public boolean paintDividers() {
        return true;
    }

    @Override
    public boolean useFooter() {
        return true;
    }
}
