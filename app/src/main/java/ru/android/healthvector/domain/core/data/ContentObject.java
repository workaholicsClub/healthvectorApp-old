package ru.android.healthvector.domain.core.data;

import android.support.annotation.NonNull;

public interface ContentObject<T> {
    boolean isContentEmpty();

    boolean isContentEqual(@NonNull T other);
}
