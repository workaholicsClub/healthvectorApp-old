package ru.android.healthvector.presentation.core.fields.dialogs.add.foodmeasure;

import android.support.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.presentation.core.fields.dialogs.add.core.AddValueDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class AddFoodMeasureDialogArguments extends AddValueDialogArguments {
    @Builder
    public AddFoodMeasureDialogArguments(@Nullable Sex sex) {
        super(sex);
    }
}
