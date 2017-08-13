package ru.android.childdiary.presentation.core.fields.dialogs;

import android.support.annotation.Nullable;

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
import ru.android.childdiary.domain.calendar.data.core.LengthValue;
import ru.android.childdiary.domain.calendar.data.core.TimeUnit;
import ru.android.childdiary.presentation.core.dialogs.BaseLengthValueDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class LengthValueDialogArguments extends BaseLengthValueDialogArguments {
    @Builder
    public LengthValueDialogArguments(@Nullable Sex sex,
                                      @NonNull Map<TimeUnit, List<Integer>> timeUnitValues,
                                      @Nullable LengthValue lengthValue) {
        super(sex, timeUnitValues, lengthValue);
    }
}
