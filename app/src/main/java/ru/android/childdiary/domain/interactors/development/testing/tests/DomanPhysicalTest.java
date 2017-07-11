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
public class DomanPhysicalTest extends Test {
    @Builder
    public DomanPhysicalTest(@NonNull String name, @NonNull String description) {
        super(TestType.DOMAN_PHYSICAL, name, description);
    }
}
