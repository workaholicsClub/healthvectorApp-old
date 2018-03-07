package ru.android.healthvector.domain.medical.data;

import android.support.annotation.Nullable;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.healthvector.domain.dictionaries.medicinemeasure.data.MedicineMeasure;

@Value
@Builder
public class MedicineMeasureValue implements Serializable {
    @Nullable
    Double amount;

    @Nullable
    MedicineMeasure medicineMeasure;
}
