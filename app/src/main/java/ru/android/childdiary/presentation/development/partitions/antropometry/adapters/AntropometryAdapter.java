package ru.android.childdiary.presentation.development.partitions.antropometry.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.development.antropometry.data.Antropometry;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.childdiary.utils.ObjectUtils;

public class AntropometryAdapter extends SwipeViewAdapter<Antropometry, AntropometryViewHolder, AntropometrySwipeActionListener, AntropometryActionListener>
        implements AntropometrySwipeActionListener {
    public AntropometryAdapter(Context context, @NonNull AntropometryActionListener itemActionListener, @Nullable FabController fabController) {
        super(context, itemActionListener, fabController);
    }

    @Override
    protected AntropometryViewHolder createViewHolder(ViewGroup parent) {
        View v = inflater.inflate(R.layout.antropometry_item, parent, false);
        return new AntropometryViewHolder(v, itemActionListener, this);
    }

    @Override
    public boolean areItemsTheSame(Antropometry oldItem, Antropometry newItem) {
        return ObjectUtils.equals(oldItem.getId(), newItem.getId());
    }
}
