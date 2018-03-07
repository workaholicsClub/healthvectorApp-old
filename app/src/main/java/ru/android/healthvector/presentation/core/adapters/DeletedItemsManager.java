package ru.android.healthvector.presentation.core.adapters;

import android.support.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class DeletedItemsManager<T> {
    private final Logger logger = LoggerFactory.getLogger(toString());
    private final Set<T> deletedItems = new HashSet<>();

    public boolean check(@NonNull T item) {
        if (deletedItems.contains(item)) {
            logger.error("already deleted or selected for deletion: " + item);
            return true;
        }
        return false;
    }

    public boolean checkAndAdd(@NonNull T item) {
        if (check(item)) {
            return true;
        }
        deletedItems.add(item);
        return false;
    }
}
