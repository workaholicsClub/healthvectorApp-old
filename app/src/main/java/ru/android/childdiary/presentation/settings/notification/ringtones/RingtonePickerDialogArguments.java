package ru.android.childdiary.presentation.settings.notification.ringtones;

import android.net.Uri;
import android.support.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.core.BaseDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class RingtonePickerDialogArguments extends BaseDialogArguments {
    @Nullable
    Uri soundUri; // TODO not serializable?

    @Builder
    public RingtonePickerDialogArguments(@Nullable Sex sex,
                                         @Nullable Uri soundUri) {
        super(sex);
        this.soundUri = soundUri;
    }
}
