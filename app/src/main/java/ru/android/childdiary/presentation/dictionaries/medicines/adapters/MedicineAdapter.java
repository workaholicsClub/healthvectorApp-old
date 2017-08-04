package ru.android.childdiary.presentation.dictionaries.medicines.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.dictionaries.medicines.data.Medicine;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.childdiary.utils.ObjectUtils;

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
    public MedicineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.picker_item, parent, false);
        return new MedicineViewHolder(v, itemActionListener, this);
    }

    @Override
    public boolean areItemsTheSame(Medicine oldItem, Medicine newItem) {
        return ObjectUtils.equals(oldItem.getId(), newItem.getId());
    }
}
