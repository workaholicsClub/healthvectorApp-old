package ru.android.childdiary.domain.interactors.child;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ChildResponse {
    List<Child> childList;
    Child activeChild;
}
