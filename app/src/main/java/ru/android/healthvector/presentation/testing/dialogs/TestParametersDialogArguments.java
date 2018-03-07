package ru.android.healthvector.presentation.testing.dialogs;

import android.support.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.testing.data.tests.core.Test;
import ru.android.healthvector.presentation.core.BaseDialogArguments;

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
