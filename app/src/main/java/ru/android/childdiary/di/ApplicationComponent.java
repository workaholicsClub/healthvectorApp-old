package ru.android.childdiary.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.android.childdiary.di.modules.ApplicationModule;
import ru.android.childdiary.di.modules.AvailabilityModule;
import ru.android.childdiary.di.modules.CloudModule;
import ru.android.childdiary.di.modules.DbModule;
import ru.android.childdiary.di.modules.NetworkModule;
import ru.android.childdiary.presentation.calendar.CalendarFragment;
import ru.android.childdiary.presentation.calendar.CalendarPresenter;
import ru.android.childdiary.presentation.calendar.partitions.BaseCalendarPresenter;
import ru.android.childdiary.presentation.chart.antropometry.AntropometryChartActivity;
import ru.android.childdiary.presentation.chart.antropometry.pages.core.AntropometryChartPresenter;
import ru.android.childdiary.presentation.chart.testing.TestChartActivity;
import ru.android.childdiary.presentation.chart.testing.pages.core.DomanChartPresenter;
import ru.android.childdiary.presentation.cloud.CloudInitialActivity;
import ru.android.childdiary.presentation.cloud.CloudInitialPresenter;
import ru.android.childdiary.presentation.cloud.CloudOperationActivity;
import ru.android.childdiary.presentation.cloud.CloudOperationPresenter;
import ru.android.childdiary.presentation.core.images.ImagePickerPresenter;
import ru.android.childdiary.presentation.development.DevelopmentDiaryFragment;
import ru.android.childdiary.presentation.development.DevelopmentDiaryPresenter;
import ru.android.childdiary.presentation.development.partitions.achievements.ConcreteAchievementsPresenter;
import ru.android.childdiary.presentation.development.partitions.achievements.add.AddConcreteAchievementActivity;
import ru.android.childdiary.presentation.development.partitions.achievements.add.AddConcreteAchievementPresenter;
import ru.android.childdiary.presentation.development.partitions.achievements.edit.EditConcreteAchievementActivity;
import ru.android.childdiary.presentation.development.partitions.achievements.edit.EditConcreteAchievementPresenter;
import ru.android.childdiary.presentation.development.partitions.antropometry.AntropometryListPresenter;
import ru.android.childdiary.presentation.development.partitions.antropometry.add.AddAntropometryActivity;
import ru.android.childdiary.presentation.development.partitions.antropometry.add.AddAntropometryPresenter;
import ru.android.childdiary.presentation.development.partitions.antropometry.edit.EditAntropometryActivity;
import ru.android.childdiary.presentation.development.partitions.antropometry.edit.EditAntropometryPresenter;
import ru.android.childdiary.presentation.development.partitions.testing.TestResultsPresenter;
import ru.android.childdiary.presentation.events.DiaperEventDetailActivity;
import ru.android.childdiary.presentation.events.DiaperEventDetailPresenter;
import ru.android.childdiary.presentation.events.DoctorVisitEventDetailActivity;
import ru.android.childdiary.presentation.events.DoctorVisitEventDetailPresenter;
import ru.android.childdiary.presentation.events.ExerciseEventDetailActivity;
import ru.android.childdiary.presentation.events.ExerciseEventDetailPresenter;
import ru.android.childdiary.presentation.events.FeedEventDetailActivity;
import ru.android.childdiary.presentation.events.FeedEventDetailPresenter;
import ru.android.childdiary.presentation.events.MedicineTakingEventDetailActivity;
import ru.android.childdiary.presentation.events.MedicineTakingEventDetailPresenter;
import ru.android.childdiary.presentation.events.OtherEventDetailActivity;
import ru.android.childdiary.presentation.events.OtherEventDetailPresenter;
import ru.android.childdiary.presentation.events.PumpEventDetailActivity;
import ru.android.childdiary.presentation.events.PumpEventDetailPresenter;
import ru.android.childdiary.presentation.events.SleepEventDetailActivity;
import ru.android.childdiary.presentation.events.SleepEventDetailPresenter;
import ru.android.childdiary.presentation.exercises.AddConcreteExerciseActivity;
import ru.android.childdiary.presentation.exercises.AddConcreteExercisePresenter;
import ru.android.childdiary.presentation.exercises.ExerciseDetailActivity;
import ru.android.childdiary.presentation.exercises.ExerciseDetailPresenter;
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
import ru.android.childdiary.presentation.medical.filter.medicines.MedicineTakingFilterPresenter;
import ru.android.childdiary.presentation.medical.filter.visits.DoctorVisitFilterPresenter;
import ru.android.childdiary.presentation.medical.partitions.medicines.MedicineTakingListPresenter;
import ru.android.childdiary.presentation.medical.partitions.visits.DoctorVisitsPresenter;
import ru.android.childdiary.presentation.medical.pickers.medicines.MedicineAddActivity;
import ru.android.childdiary.presentation.medical.pickers.medicines.MedicineAddPresenter;
import ru.android.childdiary.presentation.medical.pickers.medicines.MedicinePickerActivity;
import ru.android.childdiary.presentation.medical.pickers.medicines.MedicinePickerPresenter;
import ru.android.childdiary.presentation.medical.pickers.visits.DoctorAddActivity;
import ru.android.childdiary.presentation.medical.pickers.visits.DoctorAddPresenter;
import ru.android.childdiary.presentation.medical.pickers.visits.DoctorPickerActivity;
import ru.android.childdiary.presentation.medical.pickers.visits.DoctorPickerPresenter;
import ru.android.childdiary.presentation.profile.ProfileEditActivity;
import ru.android.childdiary.presentation.profile.ProfileEditPresenter;
import ru.android.childdiary.presentation.settings.SettingsFragment;
import ru.android.childdiary.presentation.settings.SettingsPresenter;
import ru.android.childdiary.presentation.splash.SplashActivity;
import ru.android.childdiary.presentation.splash.SplashPresenter;
import ru.android.childdiary.presentation.testing.TestResultActivity;
import ru.android.childdiary.presentation.testing.TestResultPresenter;
import ru.android.childdiary.presentation.testing.TestingActivity;
import ru.android.childdiary.presentation.testing.TestingPresenter;
import ru.android.childdiary.presentation.testing.dialogs.TestParametersPresenter;
import ru.android.childdiary.services.AccountService;
import ru.android.childdiary.services.CloudService;
import ru.android.childdiary.services.TimerService;
import ru.android.childdiary.services.UpdateDataService;

@Component(modules = {ApplicationModule.class,
        DbModule.class,
        NetworkModule.class,
        AvailabilityModule.class,
        CloudModule.class})
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

    void inject(DoctorPickerPresenter presenter);

    void inject(DoctorAddPresenter presenter);

    void inject(MedicinePickerPresenter presenter);

    void inject(MedicineAddPresenter presenter);

    void inject(DoctorVisitEventDetailPresenter presenter);

    void inject(MedicineTakingEventDetailPresenter presenter);

    void inject(ExerciseEventDetailPresenter presenter);

    void inject(ImagePickerPresenter presenter);

    void inject(DoctorVisitFilterPresenter presenter);

    void inject(MedicineTakingFilterPresenter presenter);

    void inject(ConcreteAchievementsPresenter presenter);

    void inject(AntropometryListPresenter presenter);

    void inject(TestResultsPresenter presenter);

    void inject(ExerciseDetailPresenter presenter);

    void inject(AddConcreteExercisePresenter presenter);

    void inject(CloudInitialPresenter presenter);

    void inject(CloudOperationPresenter presenter);

    void inject(TestingPresenter presenter);

    void inject(TestResultPresenter presenter);

    void inject(TestParametersPresenter presenter);

    void inject(DomanChartPresenter presenter);

    void inject(AntropometryChartPresenter presenter);

    void inject(AddAntropometryPresenter presenter);

    void inject(EditAntropometryPresenter presenter);

    void inject(AddConcreteAchievementPresenter presenter);

    void inject(EditConcreteAchievementPresenter presenter);

    // activities
    void inject(SplashActivity activity);

    void inject(MainActivity activity);

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

    void inject(DoctorPickerActivity activity);

    void inject(DoctorAddActivity activity);

    void inject(MedicinePickerActivity activity);

    void inject(MedicineAddActivity activity);

    void inject(DoctorVisitEventDetailActivity activity);

    void inject(MedicineTakingEventDetailActivity activity);

    void inject(ExerciseEventDetailActivity activity);

    void inject(ExerciseDetailActivity activity);

    void inject(AddConcreteExerciseActivity activity);

    void inject(CloudInitialActivity activity);

    void inject(CloudOperationActivity activity);

    void inject(TestingActivity activity);

    void inject(TestResultActivity activity);

    void inject(TestChartActivity activity);

    void inject(AntropometryChartActivity activity);

    void inject(AddAntropometryActivity activity);

    void inject(EditAntropometryActivity activity);

    void inject(AddConcreteAchievementActivity activity);

    void inject(EditConcreteAchievementActivity activity);

    // fragments
    void inject(CalendarFragment fragment);

    void inject(MedicalDataFragment fragment);

    void inject(DevelopmentDiaryFragment fragment);

    void inject(SettingsFragment fragment);

    // services
    void inject(TimerService service);

    void inject(UpdateDataService service);

    void inject(CloudService service);

    void inject(AccountService service);
}
