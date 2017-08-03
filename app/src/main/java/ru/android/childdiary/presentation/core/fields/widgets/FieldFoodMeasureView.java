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
import ru.android.childdiary.domain.interactors.dictionaries.foodmeasure.FoodMeasure;
import ru.android.childdiary.presentation.core.fields.adapters.FoodMeasureAdapter;

public class FieldFoodMeasureView extends FieldSpinnerView<FoodMeasure> {
    private final FoodMeasureAdapter adapter = new FoodMeasureAdapter(getContext(), Collections.singletonList(FoodMeasure.NULL));

    public FieldFoodMeasureView(Context context) {
        super(context);
    }

    public FieldFoodMeasureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldFoodMeasureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_food_measure;
    }

    @Nullable
    @Override
    protected String getTextForValue(@Nullable FoodMeasure value) {
        return value == null ? null : value.getName();
    }

    @Override
    protected ListAdapter getAdapter() {
        return adapter;
    }

    public void updateAdapter(List<FoodMeasure> items) {
        items = new ArrayList<>(items);
        items.add(FoodMeasure.NULL);
        adapter.setItems(items);
    }
}
