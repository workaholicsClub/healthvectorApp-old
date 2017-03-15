package ru.android.childdiary.data.repositories.calendar;

import android.content.Context;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.android.childdiary.domain.interactors.calendar.CalendarRepository;
import ru.android.childdiary.domain.interactors.calendar.FoodMeasure;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.child.Child;

@Singleton
public class CalendarDataRepository implements CalendarRepository {
    private final Context context;
    private final CalendarDbService dbService;
    private final List<OnSelectedDateChangedListener> selectedDateChangedListeners = new ArrayList<>();
    private LocalDate selectedDate = LocalDate.now();

    @Inject
    public CalendarDataRepository(Context context, CalendarDbService dbService) {
        this.context = context;
        this.dbService = dbService;
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
    public Observable<LocalDate> setSelectedDate(@NonNull LocalDate date) {
        return Observable.fromCallable(() -> {
            selectedDate = date;
            for (OnSelectedDateChangedListener listener : selectedDateChangedListeners) {
                listener.onSelectedDateChanged(date);
            }
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
    public Observable<List<MasterEvent>> getAll(@NonNull Child child, @NonNull LocalDate selectedDate) {
        return dbService.getAll(child, selectedDate);
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
    public Observable<DiaperEvent> add(@NonNull Child child, @NonNull DiaperEvent event) {
        return dbService.add(child, event);
    }

    @Override
    public Observable<FeedEvent> add(@NonNull Child child, @NonNull FeedEvent event) {
        return dbService.add(child, event);
    }

    @Override
    public Observable<OtherEvent> add(@NonNull Child child, @NonNull OtherEvent event) {
        return dbService.add(child, event);
    }

    @Override
    public Observable<PumpEvent> add(@NonNull Child child, @NonNull PumpEvent event) {
        return dbService.add(child, event);
    }

    @Override
    public Observable<SleepEvent> add(@NonNull Child child, @NonNull SleepEvent event) {
        return dbService.add(child, event);
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

    public Observable<MasterEvent> delete(@NonNull MasterEvent event) {
        return dbService.delete(event);
    }

    public Observable<MasterEvent> done(@NonNull MasterEvent event) {
        return dbService.done(event);
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
