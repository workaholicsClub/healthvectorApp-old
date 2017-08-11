package ru.android.childdiary.presentation.dictionaries.medicinemeasure.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.dictionaries.medicinemeasure.data.MedicineMeasure;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.childdiary.utils.ObjectUtils;

public class MedicineMeasureAdapter extends SwipeViewAdapter<
        MedicineMeasure,
        MedicineMeasureViewHolder,
        MedicineMeasureSwipeActionListener,
        MedicineMeasureActionListener> implements MedicineMeasureSwipeActionListener {
    public MedicineMeasureAdapter(Context context,
                                  @NonNull MedicineMeasureActionListener itemActionListener,
                                  @Nullable FabController fabController) {
        super(context, itemActionListener, fabController);
    }

    @Override
    protected MedicineMeasureViewHolder createUserViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.picker_item, parent, false);
        return new MedicineMeasureViewHolder(v, itemActionListener, this);
    }

    @Override
    public boolean areItemsTheSame(MedicineMeasure oldItem, MedicineMeasure newItem) {
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
