package ru.android.childdiary.presentation.medical.pickers.adapters.medicines;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.childdiary.utils.ObjectUtils;

public class MedicineAdapter extends BaseRecyclerViewAdapter<Medicine, MedicineViewHolder> {
    public MedicineAdapter(Context context) {
        super(context);
    }

    @Override
    public boolean areItemsTheSame(Medicine oldItem, Medicine newItem) {
        return ObjectUtils.equals(oldItem.getId(), newItem.getId());
    }

    @Override
    protected MedicineViewHolder createViewHolder(ViewGroup parent) {
        View v = inflater.inflate(R.layout.picker_item, parent, false);
        return new MedicineViewHolder(v);
    }
}
