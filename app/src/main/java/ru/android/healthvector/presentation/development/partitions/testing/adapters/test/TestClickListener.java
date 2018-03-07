package ru.android.healthvector.presentation.development.partitions.testing.adapters.test;

import android.support.annotation.NonNull;

import ru.android.healthvector.domain.development.testing.data.tests.core.Test;

public interface TestClickListener {
    void showTestDetails(@NonNull Test test);
}
