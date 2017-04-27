package ru.android.childdiary.presentation.medical.fragments.visits;

import org.joda.time.LocalDate;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DoctorVisitsFilter {
    LocalDate from;
    LocalDate to;
    // TODO: doctor specialty
}
