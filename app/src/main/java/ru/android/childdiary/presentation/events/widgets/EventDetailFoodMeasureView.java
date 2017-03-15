package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListAdapter;

import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.calendar.FoodMeasure;
import ru.android.childdiary.presentation.events.adapters.FoodMeasureAdapter;

public class EventDetailFoodMeasureView extends EventDetailSpinnerView<FoodMeasure> {
    private final FoodMeasureAdapter adapter = new FoodMeasureAdapter(getContext());

    public EventDetailFoodMeasureView(Context context) {
        super(context);
    }

    public EventDetailFoodMeasureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventDetailFoodMeasureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.event_detail_food_measure;
    }

    @Override
    protected String getTextForValue(FoodMeasure value) {
        return value.getName();
    }

    @Override
    protected ListAdapter getAdapter() {
        return adapter;
    }

    public void updateAdapter(List<FoodMeasure> items) {
        adapter.setItems(items);
    }
}
