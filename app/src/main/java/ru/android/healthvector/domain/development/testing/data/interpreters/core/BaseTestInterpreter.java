package ru.android.healthvector.domain.development.testing.data.interpreters.core;

import android.content.Context;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.android.healthvector.domain.development.testing.data.processors.core.BaseTestProcessor;
import ru.android.healthvector.domain.development.testing.data.tests.core.Test;

@ToString
@EqualsAndHashCode
public abstract class BaseTestInterpreter<T extends Test, TP extends BaseTestProcessor<T>> implements TestInterpreter {
    protected final Context context;
    @NonNull
    protected final TP testProcessor;
    @NonNull
    protected final T test;

    protected BaseTestInterpreter(Context context,
                                  @NonNull TP testProcessor) {
        this.context = context;
        this.testProcessor = testProcessor;
        this.test = testProcessor.getTest();
    }
}
