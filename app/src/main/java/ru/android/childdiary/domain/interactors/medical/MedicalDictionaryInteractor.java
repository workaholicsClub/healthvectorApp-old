package ru.android.childdiary.domain.interactors.medical;

import android.support.annotation.NonNull;

import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.core.validation.EventValidationResult;

public interface MedicalDictionaryInteractor<T> {
    Observable<Boolean> controlDoneButton(@NonNull Observable<TextViewAfterTextChangeEvent> nameObservable);

    Observable<List<EventValidationResult>> controlFields(@NonNull Observable<TextViewAfterTextChangeEvent> nameObservable);
}
