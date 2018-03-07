package ru.android.healthvector.presentation.events;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.healthvector.domain.calendar.data.standard.FeedEvent;
import ru.android.healthvector.domain.dictionaries.food.data.Food;
import ru.android.healthvector.domain.dictionaries.foodmeasure.data.FoodMeasure;
import ru.android.healthvector.presentation.events.core.EventDetailView;

public interface FeedEventDetailView extends EventDetailView<FeedEvent> {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showFoodMeasureList(@NonNull List<FoodMeasure> foodMeasureList);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showFoodList(@NonNull List<Food> foodList);
}
