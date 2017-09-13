package ru.android.childdiary.presentation.dictionaries.medicines;

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
import ru.android.childdiary.domain.dictionaries.medicines.data.Medicine;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.dictionaries.core.BaseAddActivity;
import ru.android.childdiary.presentation.medical.adapters.core.StringFilteredAdapter;

public class MedicineAddActivity extends BaseAddActivity<Medicine, MedicineAddView> implements MedicineAddView {
    @Getter
    @InjectPresenter
    MedicineAddPresenter presenter;

    public static Intent getIntent(Context context, @Nullable Sex sex, @Nullable String defaultText) {
        return new Intent(context, MedicineAddActivity.class)
                .putExtra(ExtraConstants.EXTRA_SEX, sex)
                .putExtra(ExtraConstants.EXTRA_TEXT, defaultText);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.add_medicine);
    }

    @Override
    @LayoutRes
    protected int getContentLayoutResourceId() {
        return R.layout.activity_add_medicine;
    }

    @Override
    protected Medicine buildItem() {
        String text = autoCompleteView.getText();
        return Medicine.builder().nameUser(text).build();
    }

    @Override
    protected FilteredArrayAdapter<String> createFilteredAdapter(@NonNull List<Medicine> items) {
        return new StringFilteredAdapter(this, Observable.fromIterable(items)
                .filter(item -> !TextUtils.isEmpty(item.getName()))
                .map(Medicine::getName)
                .toList()
                .blockingGet(), StringFilteredAdapter.FilterType.CONTAINS);
    }
}
