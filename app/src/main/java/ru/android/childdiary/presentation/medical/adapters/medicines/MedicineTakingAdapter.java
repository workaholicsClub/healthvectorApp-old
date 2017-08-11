package ru.android.childdiary.presentation.medical.adapters.medicines;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.data.MedicineTaking;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.childdiary.utils.ObjectUtils;

public class MedicineTakingAdapter extends SwipeViewAdapter<
        MedicineTaking,
        MedicineTakingViewHolder,
        MedicineTakingSwipeActionListener,
        MedicineTakingActionListener> implements MedicineTakingSwipeActionListener {
    public MedicineTakingAdapter(Context context,
                                 @NonNull MedicineTakingActionListener itemActionListener,
                                 @Nullable FabController fabController) {
        super(context, itemActionListener, fabController);
    }

    @Override
    protected MedicineTakingViewHolder createViewHolder(ViewGroup parent) {
        View v = inflater.inflate(R.layout.medical_item, parent, false);
        return new MedicineTakingViewHolder(v, itemActionListener, this);
    }

    @Override
    public boolean areItemsTheSame(MedicineTaking oldItem, MedicineTaking newItem) {
        return ObjectUtils.equals(oldItem.getId(), newItem.getId());
    }
}
