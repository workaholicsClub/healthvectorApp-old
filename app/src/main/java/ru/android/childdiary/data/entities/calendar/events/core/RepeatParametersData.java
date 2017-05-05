package ru.android.childdiary.data.entities.calendar.events.core;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Table;

@Table(name = "repeat_parameters")
@Entity(name = "RepeatParametersEntity")
public interface RepeatParametersData {
    @Key
    @Generated
    Long getId();

    Integer getPeriodicityInMinutes();

    // TODO: при необходимости постоянной длительностью считать значение -1
    Integer getLengthInMinutes();

    LinearGroups getLinearGroups();

    // TODO: добавить повтор в минутах при необходимости
    // TODO: добавить список дней недели при необходимости
    // TODO: добавить длительность в приемах при необходимости
    // TODO: добавить поля "вставлено с (включительно)", "вставлено по (включительно)" DateTime
}
