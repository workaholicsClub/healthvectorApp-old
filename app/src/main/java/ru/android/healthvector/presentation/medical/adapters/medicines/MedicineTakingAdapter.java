package ru.android.healthvector.presentation.medical.adapters.medicines;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import ru.android.healthvector.R;
import ru.android.healthvector.domain.medical.data.MedicineTaking;
import ru.android.healthvector.presentation.core.adapters.swipe.FabController;
import ru.android.healthvector.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.healthvector.utils.ObjectUtils;

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
    protected MedicineTakingViewHolder createUserViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.medical_item, parent, false);
        return new MedicineTakingViewHolder(v, sex, itemActionListener, this);
    }

    @Override
    public boolean areItemsTheSame(MedicineTaking oldItem, MedicineTaking newItem) {
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
