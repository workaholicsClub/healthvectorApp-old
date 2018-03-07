package ru.android.healthvector.presentation.core.dialogs;

import android.support.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.presentation.core.BaseDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class ProgressDialogArguments extends BaseDialogArguments {
    @NonNull
    String title;
    @NonNull
    String message;

    @Builder
    public ProgressDialogArguments(@Nullable Sex sex, @NonNull String title, @NonNull String message) {
        super(sex);
        this.title = title;
        this.message = message;
    }
}
