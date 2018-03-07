package ru.android.healthvector.presentation.development.partitions.achievements.dialogs;

import android.support.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.healthvector.data.types.AchievementType;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.presentation.core.BaseDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@Getter
public class AchievementTypeDialogArguments extends BaseDialogArguments {
    @NonNull
    AchievementType achievementType;

    @Builder(toBuilder = true)
    public AchievementTypeDialogArguments(@Nullable Sex sex,
                                          @NonNull AchievementType achievementType) {
        super(sex);
        this.achievementType = achievementType;
    }
}
