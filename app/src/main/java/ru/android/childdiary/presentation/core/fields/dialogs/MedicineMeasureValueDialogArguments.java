package ru.android.childdiary.presentation.core.fields.dialogs;

import android.support.annotation.Nullable;

import java.util.ArrayList;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasureValue;
import ru.android.childdiary.presentation.core.BaseDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class MedicineMeasureValueDialogArguments extends BaseDialogArguments {
    @NonNull
    ArrayList<MedicineMeasure> medicineMeasureList;
    @Nullable
    MedicineMeasureValue medicineMeasureValue;

    @Builder
    public MedicineMeasureValueDialogArguments(@Nullable Sex sex,
                                               @NonNull ArrayList<MedicineMeasure> medicineMeasureList,
                                               @Nullable MedicineMeasureValue medicineMeasureValue) {
        super(sex);
        this.medicineMeasureList = medicineMeasureList;
        this.medicineMeasureValue = medicineMeasureValue;
    }
}
