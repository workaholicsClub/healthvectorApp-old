package ru.android.childdiary.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.repositories.child.AntropometryDataRepository;
import ru.android.childdiary.data.repositories.child.AntropometryDbService;
import ru.android.childdiary.data.repositories.child.ChildDataRepository;
import ru.android.childdiary.data.repositories.child.ChildDbService;

@Module
public class DataModule {
    @Provides
    @Singleton
    public ReactiveEntityStore<Persistable> provideDataStore(Context appContext) {
        return DbUtils.getDataStore(appContext);
    }

    @Provides
    @Singleton
    public ChildDataRepository provideChildDataRepository(ChildDbService dbService) {
        return new ChildDataRepository(dbService);
    }

    @Provides
    @Singleton
    public AntropometryDataRepository provideAntropometryDataRepository(AntropometryDbService dbService) {
        return new AntropometryDataRepository(dbService);
    }
}
