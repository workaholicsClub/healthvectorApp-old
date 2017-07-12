package ru.android.childdiary.domain.interactors.development.testing.processors.core;

import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.types.DomanTestParameter;

@Value
@Builder
public class TestParameters {
    @Nullable
    LocalDate date;
    @Nullable
    DomanTestParameter parameter;
}
