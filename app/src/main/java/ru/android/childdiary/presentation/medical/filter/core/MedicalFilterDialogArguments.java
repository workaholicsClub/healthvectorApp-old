package ru.android.childdiary.presentation.medical.filter.core;

import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.core.BaseDialogArguments;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@Getter
public class MedicalFilterDialogArguments<T extends Serializable> extends BaseDialogArguments {
    @NonNull
    ArrayList<T> items;
    @NonNull
    ArrayList<T> selectedItems;
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
        this.items = new ArrayList<>(items);
        this.selectedItems = new ArrayList<>(selectedItems);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
}
