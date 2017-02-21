package ru.android.childdiary.di.modules;

import android.content.Context;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    public static final String DATE_FORMATTER = "DATE_FORMATTER";
    public static final String TIME_FORMATTER = "TIME_FORMATTER";

    private final Context appContext;

    public ApplicationModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return appContext;
    }

    @Provides
    @Singleton
    @Named(DATE_FORMATTER)
    public DateTimeFormatter provideDateFormat() {
        return DateTimeFormat.longDate();
    }

    @Provides
    @Singleton
    @Named(TIME_FORMATTER)
    public DateTimeFormatter provideTimeFormat() {
        return DateTimeFormat.shortTime();
    }
}
