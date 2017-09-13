package ru.android.childdiary.data.db;

import android.content.res.Resources;

import ru.android.childdiary.R;
import ru.android.childdiary.data.db.entities.dictionaries.AchievementEntity;
import ru.android.childdiary.data.db.entities.dictionaries.DoctorEntity;
import ru.android.childdiary.data.db.entities.dictionaries.FoodEntity;
import ru.android.childdiary.data.db.entities.dictionaries.FoodMeasureEntity;
import ru.android.childdiary.data.db.entities.dictionaries.MedicineEntity;
import ru.android.childdiary.data.db.entities.dictionaries.MedicineMeasureEntity;

class Dictionaries {
    static final String[] LANGUAGES = new String[]{"en", "ru"};

    static final String[] FOOD_COLUMNS = new String[]{FoodEntity.NAME_EN.getName(), FoodEntity.NAME_RU.getName()};
    static final String[] FOOD_MEASURE_COLUMNS = new String[]{FoodMeasureEntity.NAME_EN.getName(), FoodMeasureEntity.NAME_RU.getName()};
    static final String[] DOCTOR_COLUMNS = new String[]{DoctorEntity.NAME_EN.getName(), DoctorEntity.NAME_RU.getName()};
    static final String[] MEDICINE_COLUMNS = new String[]{MedicineEntity.NAME_EN.getName(), MedicineEntity.NAME_RU.getName()};
    static final String[] MEDICINE_MEASURE_COLUMNS = new String[]{MedicineMeasureEntity.NAME_EN.getName(), MedicineMeasureEntity.NAME_RU.getName()};
    static final String[] ACHIEVEMENT_COLUMNS = new String[]{AchievementEntity.NAME_EN.getName(), AchievementEntity.NAME_RU.getName()};

    final String[][]
            foodNames = new String[FOOD_COLUMNS.length][],
            foodMeasureNames = new String[FOOD_MEASURE_COLUMNS.length][],
            doctorNames = new String[DOCTOR_COLUMNS.length][],
            medicineNames = new String[MEDICINE_COLUMNS.length][],
            medicineMeasureNames = new String[MEDICINE_MEASURE_COLUMNS.length][],
            achievementNames = new String[ACHIEVEMENT_COLUMNS.length][];

    void fillFromResources(int languageIndex, Resources resources) {
        foodNames[languageIndex] = resources.getStringArray(R.array.food);
        foodMeasureNames[languageIndex] = resources.getStringArray(R.array.food_measure);
        doctorNames[languageIndex] = resources.getStringArray(R.array.doctor);
        medicineNames[languageIndex] = resources.getStringArray(R.array.medicine);
        medicineMeasureNames[languageIndex] = resources.getStringArray(R.array.medicine_measure);
        achievementNames[languageIndex] = resources.getStringArray(R.array.achievement);
    }
}
