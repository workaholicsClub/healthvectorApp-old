package ru.android.healthvector.domain.development.testing.data.processors.core;

import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import lombok.Builder;
import lombok.Value;
import ru.android.healthvector.data.types.DomanTestParameter;
import ru.android.healthvector.utils.strings.TimeUtils;

@Value
@Builder
public class TestParameters {
    @Nullable
    LocalDate birthDate;
    @Nullable
    LocalDate date;
    @Nullable
    TimeUtils.Age age;
    @Nullable
    DomanTestParameter parameter;
}
