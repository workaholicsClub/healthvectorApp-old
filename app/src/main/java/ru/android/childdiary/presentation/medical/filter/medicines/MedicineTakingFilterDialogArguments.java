package ru.android.childdiary.presentation.medical.filter.medicines;

import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.dictionaries.medicines.Medicine;
import ru.android.childdiary.presentation.medical.filter.core.MedicalFilterDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class MedicineTakingFilterDialogArguments extends MedicalFilterDialogArguments<Medicine> {
    @Builder(toBuilder = true)
    public MedicineTakingFilterDialogArguments(@Nullable Sex sex,
                                               @NonNull List<Medicine> items,
                                               @NonNull List<Medicine> selectedItems,
                                               @Nullable LocalDate fromDate,
                                               @Nullable LocalDate toDate) {
        super(sex, items, selectedItems, fromDate, toDate);
    }
}
