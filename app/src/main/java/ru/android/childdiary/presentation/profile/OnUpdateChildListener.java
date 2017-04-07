package ru.android.childdiary.presentation.profile;

import android.support.annotation.NonNull;

import ru.android.childdiary.domain.interactors.child.Child;

interface OnUpdateChildListener {
    void onUpdateChild(@NonNull Child child);
}
