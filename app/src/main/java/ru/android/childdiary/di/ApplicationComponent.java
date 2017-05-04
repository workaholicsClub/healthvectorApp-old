package ru.android.childdiary.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.android.childdiary.di.modules.ApplicationModule;
import ru.android.childdiary.di.modules.DataModule;
import ru.android.childdiary.presentation.calendar.CalendarFragment;
import ru.android.childdiary.presentation.calendar.CalendarPresenter;
import ru.android.childdiary.presentation.calendar.fragments.BaseCalendarPresenter;
import ru.android.childdiary.presentation.development.DevelopmentDiaryPresenter;
import ru.android.childdiary.presentation.events.DiaperEventDetailActivity;
import ru.android.childdiary.presentation.events.DiaperEventDetailPresenter;
import ru.android.childdiary.presentation.events.FeedEventDetailPresenter;
import ru.android.childdiary.presentation.events.FeedEventDetailActivity;
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
import ru.android.childdiary.presentation.medical.MedicalDataFragment;
import ru.android.childdiary.presentation.medical.MedicalDataPresenter;
import ru.android.childdiary.presentation.medical.add.medicines.AddDoctorVisitActivity;
import ru.android.childdiary.presentation.medical.add.medicines.AddDoctorVisitPresenter;
import ru.android.childdiary.presentation.medical.add.visits.AddMedicineTakingActivity;
import ru.android.childdiary.presentation.medical.add.visits.AddMedicineTakingPresenter;
import ru.android.childdiary.presentation.medical.edit.medicines.EditDoctorVisitActivity;
import ru.android.childdiary.presentation.medical.edit.medicines.EditDoctorVisitPresenter;
import ru.android.childdiary.presentation.medical.edit.visits.EditMedicineTakingActivity;
import ru.android.childdiary.presentation.medical.edit.visits.EditMedicineTakingPresenter;
import ru.android.childdiary.presentation.medical.fragments.medicines.MedicineTakingListPresenter;
import ru.android.childdiary.presentation.medical.fragments.visits.DoctorVisitsPresenter;
import ru.android.childdiary.presentation.profile.ProfileEditActivity;
import ru.android.childdiary.presentation.profile.ProfileEditPresenter;
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

    void inject(DoctorVisitsPresenter presenter);

    void inject(MedicineTakingListPresenter presenter);

    void inject(SettingsPresenter presenter);

    void inject(HelpPresenter presenter);

    void inject(AddDoctorVisitPresenter presenter);

    void inject(AddMedicineTakingPresenter presenter);

    void inject(EditDoctorVisitPresenter presenter);

    void inject(EditMedicineTakingPresenter presenter);

    // activities
    void inject(SplashActivity activity);

    void inject(MainActivity activity);

    void inject(SettingsFragment activity);

    void inject(ProfileEditActivity activity);

    void inject(DiaperEventDetailActivity activity);

    void inject(FeedEventDetailActivity activity);

    void inject(OtherEventDetailActivity activity);

    void inject(PumpEventDetailActivity activity);

    void inject(SleepEventDetailActivity activity);

    void inject(AddDoctorVisitActivity activity);

    void inject(AddMedicineTakingActivity activity);

    void inject(EditDoctorVisitActivity activity);

    void inject(EditMedicineTakingActivity activity);

    // fragments
    void inject(CalendarFragment fragment);

    void inject(MedicalDataFragment fragment);

    // services
    void inject(TimerService service);
}
