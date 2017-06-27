package ru.android.childdiary.domain.interactors.core;

import org.joda.time.LocalTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.core.ContentObject;

@Value
@Builder(toBuilder = true)
public class LinearGroups implements Serializable, ContentObject<LinearGroups> {
    // TODO make immutable
    @NonNull
    ArrayList<LocalTime> times;

    public LinearGroups withTime(int index, LocalTime time) {
        if (index < 0 || index >= times.size()) {
            throw new IllegalArgumentException("Linear groups: index out of bounds: " + index + "; length = " + times.size());
        }
        ArrayList<LocalTime> timesCopy = new ArrayList<>(times);
        timesCopy.set(index, time);
        Collections.sort(timesCopy);
        return LinearGroups.builder().times(timesCopy).build();
    }

    @Override
    public boolean isContentEmpty() {
        return false;
    }

    @Override
    public boolean isContentEqual(@NonNull LinearGroups other) {
        return times.equals(other.times);
    }
}
