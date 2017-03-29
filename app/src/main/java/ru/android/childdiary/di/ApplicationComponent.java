package ru.android.childdiary.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.android.childdiary.di.modules.ApplicationModule;
import ru.android.childdiary.di.modules.DataModule;
import ru.android.childdiary.presentation.calendar.fragments.BaseCalendarPresenter;
import ru.android.childdiary.presentation.calendar.CalendarFragment;
import ru.android.childdiary.presentation.calendar.CalendarPresenter;
import ru.android.childdiary.presentation.development.DevelopmentDiaryPresenter;
import ru.android.childdiary.presentation.events.DiaperEventDetailActivity;
import ru.android.childdiary.presentation.events.DiaperEventDetailPresenter;
import ru.android.childdiary.presentation.events.FeedEventDetailActivity;
import ru.android.childdiary.presentation.events.FeedEventDetailPresenter;
import ru.android.childdiary.presentation.events.OtherEventDetailActivity;
import ru.android.childdiary.presentation.events.OtherEventDetailPresenter;
import ru.android.childdiary.presentation.events.PumpEventDetailActivity;
import ru.android.childdiary.presentation.events.PumpEventDetailPresenter;
import ru.android.childdiary.presentation.events.SleepEventDetailActivity;
import ru.android.childdiary.presentation.events.SleepEventDetailPresenter;
import ru.android.childdiary.presentation.exercises.ExercisesPresenter;
import ru.android.childdiary.presentation.help.HelpPresenter;
import ru.android.childdiary.presentation.main.MainActivity;
import ru.android.childdiary.presentation.main.MainPresenter;
import ru.android.childdiary.presentation.medical.MedicalDataPresenter;
import ru.android.childdiary.presentation.profile.edit.ProfileEditActivity;
import ru.android.childdiary.presentation.profile.edit.ProfileEditPresenter;
import ru.android.childdiary.presentation.profile.review.ProfileReviewActivity;
import ru.android.childdiary.presentation.profile.review.ProfileReviewPresenter;
import ru.android.childdiary.presentation.settings.SettingsFragment;
import ru.android.childdiary.presentation.settings.SettingsPresenter;
import ru.android.childdiary.presentation.splash.SplashActivity;
import ru.android.childdiary.presentation.splash.SplashPresenter;
import ru.android.childdiary.services.TimerService;

@Component(modules = {ApplicationModule.class, DataModule.class})
@Singleton
public interface ApplicationComponent {
    // presenters
    void inject(SplashPresenter presenter);

    void inject(MainPresenter presenter);

    void inject(ProfileEditPresenter presenter);

    void inject(ProfileReviewPresenter presenter);

    void inject(BaseCalendarPresenter presenter);

    void inject(DiaperEventDetailPresenter presenter);

    void inject(FeedEventDetailPresenter presenter);

    void inject(OtherEventDetailPresenter presenter);

    void inject(PumpEventDetailPresenter presenter);

    void inject(SleepEventDetailPresenter presenter);

    void inject(CalendarPresenter presenter);

    void inject(DevelopmentDiaryPresenter presenter);

    void inject(ExercisesPresenter presenter);

    void inject(MedicalDataPresenter presenter);

    void inject(SettingsPresenter presenter);

    void inject(HelpPresenter presenter);

    // activities
    void inject(SplashActivity activity);

    void inject(MainActivity activity);

    void inject(SettingsFragment activity);

    void inject(ProfileEditActivity activity);

    void inject(ProfileReviewActivity activity);

    void inject(DiaperEventDetailActivity activity);

    void inject(FeedEventDetailActivity activity);

    void inject(OtherEventDetailActivity activity);

    void inject(PumpEventDetailActivity activity);

    void inject(SleepEventDetailActivity activity);

    // fragments
    void inject(CalendarFragment fragment);

    // services
    void inject(TimerService service);
}
