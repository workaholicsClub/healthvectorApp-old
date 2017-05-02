package ru.android.childdiary.presentation.medical.adapters.visits;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.presentation.core.swipe.FabController;
import ru.android.childdiary.presentation.core.swipe.ItemActionListener;
import ru.android.childdiary.presentation.core.swipe.SwipeActionListener;
import ru.android.childdiary.presentation.core.swipe.SwipeListAdapter;
import ru.android.childdiary.utils.ObjectUtils;

public class DoctorVisitAdapter extends SwipeListAdapter<
        DoctorVisitViewHolder,
        DoctorVisit,
        SwipeActionListener<DoctorVisitViewHolder>,
        ItemActionListener<DoctorVisit>> {
    public DoctorVisitAdapter(Context context, @NonNull ItemActionListener<DoctorVisit> itemActionListener, @Nullable FabController fabController) {
        super(context, itemActionListener, fabController);
    }

    @Override
    protected DoctorVisitViewHolder createViewHolder(ViewGroup parent) {
        View v = inflater.inflate(R.layout.doctor_visit_item, parent, false);
        return new DoctorVisitViewHolder(v, itemActionListener, this);
    }

    @Override
    public boolean areItemsTheSame(DoctorVisit oldItem, DoctorVisit newItem) {
        return ObjectUtils.equals(oldItem.getId(), newItem.getId());
    }
}
