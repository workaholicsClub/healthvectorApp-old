package ru.android.childdiary.presentation.medical.fragments.medicines;

import org.joda.time.LocalDate;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MedicineTakingListFilter {
    LocalDate from;
    LocalDate to;
}
