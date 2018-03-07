package ru.android.healthvector.domain.medical.requests;

import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;

@Value
@Builder
public class GetMedicineTakingListFilter {
    @NonNull
    List<Medicine> selectedItems;
    @Nullable
    LocalDate fromDate;
    @Nullable
    LocalDate toDate;
}
