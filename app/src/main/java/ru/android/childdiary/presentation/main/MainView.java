package ru.android.childdiary.presentation.main;

import android.support.annotation.Nullable;

import java.util.List;

import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseActivityView;

public interface MainView extends BaseActivityView {
    void childListLoaded(@Nullable Child activeChild, List<Child> childList);

    void addChild();

    void editChild(@Nullable Child child);

    void setActive(@Nullable Child child);
}
