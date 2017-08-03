package ru.android.childdiary.presentation.pickers.food;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.CalendarInteractor;
import ru.android.childdiary.domain.interactors.calendar.events.core.Food;
import ru.android.childdiary.presentation.pickers.core.BasePickerPresenter;
import ru.android.childdiary.utils.strings.StringUtils;

@InjectViewState
public class FoodPickerPresenter extends BasePickerPresenter<Food, FoodPickerView> {
    @Inject
    CalendarInteractor calendarInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected Observable<List<Food>> getAllItemsLoader() {
        return calendarInteractor.getFoodList();
    }

    @Override
    protected boolean filter(@NonNull Food item, @Nullable String filter) {
        return StringUtils.contains(item.getName(), filter, true);
    }

    @Override
    protected Observable<Food> deleteItemLoader(@NonNull Food item) {
        return null; // TODO calendarInteractor.deleteMedicine(item);
    }
}
