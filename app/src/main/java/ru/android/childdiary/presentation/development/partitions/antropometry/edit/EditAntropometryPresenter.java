package ru.android.childdiary.presentation.development.partitions.antropometry.edit;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.development.partitions.antropometry.core.AntropometryPresenter;

@InjectViewState
public class EditAntropometryPresenter extends AntropometryPresenter<EditAntropometryView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
