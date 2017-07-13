package ru.android.childdiary.presentation.core.fields.dialogs;

import android.support.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.core.BaseDialogArguments;
import ru.android.childdiary.utils.strings.TimeUtils;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class AgeDialogArguments extends BaseDialogArguments {
    @Nullable
    TimeUtils.Age age;
    int maxYears;

    @Builder
    public AgeDialogArguments(@Nullable Sex sex, @Nullable TimeUtils.Age age, int maxYears) {
        super(sex);
        this.age = age;
        if (maxYears < 0) {
            maxYears = 0;
        }
        this.maxYears = maxYears;
    }
}
