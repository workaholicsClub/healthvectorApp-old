package ru.android.childdiary.presentation.profile;

import android.support.annotation.NonNull;

import ru.android.childdiary.domain.interactors.child.data.Child;

interface OnUpdateChildListener {
    void onUpdateChild(@NonNull Child child);
}
