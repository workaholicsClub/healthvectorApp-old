package ru.android.childdiary.domain.interactors.calendar;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.domain.core.Repository;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.Food;
import ru.android.childdiary.domain.interactors.calendar.events.core.FoodMeasure;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.child.Child;

public interface CalendarRepository extends Repository {
    Observable<LocalDate> getSelectedDate();

    void setSelectedDate(@NonNull LocalDate date);

    Observable<LocalDate> getSelectedDateOnce();

    Observable<LocalDate> setSelectedDateObservable(@NonNull LocalDate date);

    Observable<List<FoodMeasure>> getFoodMeasureList();

    Observable<FoodMeasure> addFoodMeasure(@NonNull FoodMeasure value);

    Observable<List<Food>> getFoodList();

    Observable<Food> addFood(@NonNull Food value);

    Observable<FeedType> getLastFeedType();

    Observable<FoodMeasure> getLastFoodMeasure();

    Observable<Food> getLastFood();

    FeedType setLastFeedType(@Nullable FeedType feedType);

    FoodMeasure setLastFoodMeasure(@Nullable FoodMeasure foodMeasure);

    Food setLastFood(@Nullable Food food);

    Observable<List<MasterEvent>> getAll(@NonNull Child child, @NonNull LocalDate selectedDate);

    Observable<List<SleepEvent>> getSleepEventsWithTimer();

    Observable<DiaperEvent> getDiaperEventDetail(@NonNull MasterEvent event);

    Observable<FeedEvent> getFeedEventDetail(@NonNull MasterEvent event);

    Observable<OtherEvent> getOtherEventDetail(@NonNull MasterEvent event);

    Observable<PumpEvent> getPumpEventDetail(@NonNull MasterEvent event);

    Observable<SleepEvent> getSleepEventDetail(@NonNull MasterEvent event);

    Observable<DiaperEvent> add(@NonNull DiaperEvent event);

    Observable<FeedEvent> add(@NonNull FeedEvent event);

    Observable<OtherEvent> add(@NonNull OtherEvent event);

    Observable<PumpEvent> add(@NonNull PumpEvent event);

    Observable<SleepEvent> add(@NonNull SleepEvent event);

    Observable<DiaperEvent> update(@NonNull DiaperEvent event);

    Observable<FeedEvent> update(@NonNull FeedEvent event);

    Observable<OtherEvent> update(@NonNull OtherEvent event);

    Observable<PumpEvent> update(@NonNull PumpEvent event);

    Observable<SleepEvent> update(@NonNull SleepEvent event);

    Observable<MasterEvent> delete(@NonNull MasterEvent event);

    Observable<MasterEvent> done(@NonNull MasterEvent event);
}
