package ru.android.childdiary.app;

import android.app.Application;

import lombok.Getter;
import lombok.Setter;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.di.DaggerApplicationComponent;
import ru.android.childdiary.di.modules.ApplicationModule;

public class ChildDiaryApplication extends Application {
    @Getter
    @Setter
    private static ApplicationComponent sApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        sApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }
}
