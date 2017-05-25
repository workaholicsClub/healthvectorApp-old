package ru.android.childdiary.domain.interactors.calendar.events.core;

import java.util.List;

public interface LinearGroupItem<T> {
    List<LinearGroupFieldType> getChangedFields(T other);
}
