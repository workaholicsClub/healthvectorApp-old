package ru.android.healthvector.di.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.f2prateek.rx.preferences2.RxSharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.android.healthvector.data.services.ScheduleHelper;
import ru.android.healthvector.data.services.ServiceController;

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
    public ServiceController provideServiceController() {
        return serviceController;
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
