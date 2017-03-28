package ru.android.childdiary.presentation.events;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.Food;
import ru.android.childdiary.domain.interactors.calendar.FoodMeasure;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.presentation.events.core.EventDetailPresenter;

@InjectViewState
public class FeedEventDetailPresenter extends EventDetailPresenter<FeedEventDetailView, FeedEvent> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(calendarInteractor.getFoodMeasureList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(foodMeasureList -> logger.debug("showFoodMeasureList: " + foodMeasureList))
                .subscribe(getViewState()::showFoodMeasureList, this::onUnexpectedError));
        unsubscribeOnDestroy(calendarInteractor.getFoodList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(foodList -> logger.debug("showFoodList: " + foodList))
                .subscribe(getViewState()::showFoodList, this::onUnexpectedError));
    }

    public void requestFoodMeasureDialog(String tag) {
        unsubscribeOnDestroy(childInteractor.getActiveChildOnce()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(child -> getViewState().showFoodMeasureDialog(tag, child), this::onUnexpectedError));
    }

    public void requestFoodDialog(String tag) {
        unsubscribeOnDestroy(childInteractor.getActiveChildOnce()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(child -> getViewState().showFoodDialog(tag, child), this::onUnexpectedError));
    }

    public void addFoodMeasure(@NonNull FoodMeasure foodMeasure) {
        unsubscribeOnDestroy(calendarInteractor.addFoodMeasure(foodMeasure)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(addedFoodMeasure -> logger.debug("addFoodMeasure: " + addedFoodMeasure))
                .subscribe(getViewState()::foodMeasureAdded, this::onUnexpectedError));
    }

    public void addFood(@NonNull Food food) {
        unsubscribeOnDestroy(calendarInteractor.addFood(food)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(addedFood -> logger.debug("addFood: " + addedFood))
                .subscribe(getViewState()::foodAdded, this::onUnexpectedError));
    }

    @Override
    protected EventType getEventType() {
        return EventType.FEED;
    }
}
