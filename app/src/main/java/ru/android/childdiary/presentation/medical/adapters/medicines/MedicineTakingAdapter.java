package ru.android.childdiary.presentation.medical.adapters.medicines;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.presentation.core.swipe.FabController;
import ru.android.childdiary.presentation.core.swipe.ItemActionListener;
import ru.android.childdiary.presentation.core.swipe.SwipeActionListener;
import ru.android.childdiary.presentation.core.swipe.SwipeListAdapter;
import ru.android.childdiary.utils.ObjectUtils;

public class MedicineTakingAdapter extends SwipeListAdapter<
        MedicineTakingViewHolder,
        MedicineTaking,
        SwipeActionListener<MedicineTakingViewHolder>,
        ItemActionListener<MedicineTaking>> {
    public MedicineTakingAdapter(Context context, @NonNull ItemActionListener<MedicineTaking> itemActionListener, @Nullable FabController fabController) {
        super(context, itemActionListener, fabController);
    }

    @Override
    protected MedicineTakingViewHolder createViewHolder(ViewGroup parent, ItemActionListener<MedicineTaking> itemActionListener) {
        View v = inflater.inflate(R.layout.event_item, parent, false);
        return new MedicineTakingViewHolder(v, itemActionListener, this);
    }

    @Override
    public boolean areItemsTheSame(MedicineTaking oldItem, MedicineTaking newItem) {
        return ObjectUtils.equals(oldItem.getId(), newItem.getId());
    }
}
