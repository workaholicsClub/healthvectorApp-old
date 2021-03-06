package ru.android.healthvector.presentation.development.partitions.antropometry.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import ru.android.healthvector.R;
import ru.android.healthvector.domain.development.antropometry.data.Antropometry;
import ru.android.healthvector.presentation.core.adapters.swipe.FabController;
import ru.android.healthvector.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.healthvector.utils.ObjectUtils;

public class AntropometryAdapter extends SwipeViewAdapter<Antropometry, AntropometryViewHolder, AntropometrySwipeActionListener, AntropometryActionListener>
        implements AntropometrySwipeActionListener {
    public AntropometryAdapter(Context context, @NonNull AntropometryActionListener itemActionListener, @Nullable FabController fabController) {
        super(context, itemActionListener, fabController);
    }

    @Override
    protected AntropometryViewHolder createUserViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.antropometry_item, parent, false);
        return new AntropometryViewHolder(v, sex, itemActionListener, this);
    }

    @Override
    public boolean areItemsTheSame(Antropometry oldItem, Antropometry newItem) {
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
