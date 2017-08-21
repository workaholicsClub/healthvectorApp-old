package ru.android.childdiary.presentation.calendar.dialogs;

import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.calendar.data.core.TimeUnit;
import ru.android.childdiary.presentation.core.dialogs.BaseLengthValueDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class MoveEventDialogArguments extends BaseLengthValueDialogArguments {
    private static final Map<TimeUnit, List<Integer>> map = new HashMap<>();

    static {
        map.put(TimeUnit.MINUTE, Arrays.asList(15, 30, 45));
        map.put(TimeUnit.HOUR, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
        map.put(TimeUnit.DAY, Arrays.asList(2, 3, 4, 5, 6));
        map.put(TimeUnit.WEEK, Arrays.asList(1, 2));
    }

    @NonNull
    MasterEvent event;

    @Builder
    public MoveEventDialogArguments(@Nullable Sex sex,
                                    @NonNull MasterEvent event,
                                    @Nullable String title,
                                    @Nullable String description,
                                    boolean cancelable) {
        super(sex, map, null, title, description, cancelable);
        this.event = event;
    }
}
