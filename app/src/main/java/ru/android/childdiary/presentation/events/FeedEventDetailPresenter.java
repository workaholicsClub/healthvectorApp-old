package ru.android.childdiary.presentation.events;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.calendar.data.standard.FeedEvent;
import ru.android.childdiary.domain.dictionaries.food.FoodInteractor;
import ru.android.childdiary.domain.dictionaries.foodmeasure.FoodMeasureInteractor;
import ru.android.childdiary.presentation.events.core.EventDetailPresenter;

@InjectViewState
public class FeedEventDetailPresenter extends EventDetailPresenter<FeedEventDetailView, FeedEvent> {
    @Inject
    FoodInteractor foodInteractor;

    @Inject
    FoodMeasureInteractor foodMeasureInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(foodMeasureInteractor.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(foodMeasureList -> logger.debug("showFoodMeasureList: " + foodMeasureList))
                .subscribe(getViewState()::showFoodMeasureList, this::onUnexpectedError));
        unsubscribeOnDestroy(foodInteractor.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(foodList -> logger.debug("showFoodList: " + foodList))
                .subscribe(getViewState()::showFoodList, this::onUnexpectedError));
    }

    @Override
    protected EventType getEventType() {
        return EventType.FEED;
    }
}
