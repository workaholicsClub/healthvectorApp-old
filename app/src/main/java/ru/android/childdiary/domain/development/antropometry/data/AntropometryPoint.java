package ru.android.childdiary.domain.development.antropometry.data;

import org.joda.time.LocalDate;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class AntropometryPoint {
    @NonNull
    Double value;
    @NonNull
    LocalDate date;
}
