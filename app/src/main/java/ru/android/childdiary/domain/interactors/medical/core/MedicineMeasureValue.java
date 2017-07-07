package ru.android.childdiary.domain.interactors.medical.core;

import android.support.annotation.Nullable;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MedicineMeasureValue implements Serializable {
    @Nullable
    Double amount;

    @Nullable
    MedicineMeasure medicineMeasure;
}
