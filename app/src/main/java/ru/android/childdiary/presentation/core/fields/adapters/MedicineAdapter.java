package ru.android.childdiary.presentation.core.fields.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.presentation.core.adapters.BaseArrayAdapter;
import ru.android.childdiary.presentation.core.adapters.BaseViewHolder;

public class MedicineAdapter extends BaseArrayAdapter<Medicine, MedicineAdapter.ViewHolder> {
    public MedicineAdapter(Context context, List<Medicine> medicines) {
        super(context, medicines);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.spinner_item;
    }

    @Override
    protected MedicineAdapter.ViewHolder createViewHolder(View view) {
        return new MedicineAdapter.ViewHolder(view);
    }

    static class ViewHolder extends BaseViewHolder<Medicine> {
        @BindView(android.R.id.text1)
        TextView textView;
        @BindView(R.id.imageViewDropdown)
        View imageViewDropdown;
        @BindDimen(R.dimen.base_margin_horizontal)
        int baseMarginHorizontal;

        public ViewHolder(View view) {
            super(view);
        }

        @Override
        public void bind(Context context, int position, Medicine medicine) {
            String text = medicine == Medicine.NULL
                    ? context.getString(R.string.food_measure_other)
                    : medicine.getName();
            textView.setText(text);
            imageViewDropdown.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
            params.rightMargin = position == 0 ? 0 : baseMarginHorizontal;
            textView.setLayoutParams(params);
        }
    }
}
