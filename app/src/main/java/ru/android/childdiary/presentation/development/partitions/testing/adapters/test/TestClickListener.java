package ru.android.childdiary.presentation.development.partitions.testing.adapters.test;

import android.support.annotation.NonNull;

import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;

public interface TestClickListener {
    void showTestDetails(@NonNull Test test);
}
