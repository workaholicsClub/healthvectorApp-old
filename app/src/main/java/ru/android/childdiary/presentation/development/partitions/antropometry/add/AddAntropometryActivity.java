package ru.android.childdiary.presentation.development.partitions.antropometry.add;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import lombok.Getter;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.development.partitions.antropometry.core.AntropometryActivity;

public class AddAntropometryActivity extends AntropometryActivity<AddAntropometryView>
        implements AddAntropometryView, DatePickerDialog.OnDateSetListener {
    @Getter
    @InjectPresenter
    AddAntropometryPresenter presenter;

    public static Intent getIntent(Context context, @NonNull Child child) {
        return new Intent(context, AddAntropometryActivity.class)
                .putExtra(ExtraConstants.EXTRA_CHILD, child);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buttonAdd.setVisibility(View.VISIBLE);
    }
}
