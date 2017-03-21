package ru.android.childdiary.presentation.events;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
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
    }

    public void addFoodMeasure(@NonNull FoodMeasure foodMeasure) {
        unsubscribeOnDestroy(calendarInteractor.addFoodMeasure(foodMeasure)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(addedFoodMeasure -> logger.debug("addFoodMeasure: " + addedFoodMeasure))
                .subscribe(getViewState()::foodMeasureAdded, this::onUnexpectedError));
    }
}
