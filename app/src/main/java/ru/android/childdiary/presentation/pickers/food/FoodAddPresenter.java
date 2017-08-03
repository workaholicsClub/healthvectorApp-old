package ru.android.childdiary.presentation.pickers.food;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.CalendarInteractor;
import ru.android.childdiary.domain.interactors.calendar.events.core.Food;
import ru.android.childdiary.domain.interactors.medical.MedicalDictionaryInteractor;
import ru.android.childdiary.presentation.pickers.core.BaseAddPresenter;

@InjectViewState
public class FoodAddPresenter extends BaseAddPresenter<Food, FoodAddView> {
    @Inject
    CalendarInteractor calendarInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void add(@NonNull Food item) {
        unsubscribeOnDestroy(calendarInteractor.addFood(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::itemAdded, this::onUnexpectedError));
    }

    @Override
    protected Observable<List<Food>> getAllItemsLoader() {
        return calendarInteractor.getFoodList();
    }

    @Override
    protected MedicalDictionaryInteractor<Food> getInteractor() {
        return null; // TODO calendarInteractor;
    }
}
