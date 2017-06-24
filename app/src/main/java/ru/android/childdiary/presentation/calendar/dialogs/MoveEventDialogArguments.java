package ru.android.childdiary.presentation.calendar.dialogs;

import android.support.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.presentation.core.BaseDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class MoveEventDialogArguments extends BaseDialogArguments {
    @NonNull
    MasterEvent event;

    @Builder
    public MoveEventDialogArguments(@Nullable Sex sex,
                                    @NonNull MasterEvent event) {
        super(sex);
        this.event = event;
    }
}
