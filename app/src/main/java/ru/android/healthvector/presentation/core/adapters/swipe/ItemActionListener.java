package ru.android.healthvector.presentation.core.adapters.swipe;

public interface ItemActionListener<T> {
    void delete(T item);

    void edit(T item);
}
