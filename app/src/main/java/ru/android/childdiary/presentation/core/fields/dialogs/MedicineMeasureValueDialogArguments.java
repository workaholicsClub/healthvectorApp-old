package ru.android.childdiary.presentation.core.fields.dialogs;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.dictionaries.medicinemeasure.MedicineMeasure;
import ru.android.childdiary.domain.interactors.medical.data.MedicineMeasureValue;
import ru.android.childdiary.presentation.core.BaseDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class MedicineMeasureValueDialogArguments extends BaseDialogArguments {
    @NonNull
    List<MedicineMeasure> medicineMeasureList;
    @Nullable
    MedicineMeasureValue medicineMeasureValue;

    @Builder
    public MedicineMeasureValueDialogArguments(@Nullable Sex sex,
                                               @NonNull List<MedicineMeasure> medicineMeasureList,
                                               @Nullable MedicineMeasureValue medicineMeasureValue) {
        super(sex);
        this.medicineMeasureList = Collections.unmodifiableList(medicineMeasureList);
        this.medicineMeasureValue = medicineMeasureValue;
    }
}
