package ru.android.childdiary.presentation.calendar.dialogs;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.core.TimeUnit;
import ru.android.childdiary.presentation.core.dialogs.BaseLengthValueDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class MoveEventDialogArguments extends BaseLengthValueDialogArguments {
    private static final HashMap<TimeUnit, ArrayList<Integer>> map = new HashMap<>();

    static {
        map.put(TimeUnit.MINUTE, new ArrayList<>(Arrays.asList(15, 30, 45)));
        map.put(TimeUnit.HOUR, new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)));
        map.put(TimeUnit.DAY, new ArrayList<>(Arrays.asList(2, 3, 4, 5, 6)));
        map.put(TimeUnit.WEEK, new ArrayList<>(Arrays.asList(1, 2)));
    }

    @NonNull
    MasterEvent event;

    @Builder
    public MoveEventDialogArguments(@Nullable Sex sex,
                                    @NonNull MasterEvent event) {
        super(sex, map, null);
        this.event = event;
    }
}