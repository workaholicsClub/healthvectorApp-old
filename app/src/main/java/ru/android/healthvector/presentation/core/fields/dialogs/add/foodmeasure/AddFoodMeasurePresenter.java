package ru.android.healthvector.presentation.core.fields.dialogs.add.foodmeasure;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.dictionaries.foodmeasure.FoodMeasureInteractor;
import ru.android.healthvector.domain.dictionaries.foodmeasure.data.FoodMeasure;
import ru.android.healthvector.presentation.core.fields.dialogs.add.core.AddValuePresenter;

@InjectViewState
public class AddFoodMeasurePresenter extends AddValuePresenter<FoodMeasure, AddFoodMeasureView> {
    @Inject
    FoodMeasureInteractor foodMeasureInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void add(@NonNull FoodMeasure item) {
        unsubscribeOnDestroy(foodMeasureInteractor.add(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::added, this::onUnexpectedError));
    }
}
