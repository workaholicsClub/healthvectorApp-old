package ru.android.childdiary.presentation.core.dialogs;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.calendar.data.core.LengthValue;
import ru.android.childdiary.domain.calendar.data.core.TimeUnit;
import ru.android.childdiary.presentation.core.BaseDialogArguments;
import ru.android.childdiary.utils.CollectionUtils;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public abstract class BaseLengthValueDialogArguments extends BaseDialogArguments {
    @NonNull
    List<TimeUnit> timeUnits;
    @NonNull
    Map<TimeUnit, List<Integer>> timeUnitValues;
    @Nullable
    LengthValue lengthValue;
    @Nullable
    String title;
    @Nullable
    String description;
    boolean cancelable;

    public BaseLengthValueDialogArguments(@Nullable Sex sex,
                                          @NonNull Map<TimeUnit, List<Integer>> timeUnitValues,
                                          @Nullable LengthValue lengthValue,
                                          @Nullable String title,
                                          @Nullable String description,
                                          boolean cancelable) {
        super(sex);
        List<TimeUnit> timeUnits = new ArrayList<>(timeUnitValues.keySet());
        Collections.sort(timeUnits);
        this.timeUnits = Collections.unmodifiableList(timeUnits);
        this.timeUnitValues = CollectionUtils.unmodifiableMap(timeUnitValues);
        this.lengthValue = lengthValue;
        this.title = title;
        this.description = description;
        this.cancelable = cancelable;
    }
}
