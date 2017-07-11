package ru.android.childdiary.domain.interactors.development.testing.tests;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DomanMentalTest extends Test {
    @Builder
    public DomanMentalTest(@NonNull String name, @NonNull String description) {
        super(TestType.DOMAN_MENTAL, name, description);
    }
}
