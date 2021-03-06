package ru.android.healthvector.presentation.core.fields.dialogs.add.core;

import android.support.annotation.Nullable;

import lombok.AccessLevel;
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
public abstract class AddValueDialogArguments extends BaseDialogArguments {
    public AddValueDialogArguments(@Nullable Sex sex) {
        super(sex);
    }
}
