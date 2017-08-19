package ru.android.childdiary.domain.calendar;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.domain.calendar.data.DoctorVisitEvent;
import ru.android.childdiary.domain.calendar.data.ExerciseEvent;
import ru.android.childdiary.domain.calendar.data.MedicineTakingEvent;
import ru.android.childdiary.domain.calendar.data.core.EventNotification;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.calendar.data.core.PeriodicityType;
import ru.android.childdiary.domain.calendar.data.core.TimeUnit;
import ru.android.childdiary.domain.calendar.data.standard.DiaperEvent;
import ru.android.childdiary.domain.calendar.data.standard.FeedEvent;
import ru.android.childdiary.domain.calendar.data.standard.OtherEvent;
import ru.android.childdiary.domain.calendar.data.standard.PumpEvent;
import ru.android.childdiary.domain.calendar.data.standard.SleepEvent;
import ru.android.childdiary.domain.calendar.requests.GetEventsRequest;
import ru.android.childdiary.domain.calendar.requests.GetEventsResponse;
import ru.android.childdiary.domain.calendar.requests.GetSleepEventsRequest;
import ru.android.childdiary.domain.calendar.requests.GetSleepEventsResponse;
import ru.android.childdiary.domain.calendar.requests.UpdateDoctorVisitEventRequest;
import ru.android.childdiary.domain.calendar.requests.UpdateDoctorVisitEventResponse;
import ru.android.childdiary.domain.calendar.requests.UpdateExerciseEventRequest;
import ru.android.childdiary.domain.calendar.requests.UpdateExerciseEventResponse;
import ru.android.childdiary.domain.calendar.requests.UpdateMedicineTakingEventRequest;
import ru.android.childdiary.domain.calendar.requests.UpdateMedicineTakingEventResponse;
import ru.android.childdiary.domain.dictionaries.food.data.Food;
import ru.android.childdiary.domain.dictionaries.foodmeasure.data.FoodMeasure;
import ru.android.childdiary.domain.exercises.requests.DeleteConcreteExerciseEventsRequest;
import ru.android.childdiary.domain.exercises.requests.DeleteConcreteExerciseEventsResponse;
import ru.android.childdiary.domain.medical.requests.DeleteDoctorVisitEventsRequest;
import ru.android.childdiary.domain.medical.requests.DeleteDoctorVisitEventsResponse;
import ru.android.childdiary.domain.medical.requests.DeleteMedicineTakingEventsRequest;
import ru.android.childdiary.domain.medical.requests.DeleteMedicineTakingEventsResponse;

public interface CalendarRepository {
    Observable<LocalDate> getSelectedDate();

    void setSelectedDate(@NonNull LocalDate date);

    Observable<LocalDate> getSelectedDateOnce();

    Observable<LocalDate> setSelectedDateObservable(@NonNull LocalDate date);

    Observable<FeedType> getLastFeedType();

    Observable<FoodMeasure> getLastFoodMeasure();

    Observable<Food> getLastFood();

    FeedType setLastFeedType(@Nullable FeedType feedType);

    FoodMeasure setLastFoodMeasure(@Nullable FoodMeasure foodMeasure);

    Food setLastFood(@Nullable Food food);

    Observable<GetEventsResponse> getAll(@NonNull GetEventsRequest request);

    Observable<GetSleepEventsResponse> getSleepEvents(@NonNull GetSleepEventsRequest request);

    Observable<DiaperEvent> getDiaperEventDetail(@NonNull MasterEvent event);

    Observable<FeedEvent> getFeedEventDetail(@NonNull MasterEvent event);

    Observable<OtherEvent> getOtherEventDetail(@NonNull MasterEvent event);

    Observable<PumpEvent> getPumpEventDetail(@NonNull MasterEvent event);

    Observable<SleepEvent> getSleepEventDetail(@NonNull MasterEvent event);

    Observable<DoctorVisitEvent> getDoctorVisitEventDetail(@NonNull MasterEvent event);

    Observable<MedicineTakingEvent> getMedicineTakingEventDetail(@NonNull MasterEvent event);

    Observable<ExerciseEvent> getExerciseEventDetail(@NonNull MasterEvent event);

    Observable<DiaperEvent> add(@NonNull DiaperEvent event);

    Observable<FeedEvent> add(@NonNull FeedEvent event);

    Observable<OtherEvent> add(@NonNull OtherEvent event);

    Observable<PumpEvent> add(@NonNull PumpEvent event);

    Observable<SleepEvent> add(@NonNull SleepEvent event);

    Observable<MasterEvent> updateMasterEvent(@NonNull MasterEvent event);

    Observable<DiaperEvent> update(@NonNull DiaperEvent event);

    Observable<FeedEvent> update(@NonNull FeedEvent event);

    Observable<OtherEvent> update(@NonNull OtherEvent event);

    Observable<PumpEvent> update(@NonNull PumpEvent event);

    Observable<SleepEvent> update(@NonNull SleepEvent event);

    Observable<UpdateDoctorVisitEventResponse> update(@NonNull UpdateDoctorVisitEventRequest request);

    Observable<UpdateMedicineTakingEventResponse> update(@NonNull UpdateMedicineTakingEventRequest request);

    Observable<UpdateExerciseEventResponse> update(@NonNull UpdateExerciseEventRequest request);

    <T extends MasterEvent> Observable<List<String>> delete(@NonNull T request);

    Observable<DeleteDoctorVisitEventsResponse> deleteLinearGroup(@NonNull DeleteDoctorVisitEventsRequest request);

    Observable<DeleteMedicineTakingEventsResponse> deleteLinearGroup(@NonNull DeleteMedicineTakingEventsRequest request);

    Observable<DeleteConcreteExerciseEventsResponse> deleteLinearGroup(@NonNull DeleteConcreteExerciseEventsRequest request);

    Observable<EventNotification> getNotificationSettings(@NonNull EventType eventType);

    Observable<EventNotification> getNotificationSettingsOnce(@NonNull EventType eventType);

    Observable<EventNotification> setNotificationSettings(@NonNull EventNotification eventNotification);

    Observable<List<Integer>> getFrequencyList(@NonNull EventType eventType);

    Observable<List<PeriodicityType>> getPeriodicityList();

    Observable<Map<TimeUnit, List<Integer>>> getTimeUnitValues();

    Observable<List<MasterEvent>> getFinishedLinearGroupEvents(@NonNull LocalDate lastCheckedDate,
                                                               @NonNull LocalDate dateToCheck);
}
