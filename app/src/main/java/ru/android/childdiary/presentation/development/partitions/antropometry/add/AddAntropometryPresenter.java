package ru.android.childdiary.presentation.development.partitions.antropometry.add;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.development.partitions.antropometry.core.AntropometryPresenter;

@InjectViewState
public class AddAntropometryPresenter extends AntropometryPresenter<AddAntropometryView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
