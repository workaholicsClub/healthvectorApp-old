package ru.android.childdiary.presentation.events;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.childdiary.domain.interactors.calendar.events.core.Food;
import ru.android.childdiary.domain.interactors.calendar.events.core.FoodMeasure;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.presentation.events.core.EventDetailView;

public interface FeedEventDetailView extends EventDetailView<FeedEvent> {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showFoodMeasureList(@NonNull List<FoodMeasure> foodMeasureList);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void foodMeasureAdded(@NonNull FoodMeasure foodMeasure);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showFoodList(@NonNull List<Food> foodList);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void foodAdded(@NonNull Food food);
}
