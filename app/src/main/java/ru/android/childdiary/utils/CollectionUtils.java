package ru.android.childdiary.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class CollectionUtils {
    public static <E> List<E> toList(Iterable<E> iterable) {
        return Observable.fromIterable(iterable).toList().blockingGet();
    }

    public static <K, T> Map<K, List<T>> unmodifiableMap(Map<K, List<T>> map) {
        Map<K, List<T>> mapCopy = new HashMap<>();
        for (K key : map.keySet()) {
            List<T> list = map.get(key);
            mapCopy.put(key, Collections.unmodifiableList(list));
        }
        return Collections.unmodifiableMap(mapCopy);
    }
}
