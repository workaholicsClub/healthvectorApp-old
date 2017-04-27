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
import ru.android.childdiary.domain.interactors.calendar.events.core.Food;
import ru.android.childdiary.presentation.core.adapters.BaseArrayAdapter;
import ru.android.childdiary.presentation.core.adapters.BaseViewHolder;

public class FoodAdapter extends BaseArrayAdapter<Food, FoodAdapter.ViewHolder> {
    public FoodAdapter(Context context, List<Food> foodList) {
        super(context, foodList);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.spinner_item;
    }

    @Override
    protected FoodAdapter.ViewHolder createViewHolder(View view) {
        return new FoodAdapter.ViewHolder(view);
    }

    static class ViewHolder extends BaseViewHolder<Food> {
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
        public void bind(Context context, int position, Food food) {
            String text = food == Food.NULL
                    ? context.getString(R.string.food_other)
                    : food.getName();
            textView.setText(text);
            imageViewDropdown.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
            params.rightMargin = position == 0 ? 0 : baseMarginHorizontal;
            textView.setLayoutParams(params);
        }
    }
}
