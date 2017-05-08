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

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class AddValueDialogArguments extends BaseDialogArguments {
    @Builder(builderMethodName = "addValueBuilder")
    public AddValueDialogArguments(@Nullable Sex sex) {
        super(sex);
    }
}
