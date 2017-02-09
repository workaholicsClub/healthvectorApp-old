package ru.android.childdiary.app;

import android.app.Application;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.di.DaggerApplicationComponent;
import ru.android.childdiary.di.modules.ApplicationModule;

public class ChildDiaryApplication extends Application {
    @Accessors(prefix = "s")
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
