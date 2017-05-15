package ru.android.childdiary.presentation.core.fields.dialogs;

import android.support.annotation.Nullable;

import java.util.ArrayList;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.core.LengthValue;
import ru.android.childdiary.domain.interactors.core.TimeUnit;
import ru.android.childdiary.presentation.core.BaseDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class LengthValueDialogArguments extends BaseDialogArguments {
    @NonNull
    ArrayList<TimeUnit> timeUnits;
    @Nullable
    LengthValue lengthValue;

    @Builder
    public LengthValueDialogArguments(@Nullable Sex sex,
                                      @NonNull ArrayList<TimeUnit> timeUnits,
                                      @Nullable LengthValue lengthValue) {
        super(sex);
        this.timeUnits = timeUnits;
        this.lengthValue = lengthValue;
    }
}
