package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.calendar.Food;
import ru.android.childdiary.presentation.events.adapters.FoodAdapter;

public class EventDetailFoodView extends EventDetailSpinnerView<Food> {
    private final FoodAdapter adapter = new FoodAdapter(getContext(), Collections.singletonList(Food.NULL));

    public EventDetailFoodView(Context context) {
        super(context);
    }

    public EventDetailFoodView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventDetailFoodView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.event_detail_food;
    }

    @Override
    protected String getTextForValue(Food value) {
        return value.getName();
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
