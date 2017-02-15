package ru.android.childdiary.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.android.childdiary.data.repositories.child.ChildDataRepository;
import ru.android.childdiary.data.repositories.child.AntropometryDataRepository;
import ru.android.childdiary.domain.interactors.child.AntropometryInteractor;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;

@Module
public class DomainModule {
    @Provides
    public ChildInteractor provideChildInteractor(ChildDataRepository childRepository) {
        return new ChildInteractor(childRepository);
    }

    @Provides
    public AntropometryInteractor provideAntropometryInteractor(AntropometryDataRepository antropometryRepository) {
        return new AntropometryInteractor(antropometryRepository);
    }
}
