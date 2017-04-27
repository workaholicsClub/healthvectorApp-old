package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.calendar.events.core.Food;
import ru.android.childdiary.presentation.core.fields.adapters.FoodAdapter;

public class FieldFoodView extends FieldSpinnerView<Food> {
    private final FoodAdapter adapter = new FoodAdapter(getContext(), Collections.singletonList(Food.NULL));

    public FieldFoodView(Context context) {
        super(context);
    }

    public FieldFoodView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldFoodView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_food;
    }

    @Override
    protected String getTextForValue(@Nullable Food value) {
        return value == null ? null : value.getName();
    }

    @Override
    protected ListAdapter getAdapter() {
        return adapter;
    }

    public void updateAdapter(List<Food> items) {
        items = new ArrayList<>(items);
        items.add(Food.NULL);
        adapter.setItems(items);
    }
}
