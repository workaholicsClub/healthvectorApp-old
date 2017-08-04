package ru.android.childdiary.presentation.settings.daylength;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BasePresenter;

public class DayLengthPresenter extends BasePresenter<DayLengthView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
