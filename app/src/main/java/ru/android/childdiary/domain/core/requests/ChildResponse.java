package ru.android.childdiary.domain.core.requests;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.child.data.Child;

@Value
@AllArgsConstructor(suppressConstructorProperties = true)
public class ChildResponse<T> {
    @NonNull
    Child child;
    @NonNull
    T response;
}
