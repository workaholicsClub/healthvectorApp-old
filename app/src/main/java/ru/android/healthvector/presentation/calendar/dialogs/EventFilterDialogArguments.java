package ru.android.healthvector.presentation.calendar.dialogs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.presentation.core.BaseDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class EventFilterDialogArguments extends BaseDialogArguments {
    @NonNull
    Set<EventType> selectedItems;

    @Builder
    public EventFilterDialogArguments(@Nullable Sex sex,
                                      @NonNull Set<EventType> selectedItems) {
        super(sex);
        this.selectedItems = Collections.unmodifiableSet(selectedItems);
    }
}
