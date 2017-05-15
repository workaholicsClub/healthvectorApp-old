package ru.android.childdiary.presentation.core;

import android.support.annotation.Nullable;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.Sex;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
@Builder(builderMethodName = "baseBuilder")
public class BaseDialogArguments implements Serializable {
    @Nullable
    Sex sex;
}
