package ru.android.childdiary.presentation.medical.filter.adapters;

import android.content.Context;
import android.support.annotation.Nullable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.dictionaries.medicines.data.Medicine;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@Builder
public class MedicineChips implements Chips {
    @NonNull
    Medicine medicine;

    @Override
    @Nullable
    public String getText(Context context) {
        return medicine.getName();
    }

    @Override
    public boolean sameAs(Chips other) {
        return other instanceof MedicineChips
                && ObjectUtils.equals(medicine.getId(), ((MedicineChips) other).medicine.getId());
    }
}
