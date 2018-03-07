package ru.android.healthvector.presentation.profile;

import android.support.annotation.NonNull;

import ru.android.healthvector.domain.child.data.Child;

interface OnUpdateChildListener {
    void onUpdateChild(@NonNull Child child);
}
