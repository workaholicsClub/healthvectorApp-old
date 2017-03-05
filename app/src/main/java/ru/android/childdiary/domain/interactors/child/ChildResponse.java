package ru.android.childdiary.domain.interactors.child;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChildResponse {
    List<Child> childList;
    Child activeChild;

    public ChildResponse.ChildResponseBuilder getBuilder() {
        return ChildResponse.builder().childList(childList).activeChild(activeChild);
    }
}
