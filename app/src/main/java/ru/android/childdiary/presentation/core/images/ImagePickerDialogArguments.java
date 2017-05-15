package ru.android.childdiary.presentation.core.images;

import android.support.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.core.images.ImageType;
import ru.android.childdiary.presentation.core.BaseDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class ImagePickerDialogArguments extends BaseDialogArguments {
    boolean showDeleteItem;
    @NonNull
    ImageType imageType;

    @Builder
    public ImagePickerDialogArguments(@Nullable Sex sex,
                                      boolean showDeleteItem,
                                      @NonNull ImageType imageType) {
        super(sex);
        this.showDeleteItem = showDeleteItem;
        this.imageType = imageType;
    }
}
