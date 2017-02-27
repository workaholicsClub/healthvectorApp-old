package ru.android.childdiary.presentation.settings;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BaseMvpActivity;

public class SettingsActivity extends BaseMvpActivity<SettingsPresenter> implements SettingsView {
    @InjectPresenter
    SettingsPresenter presenter;

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
