package ru.android.childdiary.domain.interactors.calendar;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.core.Repository;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.child.Child;

public interface CalendarRepository extends Repository {
    Observable<LocalDate> getSelectedDate();

    Observable<LocalDate> setSelectedDate(@NonNull LocalDate date);

    Observable<List<FoodMeasure>> getFoodMeasureList();

    Observable<FoodMeasure> addFoodMeasure(@NonNull FoodMeasure value);

    Observable<List<MasterEvent>> getAll(@NonNull Child child, @NonNull LocalDate selectedDate);

    Observable<DiaperEvent> getDiaperEventDetail(@NonNull MasterEvent event);

    Observable<FeedEvent> getFeedEventDetail(@NonNull MasterEvent event);

    Observable<OtherEvent> getOtherEventDetail(@NonNull MasterEvent event);

    Observable<PumpEvent> getPumpEventDetail(@NonNull MasterEvent event);

    Observable<SleepEvent> getSleepEventDetail(@NonNull MasterEvent event);

    Observable<DiaperEvent> add(@NonNull Child child, @NonNull DiaperEvent event);

    Observable<FeedEvent> add(@NonNull Child child, @NonNull FeedEvent event);

    Observable<OtherEvent> add(@NonNull Child child, @NonNull OtherEvent event);

    Observable<PumpEvent> add(@NonNull Child child, @NonNull PumpEvent event);

    Observable<SleepEvent> add(@NonNull Child child, @NonNull SleepEvent event);

    Observable<DiaperEvent> update(@NonNull DiaperEvent event);

    Observable<FeedEvent> update(@NonNull FeedEvent event);

    Observable<OtherEvent> update(@NonNull OtherEvent event);

    Observable<PumpEvent> update(@NonNull PumpEvent event);

    Observable<SleepEvent> update(@NonNull SleepEvent event);

    Observable<MasterEvent> delete(@NonNull MasterEvent event);

    Observable<MasterEvent> done(@NonNull MasterEvent event);
}
