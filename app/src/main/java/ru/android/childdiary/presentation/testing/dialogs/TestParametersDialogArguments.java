package ru.android.childdiary.presentation.testing.dialogs;

import android.support.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;
import ru.android.childdiary.presentation.core.BaseDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@Getter
public class TestParametersDialogArguments extends BaseDialogArguments {
    @NonNull
    Child child;

    @NonNull
    Test test;

    @Builder
    public TestParametersDialogArguments(@Nullable Sex sex,
                                         @NonNull Child child,
                                         @NonNull Test test) {
        super(sex);
        this.child = child;
        this.test = test;
    }
}
