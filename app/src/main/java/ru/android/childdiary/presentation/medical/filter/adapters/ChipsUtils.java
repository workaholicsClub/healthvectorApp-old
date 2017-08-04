package ru.android.childdiary.presentation.medical.filter.adapters;

import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import ru.android.childdiary.domain.interactors.dictionaries.doctors.data.Doctor;
import ru.android.childdiary.domain.interactors.dictionaries.medicines.data.Medicine;
import ru.android.childdiary.domain.interactors.medical.requests.GetDoctorVisitsFilter;
import ru.android.childdiary.domain.interactors.medical.requests.GetMedicineTakingListFilter;

public class ChipsUtils {
    public static GetMedicineTakingListFilter mapToMedicineFilter(List<Chips> chips) {
        LocalDate[] dates = mapChipsToDateInterval(chips);

        List<Medicine> medicines = new ArrayList<>();
        //noinspection Convert2streamapi
        for (Chips chipsItem : chips) {
            if (chipsItem instanceof MedicineChips) {
                MedicineChips medicineChips = (MedicineChips) chipsItem;
                medicines.add(medicineChips.getMedicine());
            }
        }

        return GetMedicineTakingListFilter.builder()
                .fromDate(dates[0])
                .toDate(dates[1])
                .selectedItems(medicines)
                .build();
    }

    public static GetDoctorVisitsFilter mapToDoctorFilter(List<Chips> chips) {
        LocalDate[] dates = mapChipsToDateInterval(chips);

        List<Doctor> doctors = new ArrayList<>();
        //noinspection Convert2streamapi
        for (Chips chipsItem : chips) {
            if (chipsItem instanceof DoctorChips) {
                DoctorChips doctorChips = (DoctorChips) chipsItem;
                doctors.add(doctorChips.getDoctor());
            }
        }

        return GetDoctorVisitsFilter.builder()
                .fromDate(dates[0])
                .toDate(dates[1])
                .selectedItems(doctors)
                .build();
    }

    private static LocalDate[] mapChipsToDateInterval(List<Chips> chips) {
        LocalDate fromDate = null;
        LocalDate toDate = null;
        for (Chips chipsItem : chips) {
            if (chipsItem instanceof DateIntervalChips) {
                DateIntervalChips dateIntervalChips = (DateIntervalChips) chipsItem;
                fromDate = dateIntervalChips.getFromDate();
                toDate = dateIntervalChips.getToDate();
            }
        }
        return new LocalDate[]{fromDate, toDate};
    }

    public static List<Chips> mapFilterToChips(GetMedicineTakingListFilter filter) {
        List<Chips> result = new ArrayList<>();

        DateIntervalChips dateIntervalChips = mapFilterToDateIntervalChips(filter.getFromDate(), filter.getToDate());
        if (dateIntervalChips != null) {
            result.add(dateIntervalChips);
        }

        //noinspection Convert2streamapi
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

        //noinspection Convert2streamapi
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
