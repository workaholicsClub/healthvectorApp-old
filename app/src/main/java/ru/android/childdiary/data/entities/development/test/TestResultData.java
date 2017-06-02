package ru.android.childdiary.data.entities.development.test;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;
import io.requery.Table;

@Table(name = "test_result")
@Entity(name = "TestResultEntity")
public interface TestResultData extends Persistable {
    @Key
    @Generated
    Long getId();
    // Дата тестирования
    // Тип теста
    // Параметр теста
    // Возраст ребенка
    // Результат (стадия развития)
}
