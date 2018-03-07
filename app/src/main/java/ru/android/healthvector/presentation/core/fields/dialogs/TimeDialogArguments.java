package ru.android.healthvector.presentation.core.fields.dialogs;

import android.support.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.presentation.core.BaseDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class TimeDialogArguments extends BaseDialogArguments {
    int minutes;
    @Nullable
    String title;
    boolean showDays;
    boolean showHours;
    boolean showMinutes;

    @Builder
    public TimeDialogArguments(@Nullable Sex sex, int minutes, @Nullable String title,
                               boolean showDays,
                               boolean showHours,
                               boolean showMinutes) {
        super(sex);
        this.minutes = minutes;
        this.title = title;
        this.showDays = showDays;
        this.showHours = showHours;
        this.showMinutes = showMinutes;
    }
}
