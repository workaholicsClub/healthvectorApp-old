package ru.android.childdiary.domain.interactors.core;

import org.joda.time.LocalTime;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.core.ContentObject;

@Value
@Builder(toBuilder = true)
public class LinearGroups implements Serializable, ContentObject<LinearGroups> {
    @NonNull
    ArrayList<LocalTime> times;

    @Override
    public boolean isContentEmpty() {
        return false;
    }

    @Override
    public boolean isContentEqual(@NonNull LinearGroups other) {
        return times.equals(other.times);
    }
}
