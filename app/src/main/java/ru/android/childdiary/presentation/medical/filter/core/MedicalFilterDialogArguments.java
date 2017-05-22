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
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class MedicalFilterDialogArguments<T extends Serializable> extends BaseDialogArguments {
    @NonNull
    ArrayList<T> items;
    @Nullable
    T selectedItem;
    @NonNull
    LocalDate fromDate;
    @NonNull
    LocalDate toDate;

    @Builder(builderMethodName = "medicalBuilder")
    public MedicalFilterDialogArguments(@Nullable Sex sex,
                                        @NonNull List<T> items,
                                        @Nullable T selectedItem,
                                        @NonNull LocalDate fromDate,
                                        @NonNull LocalDate toDate) {
        super(sex);
        this.items = new ArrayList<>(items);
        this.selectedItem = selectedItem;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
}
