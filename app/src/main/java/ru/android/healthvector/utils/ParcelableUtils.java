package ru.android.healthvector.utils;

import android.content.Intent;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class ParcelableUtils {
    public static void wrap(Intent intent, String key, List<? extends Parcelable> parcelables) {
        intent.putParcelableArrayListExtra(key, new ArrayList<>(parcelables));
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> unwrap(Intent intent, String key) {
        List<Parcelable> parcelables = intent.getParcelableArrayListExtra(key);
        return Observable.fromIterable(parcelables)
                .map(parcelable -> (T) parcelable)
                .toList()
                .blockingGet();
    }
}
