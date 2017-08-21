package ru.android.childdiary.presentation.core.dialogs;

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
public class AlertDialogArguments extends BaseDialogArguments {
    @Nullable
    String title;
    @Nullable
    String message;
    @Nullable
    String positiveButtonText;
    @Nullable
    String negativeButtonText;
    boolean cancelable;

    @Builder
    public AlertDialogArguments(@Nullable Sex sex,
                                @Nullable String title,
                                @Nullable String message,
                                @Nullable String positiveButtonText,
                                @Nullable String negativeButtonText,
                                boolean cancelable) {
        super(sex);
        this.title = title;
        this.message = message;
        this.positiveButtonText = positiveButtonText;
        this.negativeButtonText = negativeButtonText;
        this.cancelable = cancelable;
    }
}
