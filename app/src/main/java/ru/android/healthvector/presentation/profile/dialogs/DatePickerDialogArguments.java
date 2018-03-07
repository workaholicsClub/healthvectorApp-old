package ru.android.healthvector.presentation.profile.dialogs;

import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

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
public class DatePickerDialogArguments extends BaseDialogArguments {
    @Nullable
    String title;
    @Nullable
    LocalDate date;
    @Nullable
    LocalDate minDate;
    @Nullable
    LocalDate maxDate;

    @Builder
    public DatePickerDialogArguments(@Nullable Sex sex,
                                     @Nullable String title,
                                     @Nullable LocalDate date,
                                     @Nullable LocalDate minDate,
                                     @Nullable LocalDate maxDate) {
        super(sex);
        this.title = title;
        this.date = date;
        this.minDate = minDate;
        this.maxDate = maxDate;
    }
}
