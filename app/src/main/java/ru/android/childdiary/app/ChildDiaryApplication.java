package ru.android.childdiary.app;

import android.app.Application;

import lombok.Getter;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.di.DaggerApplicationComponent;
import ru.android.childdiary.di.modules.ApplicationModule;
import ru.android.childdiary.utils.log.LogSystem;

public class ChildDiaryApplication extends Application {
    @Getter
    private static ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        LogSystem.initLogger(this);

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }
}
