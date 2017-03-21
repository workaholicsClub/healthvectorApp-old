package ru.android.childdiary.app;

import android.app.Application;
import android.content.Intent;

import net.danlew.android.joda.JodaTimeAndroid;

import lombok.Getter;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.di.DaggerApplicationComponent;
import ru.android.childdiary.di.modules.ApplicationModule;
import ru.android.childdiary.services.TimerService;
import ru.android.childdiary.utils.log.LogSystem;

public class ChildDiaryApplication extends Application {
    @Getter
    private static ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // TODO: setup custom uncaught exception handler; log exception to file; for debug purposes
        // TODO: log to external files path if available

        LogSystem.initLogger(this);

        JodaTimeAndroid.init(this);

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        startService(new Intent(this, TimerService.class));
    }
}
