package ru.android.childdiary.presentation.splash;

import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseActivityView;

public interface SplashView extends BaseActivityView {
    void startApp(Child lastActiveChild);
}
