package ru.android.healthvector.presentation.chart.testing.dialogs;

import android.support.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.healthvector.data.types.DomanTestParameter;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.domain.development.testing.data.tests.core.Test;
import ru.android.healthvector.presentation.core.BaseDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@Getter
public class ParameterDialogArguments extends BaseDialogArguments {
    @NonNull
    Test test;
    @NonNull
    DomanTestParameter selectedParameter;

    @Builder(toBuilder = true)
    public ParameterDialogArguments(@Nullable Sex sex,
                                    @NonNull Test test,
                                    @NonNull DomanTestParameter selectedParameter) {
        super(sex);
        this.test = test;
        this.selectedParameter = selectedParameter;
    }
}
