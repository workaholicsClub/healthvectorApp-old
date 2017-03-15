package ru.android.childdiary.presentation.events;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.childdiary.domain.interactors.calendar.FoodMeasure;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.presentation.events.core.EventDetailView;

public interface FeedEventDetailView extends EventDetailView<FeedEvent> {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showFoodMeasureList(@NonNull List<FoodMeasure> foodMeasureList);
}
