package ru.android.childdiary.presentation.core.dialogs;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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
public class BaseLengthValueDialogArguments extends BaseDialogArguments {
    @NonNull
    ArrayList<TimeUnit> timeUnits;
    @NonNull
    HashMap<TimeUnit, ArrayList<Integer>> timeUnitValues;
    @Nullable
    LengthValue lengthValue;

    @Builder(builderMethodName = "baseLengthValueBuilder")
    public BaseLengthValueDialogArguments(@Nullable Sex sex,
                                          @NonNull HashMap<TimeUnit, ArrayList<Integer>> timeUnitValues,
                                          @Nullable LengthValue lengthValue) {
        super(sex);
        ArrayList<TimeUnit> timeUnits = new ArrayList<>(timeUnitValues.keySet());
        Collections.sort(timeUnits);
        this.timeUnits = timeUnits;
        this.timeUnitValues = timeUnitValues;
        this.lengthValue = lengthValue;
    }
}
