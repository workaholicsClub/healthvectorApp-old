package ru.android.childdiary.domain.interactors.medical.requests;

import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;

@Value
@Builder(toBuilder = true)
public class GetMedicineTakingListFilter {
    @NonNull
    List<Medicine> selectedItems;
    @Nullable
    LocalDate fromDate;
    @Nullable
    LocalDate toDate;
}
