package ru.android.childdiary.data.repositories.calendar;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.core.ValueDataRepository;
import ru.android.childdiary.data.repositories.dictionaries.core.CrudDbService;
import ru.android.childdiary.data.repositories.dictionaries.food.FoodDbService;
import ru.android.childdiary.data.repositories.dictionaries.foodmeasure.FoodMeasureDbService;
import ru.android.childdiary.data.repositories.medical.DoctorVisitDbService;
import ru.android.childdiary.data.repositories.medical.MedicineTakingDbService;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.domain.calendar.CalendarRepository;
import ru.android.childdiary.domain.calendar.data.DoctorVisitEvent;
import ru.android.childdiary.domain.calendar.data.ExerciseEvent;
import ru.android.childdiary.domain.calendar.data.MedicineTakingEvent;
import ru.android.childdiary.domain.calendar.data.core.EventNotification;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.calendar.data.core.PeriodicityType;
import ru.android.childdiary.domain.calendar.data.core.SoundInfo;
import ru.android.childdiary.domain.calendar.data.core.TimeUnit;
import ru.android.childdiary.domain.calendar.data.standard.DiaperEvent;
import ru.android.childdiary.domain.calendar.data.standard.FeedEvent;
import ru.android.childdiary.domain.calendar.data.standard.OtherEvent;
import ru.android.childdiary.domain.calendar.data.standard.PumpEvent;
import ru.android.childdiary.domain.calendar.data.standard.SleepEvent;
import ru.android.childdiary.domain.calendar.requests.GetDoctorVisitEventsRequest;
import ru.android.childdiary.domain.calendar.requests.GetDoctorVisitEventsResponse;
import ru.android.childdiary.domain.calendar.requests.GetEventsRequest;
import ru.android.childdiary.domain.calendar.requests.GetEventsResponse;
import ru.android.childdiary.domain.calendar.requests.GetMedicineTakingEventsRequest;
import ru.android.childdiary.domain.calendar.requests.GetMedicineTakingEventsResponse;
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
import ru.android.childdiary.utils.ObjectUtils;
import ru.android.childdiary.utils.strings.TimeUtils;

@Singleton
public class CalendarDataRepository extends ValueDataRepository<LocalDate> implements CalendarRepository {
    private static final String KEY_LAST_FEED_TYPE = "last_feed_type";
    private static final String KEY_LAST_FOOD_MEASURE_ID = "last_food_measure";
    private static final String KEY_LAST_FOOD_ID = "last_food";
    private static final String KEY_NOTIFY_TIME_PREFIX = "notify_time_";
    private static final String KEY_NOTIFICATION_SOUND_INFO_PREFIX = "notification_sound_info_";
    private static final String KEY_DONT_NOTIFY_PREFIX = "dont_notify_";
    private static final String KEY_VIBRATION_PREFIX = "vibration_";
    private static final Map<EventType, Integer> defaultNotifyTimeValues = new HashMap<EventType, Integer>() {{
        put(EventType.DIAPER, 5);
        put(EventType.FEED, 30);
        put(EventType.OTHER, 10);
        put(EventType.PUMP, 10);
        put(EventType.SLEEP, 60);
        put(EventType.DOCTOR_VISIT, TimeUtils.MINUTES_IN_DAY);
        put(EventType.MEDICINE_TAKING, 10);
        put(EventType.EXERCISE, 30);
    }};
    private static final SoundInfoAdapter soundInfoAdapter = new SoundInfoAdapter();

    private final RxSharedPreferences preferences;
    private final CalendarDbService calendarDbService;
    private final AllEventsDbService allEventsDbService;
    private final DoctorVisitDbService doctorVisitDbService;
    private final MedicineTakingDbService medicineTakingDbService;
    private final CleanUpDbService cleanUpDbService;
    private CrudDbService<Food> foodDbService;
    private CrudDbService<FoodMeasure> foodMeasureDbService;

    @Inject
    public CalendarDataRepository(RxSharedPreferences preferences,
                                  CalendarDbService calendarDbService,
                                  AllEventsDbService allEventsDbService,
                                  DoctorVisitDbService doctorVisitDbService,
                                  MedicineTakingDbService medicineTakingDbService,
                                  CleanUpDbService cleanUpDbService,
                                  FoodDbService foodDbService,
                                  FoodMeasureDbService foodMeasureDbService) {
        this.preferences = preferences;
        this.calendarDbService = calendarDbService;
        this.allEventsDbService = allEventsDbService;
        this.doctorVisitDbService = doctorVisitDbService;
        this.medicineTakingDbService = medicineTakingDbService;
        this.cleanUpDbService = cleanUpDbService;
        this.foodDbService = foodDbService;
        this.foodMeasureDbService = foodMeasureDbService;
    }

    @Override
    public Observable<LocalDate> getSelectedDate() {
        return getSelectedValue();
    }

    @Override
    public void setSelectedDate(@NonNull LocalDate date) {
        setSelectedValue(date);
    }

    @Override
    public Observable<LocalDate> getSelectedDateOnce() {
        return getSelectedValueOnce();
    }

    @Override
    public Observable<LocalDate> setSelectedDateObservable(@NonNull LocalDate date) {
        return setSelectedValueObservable(date);
    }

    @Override
    protected LocalDate getDefaultValue() {
        return LocalDate.now();
    }

    @Override
    public Observable<FeedType> getLastFeedType() {
        return preferences
                .getEnum(KEY_LAST_FEED_TYPE, FeedType.BREAST_MILK, FeedType.class)
                .asObservable()
                .first(FeedType.BREAST_MILK)
                .toObservable();
    }

    @Override
    public Observable<FoodMeasure> getLastFoodMeasure() {
        return Observable.combineLatest(
                foodMeasureDbService.getAll()
                        .first(Collections.singletonList(FoodMeasure.NULL))
                        .toObservable(),
                preferences
                        .getLong(KEY_LAST_FOOD_MEASURE_ID)
                        .asObservable()
                        .first(0L)
                        .toObservable(),
                this::getLastFoodMeasure)
                .first(FoodMeasure.NULL)
                .toObservable();
    }

    private FoodMeasure getLastFoodMeasure(@NonNull List<FoodMeasure> foodMeasureList, Long id) {
        return foodMeasureList.isEmpty() ? FoodMeasure.NULL : Observable
                .fromIterable(foodMeasureList)
                .filter(food -> ObjectUtils.equals(food.getId(), id))
                .first(foodMeasureList.get(0))
                .blockingGet();
    }

    @Override
    public Observable<Food> getLastFood() {
        return Observable.combineLatest(
                foodDbService.getAll()
                        .first(Collections.singletonList(Food.NULL))
                        .toObservable(),
                preferences
                        .getLong(KEY_LAST_FOOD_ID)
                        .asObservable()
                        .first(0L)
                        .toObservable(),
                this::getLastFood)
                .first(Food.NULL)
                .toObservable();
    }

    private Food getLastFood(@NonNull List<Food> foodList, Long id) {
        return Observable
                .fromIterable(foodList)
                .filter(food -> ObjectUtils.equals(food.getId(), id))
                .first(Food.NULL)
                .blockingGet();
    }

    @Override
    public FeedType setLastFeedType(@Nullable FeedType feedType) {
        preferences.getEnum(KEY_LAST_FEED_TYPE, FeedType.BREAST_MILK, FeedType.class).set(feedType);
        return feedType;
    }

    @Override
    public FoodMeasure setLastFoodMeasure(@Nullable FoodMeasure foodMeasure) {
        preferences.getLong(KEY_LAST_FOOD_MEASURE_ID).set(foodMeasure == null ? null : foodMeasure.getId());
        return foodMeasure;
    }

    @Override
    public Food setLastFood(@Nullable Food food) {
        preferences.getLong(KEY_LAST_FOOD_ID).set(food == null ? null : food.getId());
        return food;
    }

    @Override
    public Observable<GetEventsResponse> getAll(@NonNull GetEventsRequest request) {
        return allEventsDbService.getAllEvents(request);
    }

    @Override
    public Observable<GetSleepEventsResponse> getSleepEvents(@NonNull GetSleepEventsRequest request) {
        return calendarDbService.getSleepEvents(request);
    }

    @Override
    public Observable<GetDoctorVisitEventsResponse> getDoctorVisitEvents(@NonNull GetDoctorVisitEventsRequest request) {
        return calendarDbService.getDoctorVisitEvents(request);
    }

    @Override
    public Observable<GetMedicineTakingEventsResponse> getMedicineTakingEvents(@NonNull GetMedicineTakingEventsRequest request) {
        return calendarDbService.getMedicineTakingEvents(request);
    }

    @Override
    public Observable<DiaperEvent> getDiaperEventDetail(@NonNull MasterEvent event) {
        return calendarDbService.getDiaperEventDetail(event);
    }

    @Override
    public Observable<FeedEvent> getFeedEventDetail(@NonNull MasterEvent event) {
        return calendarDbService.getFeedEventDetail(event);
    }

    @Override
    public Observable<OtherEvent> getOtherEventDetail(@NonNull MasterEvent event) {
        return calendarDbService.getOtherEventDetail(event);
    }

    @Override
    public Observable<PumpEvent> getPumpEventDetail(@NonNull MasterEvent event) {
        return calendarDbService.getPumpEventDetail(event);
    }

    @Override
    public Observable<SleepEvent> getSleepEventDetail(@NonNull MasterEvent event) {
        return calendarDbService.getSleepEventDetail(event);
    }

    @Override
    public Observable<DoctorVisitEvent> getDoctorVisitEventDetail(@NonNull MasterEvent event) {
        return calendarDbService.getDoctorVisitEventDetail(event);
    }

    @Override
    public Observable<MedicineTakingEvent> getMedicineTakingEventDetail(@NonNull MasterEvent event) {
        return calendarDbService.getMedicineTakingEventDetail(event);
    }

    @Override
    public Observable<ExerciseEvent> getExerciseEventDetail(@NonNull MasterEvent event) {
        return calendarDbService.getExerciseEventDetail(event);
    }

    @Override
    public Observable<DiaperEvent> add(@NonNull DiaperEvent event) {
        return calendarDbService.add(event);
    }

    @Override
    public Observable<FeedEvent> add(@NonNull FeedEvent event) {
        return calendarDbService.add(event);
    }

    @Override
    public Observable<OtherEvent> add(@NonNull OtherEvent event) {
        return calendarDbService.add(event);
    }

    @Override
    public Observable<PumpEvent> add(@NonNull PumpEvent event) {
        return calendarDbService.add(event);
    }

    @Override
    public Observable<SleepEvent> add(@NonNull SleepEvent event) {
        return calendarDbService.add(event);
    }

    @Override
    public Observable<MasterEvent> updateMasterEvent(@NonNull MasterEvent event) {
        return calendarDbService.updateMasterEventObservable(event);
    }

    @Override
    public Observable<DiaperEvent> update(@NonNull DiaperEvent event) {
        return calendarDbService.update(event);
    }

    @Override
    public Observable<FeedEvent> update(@NonNull FeedEvent event) {
        return calendarDbService.update(event);
    }

    @Override
    public Observable<OtherEvent> update(@NonNull OtherEvent event) {
        return calendarDbService.update(event);
    }

    @Override
    public Observable<PumpEvent> update(@NonNull PumpEvent event) {
        return calendarDbService.update(event);
    }

    @Override
    public Observable<SleepEvent> update(@NonNull SleepEvent event) {
        return calendarDbService.update(event);
    }

    @Override
    public Observable<UpdateDoctorVisitEventResponse> update(@NonNull UpdateDoctorVisitEventRequest request) {
        return calendarDbService.update(request);
    }

    @Override
    public Observable<UpdateMedicineTakingEventResponse> update(@NonNull UpdateMedicineTakingEventRequest request) {
        return calendarDbService.update(request);
    }

    @Override
    public Observable<UpdateExerciseEventResponse> update(@NonNull UpdateExerciseEventRequest request) {
        return calendarDbService.update(request);
    }

    public <T extends MasterEvent> Observable<List<String>> delete(@NonNull T event) {
        return cleanUpDbService.deleteEvent(event);
    }

    public Observable<DeleteDoctorVisitEventsResponse> deleteLinearGroup(@NonNull DeleteDoctorVisitEventsRequest request) {
        return cleanUpDbService.deleteDoctorVisitEvents(request);
    }

    public Observable<DeleteMedicineTakingEventsResponse> deleteLinearGroup(@NonNull DeleteMedicineTakingEventsRequest request) {
        return cleanUpDbService.deleteMedicineTakingEvents(request);
    }

    @Override
    public Observable<DeleteConcreteExerciseEventsResponse> deleteLinearGroup(@NonNull DeleteConcreteExerciseEventsRequest request) {
        return cleanUpDbService.deleteExerciseEvents(request);
    }

    @Override
    public Observable<EventNotification> getNotificationSettings(@NonNull EventType eventType) {
        return Observable.combineLatest(
                getNotifyTimeInMinutes(eventType),
                getNotificationSound(eventType),
                getDontNotify(eventType),
                getVibration(eventType),
                (minutes, soundInfo, dontNotify, vibration) -> EventNotification.builder()
                        .eventType(eventType)
                        .minutes(minutes)
                        .soundInfo(soundInfo)
                        .dontNotify(dontNotify)
                        .vibration(vibration)
                        .build());
    }

    @Override
    public Observable<EventNotification> getNotificationSettingsOnce(@NonNull EventType eventType) {
        return getNotificationSettings(eventType)
                .first(EventNotification.builder()
                        .eventType(eventType)
                        .build())
                .toObservable();
    }

    @Override
    public Observable<EventNotification> setNotificationSettings(@NonNull EventNotification eventNotification) {
        return Observable.fromCallable(() -> {
            EventType eventType = eventNotification.getEventType();
            String keyNotifyTime = KEY_NOTIFY_TIME_PREFIX + eventType;
            preferences.getInteger(keyNotifyTime).set(eventNotification.getMinutes());
            preferences.getObject(KEY_NOTIFICATION_SOUND_INFO_PREFIX + eventType, SoundInfo.NULL, soundInfoAdapter).set(eventNotification.getSoundInfo());
            String keyDontNotify = KEY_DONT_NOTIFY_PREFIX + eventType;
            preferences.getBoolean(keyDontNotify).set(eventNotification.isDontNotify());
            String keyVibration = KEY_VIBRATION_PREFIX + eventType;
            preferences.getBoolean(keyVibration).set(eventNotification.isVibration());
            return eventNotification;
        });
    }

    private Observable<Integer> getNotifyTimeInMinutes(@NonNull EventType eventType) {
        String key = KEY_NOTIFY_TIME_PREFIX + eventType;
        int defaultValue = defaultNotifyTimeValues.get(eventType);
        return preferences.getInteger(key, defaultValue).asObservable();
    }

    private Observable<Integer> getNotifyTimeInMinutesOnce(@NonNull EventType eventType) {
        int defaultValue = defaultNotifyTimeValues.get(eventType);
        return getNotifyTimeInMinutes(eventType).first(defaultValue).toObservable();
    }

    private Observable<SoundInfo> getNotificationSound(@NonNull EventType eventType) {
        String key = KEY_NOTIFICATION_SOUND_INFO_PREFIX + eventType;
        return preferences.getObject(key, SoundInfo.NULL, soundInfoAdapter).asObservable();
    }

    private Observable<SoundInfo> getNotificationSoundOnce(@NonNull EventType eventType) {
        return getNotificationSound(eventType).first(SoundInfo.NULL).toObservable();
    }

    private Observable<Boolean> getDontNotify(@NonNull EventType eventType) {
        String key = KEY_DONT_NOTIFY_PREFIX + eventType;
        return preferences.getBoolean(key, eventType == EventType.DIAPER).asObservable();
    }

    private Observable<Boolean> getDontNotifyOnce(@NonNull EventType eventType) {
        return getDontNotify(eventType).first(eventType == EventType.DIAPER).toObservable();
    }

    private Observable<Boolean> getVibration(@NonNull EventType eventType) {
        String key = KEY_VIBRATION_PREFIX + eventType;
        return preferences.getBoolean(key, false).asObservable();
    }

    private Observable<Boolean> getVibrationOnce(@NonNull EventType eventType) {
        return getVibration(eventType).first(false).toObservable();
    }

    @Override
    public Observable<List<Integer>> getFrequencyList(@NonNull EventType eventType) {
        if (eventType == EventType.DOCTOR_VISIT) {
            return Observable.just(Arrays.asList(0, 1));
        }
        return Observable.just(Arrays.asList(0, 1, 2, 3, 4, 5));
    }

    @Override
    public Observable<List<PeriodicityType>> getPeriodicityList() {
        return Observable.just(Arrays.asList(PeriodicityType.values()));
    }

    @Override
    public Observable<Map<TimeUnit, List<Integer>>> getTimeUnitValues() {
        return Observable.fromCallable(() -> {
            Map<TimeUnit, List<Integer>> map = new HashMap<>();
            map.put(TimeUnit.DAY, Observable.range(1, 30).toList().blockingGet());
            map.put(TimeUnit.WEEK, Observable.range(1, 52).toList().blockingGet());
            map.put(TimeUnit.MONTH, Observable.range(1, TimeUtils.MONTHS_IN_YEAR).toList().blockingGet());
            return map;
        });
    }

    private static class SoundInfoAdapter implements Preference.Adapter<SoundInfo> {
        private static final String DELIMITER = ";";
        private final Logger logger = LoggerFactory.getLogger(toString());

        @Override
        public SoundInfo get(@NonNull String key, @NonNull SharedPreferences preferences) {
            String content = preferences.getString(key, null);
            if (TextUtils.isEmpty(content)) {
                return SoundInfo.NULL;
            }
            String[] parts = content.split(DELIMITER);
            if (parts.length != 2 || TextUtils.isEmpty(parts[0]) || TextUtils.isEmpty(parts[1])) {
                return SoundInfo.NULL;
            }
            return SoundInfo.builder()
                    .name(parts[0])
                    .path(parts[1])
                    .build();
        }

        @Override
        public void set(@NonNull String key, @NonNull SoundInfo value, @NonNull SharedPreferences.Editor editor) {
            String name = value.getName() == null ? "" : value.getName();
            String path = value.getPath() == null ? "" : value.getPath();
            String content = name + DELIMITER + path;
            editor.putString(key, content).commit();
        }
    }
}
