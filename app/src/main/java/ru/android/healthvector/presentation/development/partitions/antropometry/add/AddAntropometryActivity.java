package ru.android.healthvector.presentation.development.partitions.antropometry.add;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.OnClick;
import icepick.State;
import lombok.Getter;
import ru.android.healthvector.R;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.antropometry.data.Antropometry;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.core.bindings.RxFieldValueView;
import ru.android.healthvector.presentation.development.partitions.antropometry.core.AntropometryActivity;
import ru.android.healthvector.utils.ui.ResourcesUtils;

public class AddAntropometryActivity extends AntropometryActivity<AddAntropometryView>
        implements AddAntropometryView {
    @Getter
    @InjectPresenter
    AddAntropometryPresenter presenter;

    @State
    boolean isButtonDoneEnabled;

    public static Intent getIntent(Context context, @NonNull Child child, @NonNull Antropometry defaultAntropometry) {
        return new Intent(context, AddAntropometryActivity.class)
                .putExtra(ExtraConstants.EXTRA_CHILD, child)
                .putExtra(ExtraConstants.EXTRA_ITEM, defaultAntropometry);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buttonAdd.setVisibility(View.VISIBLE);

        unsubscribeOnDestroy(getPresenter().listenForDoneButtonUpdate(
                RxFieldValueView.valueChangeEvents(heightView),
                RxFieldValueView.valueChangeEvents(weightView)
        ));
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), isButtonDoneEnabled));
    }

    @OnClick(R.id.buttonAdd)
    void onButtonAddClick() {
        presenter.add(buildAntropometry());
        if (isButtonDoneEnabled) {
            hideKeyboardAndClearFocus();
        }
    }

    @Override
    public void added(@NonNull Antropometry antropometry) {
        finish();
    }

    @Override
    public void setButtonDoneEnabled(boolean enabled) {
        isButtonDoneEnabled = enabled;
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), isButtonDoneEnabled));
    }

    @Override
    protected void upsert(@NonNull Antropometry antropometry) {
        presenter.add(antropometry);
    }
}
