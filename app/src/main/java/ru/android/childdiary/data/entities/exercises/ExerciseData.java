package ru.android.childdiary.data.entities.exercises;

import io.requery.Entity;
import io.requery.Persistable;
import io.requery.Table;

@Table(name = "exercise")
@Entity(name = "ExerciseEntity")
public interface ExerciseData extends Persistable {
    // Наименование занятия
    // Тип занятия
    // Описание
    // Фотографии
}
