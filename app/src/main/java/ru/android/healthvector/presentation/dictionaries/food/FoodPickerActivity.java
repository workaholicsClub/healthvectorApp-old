package ru.android.healthvector.presentation.dictionaries.food;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.presenter.InjectPresenter;

import lombok.Getter;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.dictionaries.food.data.Food;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.healthvector.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.healthvector.presentation.dictionaries.core.BaseAddActivity;
import ru.android.healthvector.presentation.dictionaries.core.BaseAddView;
import ru.android.healthvector.presentation.dictionaries.core.BasePickerActivity;
import ru.android.healthvector.presentation.dictionaries.food.adapters.FoodActionListener;
import ru.android.healthvector.presentation.dictionaries.food.adapters.FoodAdapter;
import ru.android.healthvector.utils.ui.ThemeUtils;

public class FoodPickerActivity extends BasePickerActivity<Food, FoodPickerView>
        implements FoodPickerView, FoodActionListener {
    @Getter
    @InjectPresenter
    FoodPickerPresenter presenter;

    public static Intent getIntent(Context context, @Nullable Sex sex, boolean pick) {
        return new Intent(context, FoodPickerActivity.class)
                .putExtra(ExtraConstants.EXTRA_SEX, sex)
                .putExtra(ExtraConstants.EXTRA_PICK, pick);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.food);
    }

    @Override
    public void deletionRestricted() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.deletion_restricted_food)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    @Override
    protected Class<? extends BaseAddActivity<Food, ? extends BaseAddView<Food>>> getAddActivityClass() {
        return FoodAddActivity.class;
    }

    @Override
    protected BaseRecyclerViewAdapter<Food, ? extends BaseRecyclerViewHolder<Food>> createAdapter() {
        return new FoodAdapter(this, this, this);
    }

    @Override
    protected int getMaxLength() {
        return maxLengthNameMedium;
    }

    @Override
    protected String getIntentionText() {
        return getString(R.string.add_food);
    }

    @Override
    protected int getDeleteConfirmationStringId() {
        return R.string.delete_food;
    }
}
