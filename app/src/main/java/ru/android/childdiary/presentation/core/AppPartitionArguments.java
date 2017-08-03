package ru.android.childdiary.presentation.core;

import org.joda.time.LocalDate;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.domain.interactors.child.data.Child;

@ToString
@EqualsAndHashCode
@AllArgsConstructor(suppressConstructorProperties = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@Getter
@Builder
public class AppPartitionArguments implements Serializable {
    @NonNull
    Child child;
    @NonNull
    LocalDate selectedDate;
}
