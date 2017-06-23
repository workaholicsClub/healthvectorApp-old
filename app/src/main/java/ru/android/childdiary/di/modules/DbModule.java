package ru.android.childdiary.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.BuildConfig;
import ru.android.childdiary.data.db.CustomDatabaseSource;
import ru.android.childdiary.data.db.DbUtils;

@Module
public class DbModule {
    @Provides
    @Singleton
    public CustomDatabaseSource getDatabaseSource(Context appContext) {
        return DbUtils.getDatabaseSource(appContext, BuildConfig.DB_NAME, BuildConfig.DB_VERSION);
    }

    @Provides
    @Singleton
    public ReactiveEntityStore<Persistable> provideDataStore(CustomDatabaseSource source) {
        return DbUtils.getDataStore(source);
    }
}
