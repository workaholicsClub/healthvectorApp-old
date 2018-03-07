package ru.android.healthvector.presentation.medical.filter.core;

import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.presentation.core.BaseDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@Getter
public class MedicalFilterDialogArguments<T extends Serializable> extends BaseDialogArguments {
    @NonNull
    List<T> items;
    @NonNull
    List<T> selectedItems;
    @Nullable
    LocalDate fromDate;
    @Nullable
    LocalDate toDate;

    @Builder(builderMethodName = "medicalBuilder")
    public MedicalFilterDialogArguments(@Nullable Sex sex,
                                        @NonNull List<T> items,
                                        @NonNull List<T> selectedItems,
                                        @Nullable LocalDate fromDate,
                                        @Nullable LocalDate toDate) {
        super(sex);
        this.items = Collections.unmodifiableList(items);
        this.selectedItems = Collections.unmodifiableList(selectedItems);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
}
