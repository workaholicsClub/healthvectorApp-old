package ru.android.childdiary.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;

@Module
public class DataModule {
    @Provides
    @Singleton
    public ReactiveEntityStore<Persistable> provideDataStore(Context appContext) {
        return DbUtils.getDataStore(appContext);
    }
}
