package ru.android.childdiary.domain.interactors.medical.requests;

import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;

@Value
@Builder
public class GetDoctorVisitsFilter {
    @NonNull
    List<Doctor> selectedItems;
    @Nullable
    LocalDate fromDate;
    @Nullable
    LocalDate toDate;
}
