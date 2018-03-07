package ru.android.healthvector.presentation.core.images;

import android.support.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.presentation.core.BaseDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class ImagePickerDialogArguments extends BaseDialogArguments {
    boolean showDeleteItem;
    boolean showCircleFrame;

    @Builder
    public ImagePickerDialogArguments(@Nullable Sex sex,
                                      boolean showDeleteItem,
                                      boolean showCircleFrame) {
        super(sex);
        this.showDeleteItem = showDeleteItem;
        this.showCircleFrame = showCircleFrame;
    }
}
