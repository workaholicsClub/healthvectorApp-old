package ru.android.childdiary.data.repositories.calendar;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f2prateek.rx.preferences2.RxSharedPreferences;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.domain.interactors.calendar.CalendarRepository;
import ru.android.childdiary.domain.interactors.calendar.events.DoctorVisitEvent;
import ru.android.childdiary.domain.interactors.calendar.events.MedicineTakingEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.Food;
import ru.android.childdiary.domain.interactors.calendar.events.core.FoodMeasure;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.calendar.requests.EventsRequest;
import ru.android.childdiary.domain.interactors.core.PeriodicityType;
import ru.android.childdiary.domain.interactors.core.TimeUnit;
import ru.android.childdiary.utils.ObjectUtils;
import ru.android.childdiary.utils.TimeUtils;

@Singleton
public class CalendarDataRepository implements CalendarRepository {
    private static final String KEY_LAST_FEED_TYPE = "last_feed_type";
    private static final String KEY_LAST_FOOD_MEASURE_ID = "last_food_measure";
    private static final String KEY_LAST_FOOD_ID = "last_food";

    private final RxSharedPreferences preferences;
    private final CalendarDbService dbService;
    private final AllEventsDbService allEventsDbService;
    private final List<OnSelectedDateChangedListener> selectedDateChangedListeners = new ArrayList<>();
    private LocalDate selectedDate = LocalDate.now();

    @Inject
    public CalendarDataRepository(RxSharedPreferences preferences,
                                  CalendarDbService dbService,
                                  AllEventsDbService allEventsDbService) {
        this.preferences = preferences;
        this.dbService = dbService;
        this.allEventsDbService = allEventsDbService;
    }

    void addOnActiveChildChangedListener(OnSelectedDateChangedListener listener) {
        synchronized (selectedDateChangedListeners) {
            selectedDateChangedListeners.add(listener);
        }
    }

    void removeOnActiveChildChangedListener(OnSelectedDateChangedListener listener) {
        synchronized (selectedDateChangedListeners) {
            selectedDateChangedListeners.remove(listener);
        }
    }

    @Override
    public Observable<LocalDate> getSelectedDate() {
        return new SelectedDateObservable();
    }

    @Override
    public void setSelectedDate(@NonNull LocalDate date) {
        selectedDate = date;
        synchronized (selectedDateChangedListeners) {
            for (OnSelectedDateChangedListener listener : selectedDateChangedListeners) {
                listener.onSelectedDateChanged(date);
            }
        }
    }

    @Override
    public Observable<LocalDate> getSelectedDateOnce() {
        return getSelectedDate().first(LocalDate.now()).toObservable();
    }

    @Override
    public Observable<LocalDate> setSelectedDateObservable(@NonNull LocalDate date) {
        return Observable.fromCallable(() -> {
            setSelectedDate(date);
            return date;
        });
    }

    @Override
    public Observable<List<FoodMeasure>> getFoodMeasureList() {
        return dbService.getFoodMeasureList();
    }

    @Override
    public Observable<FoodMeasure> addFoodMeasure(@NonNull FoodMeasure foodMeasure) {
        return dbService.addFoodMeasure(foodMeasure);
    }

    @Override
    public Observable<List<Food>> getFoodList() {
        return dbService.getFoodList();
    }

    @Override
    public Observable<Food> addFood(@NonNull Food food) {
        return dbService.addFood(food);
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
                getFoodMeasureList()
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
        return Observable
                .fromIterable(foodMeasureList)
                .filter(food -> ObjectUtils.equals(food.getId(), id))
                .first(foodMeasureList.get(0))
                .blockingGet();
    }

    @Override
    public Observable<Food> getLastFood() {
        return Observable.combineLatest(
                getFoodList()
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
    public Observable<List<MasterEvent>> getAll(@NonNull EventsRequest request) {
        return allEventsDbService.getAllEvents(request);
    }

    @Override
    public Observable<List<SleepEvent>> getSleepEventsWithTimer() {
        return dbService.getSleepEventsWithTimer();
    }

    @Override
    public Observable<DiaperEvent> getDiaperEventDetail(@NonNull MasterEvent event) {
        return dbService.getDiaperEventDetail(event);
    }

    @Override
    public Observable<FeedEvent> getFeedEventDetail(@NonNull MasterEvent event) {
        return dbService.getFeedEventDetail(event);
    }

    @Override
    public Observable<OtherEvent> getOtherEventDetail(@NonNull MasterEvent event) {
        return dbService.getOtherEventDetail(event);
    }

    @Override
    public Observable<PumpEvent> getPumpEventDetail(@NonNull MasterEvent event) {
        return dbService.getPumpEventDetail(event);
    }

    @Override
    public Observable<SleepEvent> getSleepEventDetail(@NonNull MasterEvent event) {
        return dbService.getSleepEventDetail(event);
    }

    @Override
    public Observable<DoctorVisitEvent> getDoctorVisitEventDetail(@NonNull MasterEvent event) {
        return dbService.getDoctorVisitEventDetail(event);
    }

    @Override
    public Observable<MedicineTakingEvent> getMedicineTakingEventDetail(@NonNull MasterEvent event) {
        return dbService.getMedicineTakingEventDetail(event);
    }

    @Override
    public Observable<DiaperEvent> add(@NonNull DiaperEvent event) {
        return dbService.add(event);
    }

    @Override
    public Observable<FeedEvent> add(@NonNull FeedEvent event) {
        return dbService.add(event);
    }

    @Override
    public Observable<OtherEvent> add(@NonNull OtherEvent event) {
        return dbService.add(event);
    }

    @Override
    public Observable<PumpEvent> add(@NonNull PumpEvent event) {
        return dbService.add(event);
    }

    @Override
    public Observable<SleepEvent> add(@NonNull SleepEvent event) {
        return dbService.add(event);
    }

    @Override
    public Observable<DiaperEvent> update(@NonNull DiaperEvent event) {
        return dbService.update(event);
    }

    @Override
    public Observable<FeedEvent> update(@NonNull FeedEvent event) {
        return dbService.update(event);
    }

    @Override
    public Observable<OtherEvent> update(@NonNull OtherEvent event) {
        return dbService.update(event);
    }

    @Override
    public Observable<PumpEvent> update(@NonNull PumpEvent event) {
        return dbService.update(event);
    }

    @Override
    public Observable<SleepEvent> update(@NonNull SleepEvent event) {
        return dbService.update(event);
    }

    @Override
    public Observable<DoctorVisitEvent> update(@NonNull DoctorVisitEvent event) {
        return dbService.update(event);
    }

    @Override
    public Observable<MedicineTakingEvent> update(@NonNull MedicineTakingEvent event) {
        return dbService.update(event);
    }

    @Override
    public Observable<MasterEvent> delete(@NonNull MasterEvent event) {
        return dbService.delete(event);
    }

    @Override
    public Observable<MasterEvent> done(@NonNull MasterEvent event) {
        return dbService.done(event);
    }

    @Override
    public Observable<Integer> getDefaultNotifyTimeInMinutes(@NonNull EventType eventType) {
        switch (eventType) {
            case DIAPER:
                return Observable.just(0);
            case FEED:
                return Observable.just(30);
            case OTHER:
                return Observable.just(10);
            case PUMP:
                return Observable.just(10);
            case SLEEP:
                return Observable.just(60);
            case DOCTOR_VISIT:
                return Observable.just(TimeUtils.MINUTES_IN_DAY);
            case MEDICINE_TAKING:
                return Observable.just(10);
            // TODO EXERCISE
        }
        throw new IllegalStateException("Unsupported event type");
    }

    @Override
    public Observable<List<Integer>> getFrequencyList() {
        return Observable.just(Arrays.asList(0, 1, 2, 3, 4, 5));
    }

    @Override
    public Observable<List<PeriodicityType>> getPeriodicityList() {
        return Observable.just(Arrays.asList(PeriodicityType.values()));
    }

    @Override
    public Observable<List<TimeUnit>> getTimeUnits() {
        return Observable.just(Arrays.asList(TimeUnit.values()));
    }

    private interface OnSelectedDateChangedListener {
        void onSelectedDateChanged(LocalDate date);
    }

    private class SelectedDateObservable extends Observable<LocalDate> {
        @Override
        protected void subscribeActual(Observer<? super LocalDate> observer) {
            OnSelectedDateChangedSubscription listener = new OnSelectedDateChangedSubscription(observer);
            listener.subscribe();
        }
    }

    private class OnSelectedDateChangedSubscription implements Disposable, OnSelectedDateChangedListener {
        private final Observer<? super LocalDate> observer;
        private final AtomicBoolean unsubscribed = new AtomicBoolean();

        public OnSelectedDateChangedSubscription(Observer<? super LocalDate> observer) {
            this.observer = observer;
        }

        public void subscribe() {
            addOnActiveChildChangedListener(this);
            observer.onSubscribe(this);
            observer.onNext(selectedDate);
        }

        @Override
        public void onSelectedDateChanged(@NonNull LocalDate date) {
            observer.onNext(date);
        }

        @Override
        public void dispose() {
            if (unsubscribed.compareAndSet(false, true)) {
                removeOnActiveChildChangedListener(this);
            }
        }

        @Override
        public boolean isDisposed() {
            return unsubscribed.get();
        }
    }
}
