package ru.android.childdiary.domain.interactors.calendar.data.core;

import org.joda.time.LocalTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.core.data.ContentObject;

@Value
public class LinearGroups implements Serializable, ContentObject<LinearGroups> {
    @NonNull
    List<LocalTime> times;

    @Builder
    public LinearGroups(@NonNull List<LocalTime> times) {
        this.times = Collections.unmodifiableList(times);
    }

    public LinearGroups withTime(int index, LocalTime time) {
        if (index < 0 || index >= times.size()) {
            throw new IllegalArgumentException("Linear groups: index out of bounds: " + index + "; length = " + times.size());
        }
        List<LocalTime> timesCopy = new ArrayList<>(times);
        timesCopy.set(index, time);
        Collections.sort(timesCopy);
        return LinearGroups.builder().times(timesCopy).build();
    }

    @Override
    public boolean isContentEmpty() {
        return false;
    }

    @Override
    public boolean isContentEqual(@android.support.annotation.NonNull LinearGroups other) {
        return times.equals(other.times);
    }
}
