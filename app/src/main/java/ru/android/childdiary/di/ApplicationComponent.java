package ru.android.childdiary.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.di.modules.ApplicationModule;
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

@Component(modules = {ApplicationModule.class})
@Singleton
public interface ApplicationComponent {
    Context provideContext();

    NavigationController provideNavigationController();

    ReactiveEntityStore<Persistable> provideDataStore();

    void inject(SplashPresenter presenter);

    void inject(MainPresenter presenter);

    void inject(SettingsPresenter presenter);

    void inject(ProfileEditPresenter presenter);

    void inject(ProfileReviewPresenter presenter);

    void inject(SplashActivity activity);

    void inject(MainActivity activity);

    void inject(SettingsActivity activity);

    void inject(ProfileEditActivity activity);

    void inject(ProfileReviewActivity activity);
}
