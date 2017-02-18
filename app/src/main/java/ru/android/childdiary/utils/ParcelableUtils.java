package ru.android.childdiary.utils;

import android.content.Intent;
import android.os.Parcelable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

public class ParcelableUtils {
    public static void wrap(Intent intent, String key, List<? extends Parcelable> parcelables) {
        intent.putParcelableArrayListExtra(key, new ArrayList<>(parcelables));
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> unwrap(Intent intent, String key) {
        List<Parcelable> parcelables = intent.getParcelableArrayListExtra(key);
        return Stream.of(parcelables)
                .map(parcelable -> (T) parcelable)
                .collect(Collectors.toList());
    }
}
