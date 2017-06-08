package ru.android.childdiary.utils;

import java.util.List;

import io.reactivex.Observable;

public class CollectionUtils {
    public static <E> List<E> toList(Iterable<E> iterable) {
        return Observable.fromIterable(iterable).toList().blockingGet();
    }
}
