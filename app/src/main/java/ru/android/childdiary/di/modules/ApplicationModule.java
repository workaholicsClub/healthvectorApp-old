package ru.android.childdiary.di.modules;

import android.content.Context;

import org.greenrobot.greendao.annotation.apihint.Internal;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.android.childdiary.app.ChildDiaryApplication;
import ru.android.childdiary.presentation.core.navigation.NavigationController;

@Module
public class ApplicationModule {
    private final ChildDiaryApplication mApplication;

    public ApplicationModule(ChildDiaryApplication application) {
        this.mApplication = application;
    }

    @Internal
    @Provides
    @Singleton
    Context provideContext() {
        return mApplication;
    }

    @Internal
    @Provides
    @Singleton
    public NavigationController provideNavigationController(Context context) {
        return new NavigationController(context);
    }
}
