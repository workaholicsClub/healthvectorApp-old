package ru.android.childdiary.presentation.medical.filter.visits;

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
import ru.android.childdiary.domain.interactors.dictionaries.doctors.Doctor;
import ru.android.childdiary.presentation.medical.filter.core.MedicalFilterDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class DoctorVisitFilterDialogArguments extends MedicalFilterDialogArguments<Doctor> {
    @Builder(toBuilder = true)
    public DoctorVisitFilterDialogArguments(@Nullable Sex sex,
                                            @NonNull List<Doctor> items,
                                            @NonNull List<Doctor> selectedItems,
                                            @Nullable LocalDate fromDate,
                                            @Nullable LocalDate toDate) {
        super(sex, items, selectedItems, fromDate, toDate);
    }
}
