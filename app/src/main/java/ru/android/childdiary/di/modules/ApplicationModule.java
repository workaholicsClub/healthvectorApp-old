package ru.android.childdiary.di.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.f2prateek.rx.preferences2.RxSharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.android.childdiary.data.services.ServiceController;
import ru.android.childdiary.data.services.ScheduleHelper;

@Module
public class ApplicationModule {
    private final Context appContext;
    private final ServiceController serviceController;
    private final ScheduleHelper scheduleHelper;

    public ApplicationModule(Context appContext,
                             ServiceController serviceController,
                             ScheduleHelper scheduleHelper) {
        this.appContext = appContext;
        this.serviceController = serviceController;
        this.scheduleHelper = scheduleHelper;
    }

    @Provides
    @Singleton
    public ScheduleHelper provideScheduleHelper() {
        return scheduleHelper;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return appContext;
    }

    @Provides
    @Singleton
    public RxSharedPreferences providePreferences(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);
        return rxPreferences;
    }
}
