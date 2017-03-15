package ru.android.childdiary.presentation.events.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.calendar.FoodMeasure;
import ru.android.childdiary.presentation.core.adapters.BaseArrayAdapter;
import ru.android.childdiary.presentation.core.adapters.BaseViewHolder;

public class FoodMeasureAdapter extends BaseArrayAdapter<FoodMeasure, FoodMeasureAdapter.ViewHolder> {
    public FoodMeasureAdapter(Context context, List<FoodMeasure> foodMeasureList) {
        super(context, foodMeasureList);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.spinner_item;
    }

    @Override
    protected FoodMeasureAdapter.ViewHolder createViewHolder(View view) {
        return new FoodMeasureAdapter.ViewHolder(view);
    }

    static class ViewHolder extends BaseViewHolder<FoodMeasure> {
        @BindView(android.R.id.text1)
        TextView textView;
        @BindView(R.id.imageViewDropdown)
        View imageViewDropdown;

        public ViewHolder(View view) {
            super(view);
        }

        @Override
        public void bind(Context context, int position, FoodMeasure foodMeasure) {
            String text = foodMeasure == FoodMeasure.NULL
                    ? context.getString(R.string.food_measure_other)
                    : foodMeasure.getName();
            textView.setText(text);
            imageViewDropdown.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        }
    }
}
