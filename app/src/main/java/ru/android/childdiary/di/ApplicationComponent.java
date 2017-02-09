package ru.android.childdiary.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import ru.android.childdiary.di.modules.ApplicationModule;
import ru.android.childdiary.presentation.core.NavigationController;

@Component(modules = {ApplicationModule.class})
@Singleton
public interface ApplicationComponent {
    Context provideContext();

    NavigationController provideNavigationController();
}
