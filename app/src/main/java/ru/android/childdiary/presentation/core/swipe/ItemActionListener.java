package ru.android.childdiary.presentation.core.swipe;

public interface ItemActionListener<T> {
    void delete(T item);

    void edit(T item);
}
