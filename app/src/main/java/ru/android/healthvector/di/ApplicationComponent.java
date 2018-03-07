package ru.android.healthvector.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.android.healthvector.di.modules.ApplicationModule;
import ru.android.healthvector.di.modules.AvailabilityModule;
import ru.android.healthvector.di.modules.CloudModule;
import ru.android.healthvector.di.modules.DbModule;
import ru.android.healthvector.di.modules.NetworkModule;
import ru.android.healthvector.presentation.calendar.CalendarFragment;
import ru.android.healthvector.presentation.calendar.CalendarPresenter;
import ru.android.healthvector.presentation.calendar.partitions.BaseCalendarPresenter;
import ru.android.healthvector.presentation.chart.antropometry.AntropometryChartActivity;
import ru.android.healthvector.presentation.chart.antropometry.pages.core.AntropometryChartPresenter;
import ru.android.healthvector.presentation.chart.testing.TestChartActivity;
import ru.android.healthvector.presentation.chart.testing.pages.core.DomanChartPresenter;
import ru.android.healthvector.presentation.cloud.CloudInitialActivity;
import ru.android.healthvector.presentation.cloud.CloudInitialPresenter;
import ru.android.healthvector.presentation.cloud.CloudOperationActivity;
import ru.android.healthvector.presentation.cloud.CloudOperationPresenter;
import ru.android.healthvector.presentation.core.fields.dialogs.add.food.AddFoodPresenter;
import ru.android.healthvector.presentation.core.fields.dialogs.add.foodmeasure.AddFoodMeasurePresenter;
import ru.android.healthvector.presentation.core.images.ImagePickerPresenter;
import ru.android.healthvector.presentation.development.DevelopmentDiaryFragment;
import ru.android.healthvector.presentation.development.DevelopmentDiaryPresenter;
import ru.android.healthvector.presentation.development.partitions.achievements.ConcreteAchievementsPresenter;
import ru.android.healthvector.presentation.development.partitions.achievements.add.AddConcreteAchievementActivity;
import ru.android.healthvector.presentation.development.partitions.achievements.add.AddConcreteAchievementPresenter;
import ru.android.healthvector.presentation.development.partitions.achievements.edit.EditConcreteAchievementActivity;
import ru.android.healthvector.presentation.development.partitions.achievements.edit.EditConcreteAchievementPresenter;
import ru.android.healthvector.presentation.development.partitions.antropometry.AntropometryListPresenter;
import ru.android.healthvector.presentation.development.partitions.antropometry.add.AddAntropometryActivity;
import ru.android.healthvector.presentation.development.partitions.antropometry.add.AddAntropometryPresenter;
import ru.android.healthvector.presentation.development.partitions.antropometry.edit.EditAntropometryActivity;
import ru.android.healthvector.presentation.development.partitions.antropometry.edit.EditAntropometryPresenter;
import ru.android.healthvector.presentation.development.partitions.testing.TestResultsPresenter;
import ru.android.healthvector.presentation.dictionaries.achievements.AchievementAddActivity;
import ru.android.healthvector.presentation.dictionaries.achievements.AchievementAddPresenter;
import ru.android.healthvector.presentation.dictionaries.achievements.AchievementPickerActivity;
import ru.android.healthvector.presentation.dictionaries.achievements.AchievementPickerPresenter;
import ru.android.healthvector.presentation.dictionaries.doctors.DoctorAddActivity;
import ru.android.healthvector.presentation.dictionaries.doctors.DoctorAddPresenter;
import ru.android.healthvector.presentation.dictionaries.doctors.DoctorPickerActivity;
import ru.android.healthvector.presentation.dictionaries.doctors.DoctorPickerPresenter;
import ru.android.healthvector.presentation.dictionaries.food.FoodAddActivity;
import ru.android.healthvector.presentation.dictionaries.food.FoodAddPresenter;
import ru.android.healthvector.presentation.dictionaries.food.FoodPickerActivity;
import ru.android.healthvector.presentation.dictionaries.food.FoodPickerPresenter;
import ru.android.healthvector.presentation.dictionaries.foodmeasure.FoodMeasureAddActivity;
import ru.android.healthvector.presentation.dictionaries.foodmeasure.FoodMeasureAddPresenter;
import ru.android.healthvector.presentation.dictionaries.foodmeasure.FoodMeasurePickerActivity;
import ru.android.healthvector.presentation.dictionaries.foodmeasure.FoodMeasurePickerPresenter;
import ru.android.healthvector.presentation.dictionaries.medicinemeasure.MedicineMeasureAddActivity;
import ru.android.healthvector.presentation.dictionaries.medicinemeasure.MedicineMeasureAddPresenter;
import ru.android.healthvector.presentation.dictionaries.medicinemeasure.MedicineMeasurePickerActivity;
import ru.android.healthvector.presentation.dictionaries.medicinemeasure.MedicineMeasurePickerPresenter;
import ru.android.healthvector.presentation.dictionaries.medicines.MedicineAddActivity;
import ru.android.healthvector.presentation.dictionaries.medicines.MedicineAddPresenter;
import ru.android.healthvector.presentation.dictionaries.medicines.MedicinePickerActivity;
import ru.android.healthvector.presentation.dictionaries.medicines.MedicinePickerPresenter;
import ru.android.healthvector.presentation.events.DiaperEventDetailActivity;
import ru.android.healthvector.presentation.events.DiaperEventDetailPresenter;
import ru.android.healthvector.presentation.events.DoctorVisitEventDetailActivity;
import ru.android.healthvector.presentation.events.DoctorVisitEventDetailPresenter;
import ru.android.healthvector.presentation.events.ExerciseEventDetailActivity;
import ru.android.healthvector.presentation.events.ExerciseEventDetailPresenter;
import ru.android.healthvector.presentation.events.FeedEventDetailActivity;
import ru.android.healthvector.presentation.events.FeedEventDetailPresenter;
import ru.android.healthvector.presentation.events.MedicineTakingEventDetailActivity;
import ru.android.healthvector.presentation.events.MedicineTakingEventDetailPresenter;
import ru.android.healthvector.presentation.events.OtherEventDetailActivity;
import ru.android.healthvector.presentation.events.OtherEventDetailPresenter;
import ru.android.healthvector.presentation.events.PumpEventDetailActivity;
import ru.android.healthvector.presentation.events.PumpEventDetailPresenter;
import ru.android.healthvector.presentation.events.SleepEventDetailActivity;
import ru.android.healthvector.presentation.events.SleepEventDetailPresenter;
import ru.android.healthvector.presentation.exercises.AddConcreteExerciseActivity;
import ru.android.healthvector.presentation.exercises.AddConcreteExercisePresenter;
import ru.android.healthvector.presentation.exercises.ExerciseDetailActivity;
import ru.android.healthvector.presentation.exercises.ExerciseDetailPresenter;
import ru.android.healthvector.presentation.exercises.ExercisesPresenter;
import ru.android.healthvector.presentation.help.HelpPresenter;
import ru.android.healthvector.presentation.main.MainActivity;
import ru.android.healthvector.presentation.main.MainPresenter;
import ru.android.healthvector.presentation.medical.MedicalDataFragment;
import ru.android.healthvector.presentation.medical.MedicalDataPresenter;
import ru.android.healthvector.presentation.medical.add.medicines.AddDoctorVisitActivity;
import ru.android.healthvector.presentation.medical.add.medicines.AddDoctorVisitPresenter;
import ru.android.healthvector.presentation.medical.add.visits.AddMedicineTakingActivity;
import ru.android.healthvector.presentation.medical.add.visits.AddMedicineTakingPresenter;
import ru.android.healthvector.presentation.medical.edit.medicines.EditDoctorVisitActivity;
import ru.android.healthvector.presentation.medical.edit.medicines.EditDoctorVisitPresenter;
import ru.android.healthvector.presentation.medical.edit.visits.EditMedicineTakingActivity;
import ru.android.healthvector.presentation.medical.edit.visits.EditMedicineTakingPresenter;
import ru.android.healthvector.presentation.medical.filter.medicines.MedicineTakingFilterPresenter;
import ru.android.healthvector.presentation.medical.filter.visits.DoctorVisitFilterPresenter;
import ru.android.healthvector.presentation.medical.partitions.medicines.MedicineTakingListPresenter;
import ru.android.healthvector.presentation.medical.partitions.visits.DoctorVisitsPresenter;
import ru.android.healthvector.presentation.onboarding.AppIntroActivity;
import ru.android.healthvector.presentation.onboarding.AppIntroPresenter;
import ru.android.healthvector.presentation.profile.ProfileEditActivity;
import ru.android.healthvector.presentation.profile.ProfileEditPresenter;
import ru.android.healthvector.presentation.settings.SettingsFragment;
import ru.android.healthvector.presentation.settings.SettingsPresenter;
import ru.android.healthvector.presentation.settings.daylength.DayLengthActivity;
import ru.android.healthvector.presentation.settings.daylength.DayLengthPresenter;
import ru.android.healthvector.presentation.settings.notification.NotificationActivity;
import ru.android.healthvector.presentation.settings.notification.NotificationPresenter;
import ru.android.healthvector.presentation.settings.notifications.NotificationsActivity;
import ru.android.healthvector.presentation.settings.notifications.NotificationsPresenter;
import ru.android.healthvector.presentation.splash.SplashActivity;
import ru.android.healthvector.presentation.splash.SplashPresenter;
import ru.android.healthvector.presentation.testing.TestResultActivity;
import ru.android.healthvector.presentation.testing.TestResultPresenter;
import ru.android.healthvector.presentation.testing.TestingActivity;
import ru.android.healthvector.presentation.testing.TestingPresenter;
import ru.android.healthvector.presentation.testing.dialogs.TestParametersPresenter;
import ru.android.healthvector.receivers.TimeChangedReceiver;
import ru.android.healthvector.services.AccountService;
import ru.android.healthvector.services.CloudService;
import ru.android.healthvector.services.EventScheduleService;
import ru.android.healthvector.services.LinearGroupFinishedService;
import ru.android.healthvector.services.TimerService;
import ru.android.healthvector.services.UpdateDataService;
import ru.android.healthvector.services.notifications.AlarmEventService;
import ru.android.healthvector.services.notifications.NotificationEventService;

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

    void inject(MedicineMeasurePickerPresenter presenter);

    void inject(MedicineMeasureAddPresenter presenter);

    void inject(FoodPickerPresenter presenter);

    void inject(FoodAddPresenter presenter);

    void inject(FoodMeasurePickerPresenter presenter);

    void inject(FoodMeasureAddPresenter presenter);

    void inject(AchievementPickerPresenter presenter);

    void inject(AchievementAddPresenter presenter);

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

    void inject(AddFoodPresenter presenter);

    void inject(AddFoodMeasurePresenter presenter);

    void inject(DayLengthPresenter presenter);

    void inject(AppIntroPresenter presenter);

    void inject(NotificationPresenter presenter);

    void inject(NotificationsPresenter presenter);

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

    void inject(MedicineMeasurePickerActivity activity);

    void inject(MedicineMeasureAddActivity activity);

    void inject(FoodPickerActivity activity);

    void inject(FoodAddActivity activity);

    void inject(FoodMeasurePickerActivity activity);

    void inject(FoodMeasureAddActivity activity);

    void inject(AchievementPickerActivity activity);

    void inject(AchievementAddActivity activity);

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

    void inject(DayLengthActivity activity);

    void inject(AppIntroActivity activity);

    void inject(NotificationActivity activity);

    void inject(NotificationsActivity activity);

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

    void inject(EventScheduleService service);

    void inject(LinearGroupFinishedService service);

    void inject(AlarmEventService service);

    void inject(NotificationEventService service);

    // receivers
    void inject(TimeChangedReceiver receiver);
}
