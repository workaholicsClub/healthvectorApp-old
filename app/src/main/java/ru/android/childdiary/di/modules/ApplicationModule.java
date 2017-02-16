package ru.android.childdiary.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final Context appContext;

    public ApplicationModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return appContext;
    }
}
