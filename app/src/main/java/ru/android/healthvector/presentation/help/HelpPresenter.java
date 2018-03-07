package ru.android.healthvector.presentation.help;

import com.arellomobile.mvp.InjectViewState;

import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.presentation.core.AppPartitionPresenter;

@InjectViewState
public class HelpPresenter extends AppPartitionPresenter<HelpView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
