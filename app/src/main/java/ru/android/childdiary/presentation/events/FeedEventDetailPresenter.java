package ru.android.childdiary.presentation.events;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
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
                .doOnNext(child -> logger.debug("showChild: " + child))
                .subscribe(getViewState()::showFoodMeasureList, this::onUnexpectedError));
    }
}
