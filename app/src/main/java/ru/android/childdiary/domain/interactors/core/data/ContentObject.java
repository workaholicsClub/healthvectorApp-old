package ru.android.childdiary.domain.interactors.core.data;

import android.support.annotation.NonNull;

public interface ContentObject<T> {
    boolean isContentEmpty();

    boolean isContentEqual(@NonNull T other);
}
