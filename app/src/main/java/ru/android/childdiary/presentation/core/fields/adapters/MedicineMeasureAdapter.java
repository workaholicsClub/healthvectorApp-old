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
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;
import ru.android.childdiary.presentation.core.adapters.BaseArrayAdapter;
import ru.android.childdiary.presentation.core.adapters.BaseViewHolder;

public class MedicineMeasureAdapter extends BaseArrayAdapter<MedicineMeasure, MedicineMeasureAdapter.ViewHolder> {
    public MedicineMeasureAdapter(Context context, List<MedicineMeasure> medicineMeasureList) {
        super(context, medicineMeasureList);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.spinner_item;
    }

    @Override
    protected MedicineMeasureAdapter.ViewHolder createViewHolder(View view) {
        return new MedicineMeasureAdapter.ViewHolder(view);
    }

    static class ViewHolder extends BaseViewHolder<MedicineMeasure> {
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
        public void bind(Context context, int position, MedicineMeasure medicineMeasure) {
            String text = medicineMeasure == MedicineMeasure.NULL
                    ? context.getString(R.string.food_measure_other)
                    : medicineMeasure.getName();
            textView.setText(text);
            imageViewDropdown.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
            params.rightMargin = position == 0 ? 0 : baseMarginHorizontal;
            textView.setLayoutParams(params);
        }
    }
}
