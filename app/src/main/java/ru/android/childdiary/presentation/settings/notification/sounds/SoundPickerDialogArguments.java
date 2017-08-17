package ru.android.childdiary.presentation.settings.notification.sounds;

import android.support.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.calendar.data.core.SoundInfo;
import ru.android.childdiary.presentation.core.BaseDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class SoundPickerDialogArguments extends BaseDialogArguments {
    @Nullable
    SoundInfo selectedSoundInfo;

    @Builder
    public SoundPickerDialogArguments(@Nullable Sex sex,
                                      @Nullable SoundInfo selectedSoundInfo) {
        super(sex);
        this.selectedSoundInfo = selectedSoundInfo;
    }
}
