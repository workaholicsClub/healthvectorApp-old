package ru.android.childdiary.di.modules;

import android.content.Context;

import com.squareup.otto.Bus;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import proxypref.ProxyPreferences;
import ru.android.childdiary.app.ChildDiaryPreferences;

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
    public ChildDiaryPreferences providePreferences(Context context) {
        return ProxyPreferences.build(ChildDiaryPreferences.class,
                context.getSharedPreferences("diary", Context.MODE_PRIVATE));
    }

    @Provides
    @Singleton
    @Named(DATE_FORMATTER)
    public DateTimeFormatter provideDateFormat() {
        return DateTimeFormat.forPattern("dd.MM.yyyy");
    }

    @Provides
    @Singleton
    @Named(TIME_FORMATTER)
    public DateTimeFormatter provideTimeFormat() {
        return DateTimeFormat.forPattern("HH:mm");
    }

    @Provides
    @Singleton
    public Bus provideBus() {
        return new Bus();
    }
}
