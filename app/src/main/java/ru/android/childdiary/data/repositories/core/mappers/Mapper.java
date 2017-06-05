package ru.android.childdiary.data.repositories.core.mappers;

import android.support.annotation.NonNull;

public interface Mapper<FromObject, ToObject> {
    ToObject map(@NonNull FromObject fromObject);
}
