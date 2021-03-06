package ru.android.healthvector.presentation.medical.filter.adapters;

import android.content.Context;
import android.support.annotation.Nullable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.dictionaries.doctors.data.Doctor;
import ru.android.healthvector.utils.ObjectUtils;

@Value
@Builder
public class DoctorChips implements Chips {
    @NonNull
    Doctor doctor;

    @Override
    @Nullable
    public String getText(Context context) {
        return doctor.getName();
    }

    @Override
    public boolean sameAs(Chips other) {
        return other instanceof DoctorChips
                && ObjectUtils.equals(doctor.getId(), ((DoctorChips) other).doctor.getId());
    }
}
