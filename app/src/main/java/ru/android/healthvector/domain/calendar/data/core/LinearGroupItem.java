package ru.android.healthvector.domain.calendar.data.core;

import java.util.List;

public interface LinearGroupItem<T> {
    List<LinearGroupFieldType> getChangedFields(T other);
}
