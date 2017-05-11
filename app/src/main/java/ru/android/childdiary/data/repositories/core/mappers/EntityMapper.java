package ru.android.childdiary.data.repositories.core.mappers;

import android.support.annotation.NonNull;

import io.requery.BlockingEntityStore;

public interface EntityMapper<Data, Entity extends Data, PlainObject> {
    PlainObject mapToPlainObject(@NonNull Data data);

    Entity mapToEntity(BlockingEntityStore blockingEntityStore, @NonNull PlainObject plainObject);

    void fillNonReferencedFields(@NonNull Entity to, @NonNull PlainObject from);
}
