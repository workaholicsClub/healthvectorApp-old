package ru.android.childdiary.presentation.medical.adapters.medicines;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.childdiary.utils.ObjectUtils;

public class MedicineChipsAdapter extends BaseRecyclerViewAdapter<Medicine, MedicineChipsAdapter.MedicineChipsViewHolder> {
    public MedicineChipsAdapter(Context context) {
        super(context);
    }

    @Override
    protected MedicineChipsViewHolder createViewHolder(ViewGroup parent) {
        View v = inflater.inflate(R.layout.chips_item, parent, false);
        return new MedicineChipsViewHolder(v);
    }

    @Override
    public boolean areItemsTheSame(Medicine oldItem, Medicine newItem) {
        return ObjectUtils.equals(oldItem.getId(), newItem.getId());
    }

    static class MedicineChipsViewHolder extends BaseRecyclerViewHolder<Medicine> {
        @BindView(R.id.textView)
        TextView textView;

        public MedicineChipsViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(Context context, Sex sex, Medicine item) {
            super.bind(context, sex, item);
            textView.setText(item.getName());
        }
    }
}
