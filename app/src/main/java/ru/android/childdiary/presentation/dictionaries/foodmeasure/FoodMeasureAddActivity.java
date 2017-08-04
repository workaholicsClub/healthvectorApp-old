package ru.android.childdiary.presentation.dictionaries.foodmeasure;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.tokenautocomplete.FilteredArrayAdapter;

import java.util.List;

import io.reactivex.Observable;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.dictionaries.foodmeasure.data.FoodMeasure;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.dictionaries.core.BaseAddActivity;
import ru.android.childdiary.presentation.medical.adapters.core.StringFilteredAdapter;

public class FoodMeasureAddActivity extends BaseAddActivity<FoodMeasure, FoodMeasureAddView> implements FoodMeasureAddView {
    @Getter
    @InjectPresenter
    FoodMeasureAddPresenter presenter;

    public static Intent getIntent(Context context, @Nullable Sex sex) {
        return new Intent(context, FoodMeasureAddActivity.class)
                .putExtra(ExtraConstants.EXTRA_SEX, sex);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.add_measure_unit);
    }

    @Override
    @LayoutRes
    protected int getContentLayoutResourceId() {
        return R.layout.activity_add_food_measure;
    }

    @Override
    protected FoodMeasure buildItem() {
        String text = autoCompleteView.getText();
        return FoodMeasure.builder().name(text).build();
    }

    @Override
    protected FilteredArrayAdapter<String> createFilteredAdapter(@NonNull List<FoodMeasure> items) {
        return new StringFilteredAdapter(this, Observable.fromIterable(items)
                .filter(item -> !TextUtils.isEmpty(item.getName()))
                .map(FoodMeasure::getName)
                .toList()
                .blockingGet(), StringFilteredAdapter.FilterType.CONTAINS);
    }
}
