package ru.android.childdiary.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.android.childdiary.presentation.core.navigation.NavigationController;

@Module
public class ApplicationModule {
    private final Context appContext;

    public ApplicationModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return appContext;
    }

    @Provides
    @Singleton
    public NavigationController provideNavigationController(Context appContext) {
        return new NavigationController(appContext);
    }
}
