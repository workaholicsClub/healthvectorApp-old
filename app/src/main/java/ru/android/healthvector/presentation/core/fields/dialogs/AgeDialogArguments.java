package ru.android.healthvector.presentation.core.fields.dialogs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.presentation.core.BaseDialogArguments;
import ru.android.healthvector.utils.strings.TimeUtils;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class AgeDialogArguments extends BaseDialogArguments {
    @NonNull
    String title;
    @Nullable
    TimeUtils.Age age;
    @NonNull
    TimeUtils.Age maxAge;

    @Builder
    public AgeDialogArguments(@Nullable Sex sex,
                              @NonNull String title,
                              @Nullable TimeUtils.Age age,
                              @NonNull TimeUtils.Age maxAge) {
        super(sex);
        this.title = title;
        this.age = age;
        this.maxAge = maxAge;
    }
}
