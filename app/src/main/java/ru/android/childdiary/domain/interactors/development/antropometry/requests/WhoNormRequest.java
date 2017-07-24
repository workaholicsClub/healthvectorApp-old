package ru.android.childdiary.domain.interactors.development.antropometry.requests;

import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.Child;

@Value
@Builder
public class WhoNormRequest {
    @NonNull
    Child child;
    @Nullable
    LocalDate minDate;
    @Nullable
    LocalDate maxDate;
}
