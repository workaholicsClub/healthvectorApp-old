package ru.android.childdiary.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.repositories.child.AntropometryDataRepository;
import ru.android.childdiary.data.repositories.child.AntropometryDbService;
import ru.android.childdiary.data.repositories.child.ChildDataRepository;
import ru.android.childdiary.data.repositories.child.ChildDbService;
import ru.android.childdiary.di.modules.ApplicationModule;
import ru.android.childdiary.di.modules.DataModule;
import ru.android.childdiary.di.modules.DomainModule;
import ru.android.childdiary.domain.interactors.child.AntropometryInteractor;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.presentation.core.navigation.NavigationController;
import ru.android.childdiary.presentation.main.MainActivity;
import ru.android.childdiary.presentation.main.MainPresenter;
import ru.android.childdiary.presentation.profile.edit.ProfileEditActivity;
import ru.android.childdiary.presentation.profile.edit.ProfileEditPresenter;
import ru.android.childdiary.presentation.profile.review.ProfileReviewActivity;
import ru.android.childdiary.presentation.profile.review.ProfileReviewPresenter;
import ru.android.childdiary.presentation.settings.SettingsActivity;
import ru.android.childdiary.presentation.settings.SettingsPresenter;
import ru.android.childdiary.presentation.splash.SplashActivity;
import ru.android.childdiary.presentation.splash.SplashPresenter;

@Component(modules = {ApplicationModule.class, DomainModule.class, DataModule.class})
@Singleton
public interface ApplicationComponent {
    // application module
    Context provideContext();

    NavigationController provideNavigationController();

    // domain module
    ChildInteractor provideChildInteractor();

    AntropometryInteractor provideAntropometryInteractor();

    // data module
    ReactiveEntityStore<Persistable> provideDataStore();

    ChildDbService provideChildDbService();

    AntropometryDbService provideAntropometryDbService();

    ChildDataRepository provideChildDataRepository();

    AntropometryDataRepository provideAntropometryDataRepository();

    // presenters
    void inject(SplashPresenter presenter);

    void inject(MainPresenter presenter);

    void inject(SettingsPresenter presenter);

    void inject(ProfileEditPresenter presenter);

    void inject(ProfileReviewPresenter presenter);

    // activities
    void inject(SplashActivity activity);

    void inject(MainActivity activity);

    void inject(SettingsActivity activity);

    void inject(ProfileEditActivity activity);

    void inject(ProfileReviewActivity activity);

    // interactors
    void inject(ChildInteractor interactor);

    void inject(AntropometryInteractor interactor);
}
