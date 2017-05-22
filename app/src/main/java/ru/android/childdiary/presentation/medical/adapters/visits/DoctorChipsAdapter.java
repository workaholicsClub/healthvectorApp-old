package ru.android.childdiary.presentation.medical.adapters.visits;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.childdiary.utils.ObjectUtils;

public class DoctorChipsAdapter extends BaseRecyclerViewAdapter<Doctor, DoctorChipsAdapter.DoctorChipsViewHolder> {
    public DoctorChipsAdapter(Context context) {
        super(context);
    }

    @Override
    protected DoctorChipsViewHolder createViewHolder(ViewGroup parent) {
        View v = inflater.inflate(R.layout.chips_item, parent, false);
        return new DoctorChipsViewHolder(v);
    }

    @Override
    public boolean areItemsTheSame(Doctor oldItem, Doctor newItem) {
        return ObjectUtils.equals(oldItem.getId(), newItem.getId());
    }

    static class DoctorChipsViewHolder extends BaseRecyclerViewHolder<Doctor> {
        @BindView(R.id.textView)
        TextView textView;

        public DoctorChipsViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(Context context, Sex sex, Doctor item) {
            super.bind(context, sex, item);
            textView.setText(item.getName());
        }
    }
}
