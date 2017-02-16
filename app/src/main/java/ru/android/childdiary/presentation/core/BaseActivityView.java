package ru.android.childdiary.presentation.core;

import android.support.annotation.Nullable;

import java.util.List;

import ru.android.childdiary.domain.interactors.child.Child;

public interface BaseActivityView extends BaseView {
    void navigateToMain(Child child, List<Child> childList);

    void navigateToProfileEdit(@Nullable Child child);

    void finish();
}
