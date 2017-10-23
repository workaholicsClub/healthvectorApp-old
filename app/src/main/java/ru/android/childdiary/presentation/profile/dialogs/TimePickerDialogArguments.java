package ru.android.childdiary.presentation.profile.dialogs;

import android.support.annotation.Nullable;

import org.joda.time.LocalTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.core.BaseDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class TimePickerDialogArguments extends BaseDialogArguments {
    @Nullable
    String title;
    @Nullable
    LocalTime time;
    @Nullable
    LocalTime minTime;
    @Nullable
    LocalTime maxTime;

    @Builder
    public TimePickerDialogArguments(@Nullable Sex sex,
                                     @Nullable String title,
                                     @Nullable LocalTime time,
                                     @Nullable LocalTime minTime,
                                     @Nullable LocalTime maxTime) {
        super(sex);
        this.title = title;
        this.time = time;
        this.minTime = minTime;
        this.maxTime = maxTime;
    }
}
