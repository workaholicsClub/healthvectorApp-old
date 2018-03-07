package ru.android.healthvector.presentation.core.fields.dialogs;

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
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.domain.calendar.data.core.LengthValue;
import ru.android.healthvector.domain.calendar.data.core.TimeUnit;
import ru.android.healthvector.presentation.core.dialogs.BaseLengthValueDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class LengthValueDialogArguments extends BaseLengthValueDialogArguments {
    @Builder
    public LengthValueDialogArguments(@Nullable Sex sex,
                                      @NonNull Map<TimeUnit, List<Integer>> timeUnitValues,
                                      @Nullable LengthValue lengthValue,
                                      @Nullable String title,
                                      @Nullable String description,
                                      boolean cancelable) {
        super(sex, timeUnitValues, lengthValue, title, description, cancelable);
    }
}
