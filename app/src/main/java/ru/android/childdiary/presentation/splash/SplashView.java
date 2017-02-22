package ru.android.childdiary.presentation.splash;

import android.support.annotation.Nullable;

import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.core.BaseActivityView;

public interface SplashView extends BaseActivityView {
    void startApp(@Nullable Sex sex);
}
