package ru.android.childdiary.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.presentation.core.navigation.NavigationController;
import ru.android.childdiary.utils.db.DbUtils;

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

    @Provides
    @Singleton
    public ReactiveEntityStore<Persistable> provideDataStore() {
        return DbUtils.getDataStore(appContext);
    }
}
