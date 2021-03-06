package ru.android.healthvector.presentation.medical.adapters.visits;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import ru.android.healthvector.R;
import ru.android.healthvector.domain.medical.data.DoctorVisit;
import ru.android.healthvector.presentation.core.adapters.swipe.FabController;
import ru.android.healthvector.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.healthvector.utils.ObjectUtils;

public class DoctorVisitAdapter extends SwipeViewAdapter<
        DoctorVisit,
        DoctorVisitViewHolder,
        DoctorVisitSwipeActionListener,
        DoctorVisitActionListener> implements DoctorVisitSwipeActionListener {
    public DoctorVisitAdapter(Context context,
                              @NonNull DoctorVisitActionListener itemActionListener,
                              @Nullable FabController fabController) {
        super(context, itemActionListener, fabController);
    }

    @Override
    protected DoctorVisitViewHolder createUserViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.medical_item, parent, false);
        return new DoctorVisitViewHolder(v, sex, itemActionListener, this);
    }

    @Override
    public boolean areItemsTheSame(DoctorVisit oldItem, DoctorVisit newItem) {
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
