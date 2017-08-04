package ru.android.childdiary.presentation.core.fields.dialogs.add.food;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.dictionaries.food.FoodInteractor;
import ru.android.childdiary.domain.interactors.dictionaries.food.data.Food;
import ru.android.childdiary.presentation.core.fields.dialogs.add.core.AddValuePresenter;

@InjectViewState
public class AddFoodPresenter extends AddValuePresenter<Food, AddFoodView> {
    @Inject
    FoodInteractor foodInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void add(@NonNull Food item) {
        unsubscribeOnDestroy(foodInteractor.add(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::added, this::onUnexpectedError));
    }
}
