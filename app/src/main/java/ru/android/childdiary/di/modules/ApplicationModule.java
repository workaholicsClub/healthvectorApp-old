package ru.android.childdiary.di.modules;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import proxypref.ProxyPreferences;
import ru.android.childdiary.app.ChildDiaryPreferences;

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

    @Provides
    @Singleton
    public ChildDiaryPreferences providePreferences(Context context) {
        return ProxyPreferences.build(ChildDiaryPreferences.class,
                context.getSharedPreferences("diary", Context.MODE_PRIVATE));
    }

    @Provides
    @Singleton
    public EventBus provideBus() {
        return EventBus.getDefault();
    }
}
