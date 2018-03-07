package ru.android.healthvector.presentation.dictionaries.medicines.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import ru.android.healthvector.R;
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;
import ru.android.healthvector.presentation.core.adapters.swipe.FabController;
import ru.android.healthvector.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.healthvector.utils.ObjectUtils;

public class MedicineAdapter extends SwipeViewAdapter<
        Medicine,
        MedicineViewHolder,
        MedicineSwipeActionListener,
        MedicineActionListener> implements MedicineSwipeActionListener {
    public MedicineAdapter(Context context,
                           @NonNull MedicineActionListener itemActionListener,
                           @Nullable FabController fabController) {
        super(context, itemActionListener, fabController);
    }

    @Override
    protected MedicineViewHolder createUserViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.picker_item, parent, false);
        return new MedicineViewHolder(v, sex, itemActionListener, this);
    }

    @Override
    public boolean areItemsTheSame(Medicine oldItem, Medicine newItem) {
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
