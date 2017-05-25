package ru.android.childdiary.presentation.core;

import org.joda.time.LocalDate;

import java.io.Serializable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.Child;

@Value
@Builder
public class AppPartitionArguments implements Serializable {
    @NonNull
    Child child;
    @NonNull
    LocalDate selectedDate;
}
