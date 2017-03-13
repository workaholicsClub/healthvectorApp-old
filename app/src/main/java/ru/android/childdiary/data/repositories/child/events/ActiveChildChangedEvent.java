package ru.android.childdiary.data.repositories.child.events;

import android.support.annotation.NonNull;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.Child;

@Value
@Builder
public class ActiveChildChangedEvent {
    @NonNull
    Child child;
}
