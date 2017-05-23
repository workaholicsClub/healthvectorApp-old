package ru.android.childdiary.presentation.medical.filter.adapters;

import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.requests.GetDoctorVisitsFilter;
import ru.android.childdiary.domain.interactors.medical.requests.GetMedicineTakingListFilter;

public class ChipsUtils {
    public static List<Chips> mapFilterToChips(GetMedicineTakingListFilter filter) {
        List<Chips> result = new ArrayList<>();

        DateIntervalChips dateIntervalChips = mapFilterToDateIntervalChips(filter.getFromDate(), filter.getToDate());
        if (dateIntervalChips != null) {
            result.add(dateIntervalChips);
        }

        for (Medicine medicine : filter.getSelectedItems()) {
            result.add(MedicineChips.builder().medicine(medicine).build());
        }

        return result;
    }

    public static List<Chips> mapFilterToChips(GetDoctorVisitsFilter filter) {
        List<Chips> result = new ArrayList<>();

        DateIntervalChips dateIntervalChips = mapFilterToDateIntervalChips(filter.getFromDate(), filter.getToDate());
        if (dateIntervalChips != null) {
            result.add(dateIntervalChips);
        }

        for (Doctor doctor : filter.getSelectedItems()) {
            result.add(DoctorChips.builder().doctor(doctor).build());
        }

        return result;
    }

    @Nullable
    private static DateIntervalChips mapFilterToDateIntervalChips(@Nullable LocalDate fromDate,
                                                                  @Nullable LocalDate toDate) {
        if (fromDate != null || toDate != null) {
            return DateIntervalChips.builder().fromDate(fromDate).toDate(toDate).build();
        }
        return null;
    }
}
