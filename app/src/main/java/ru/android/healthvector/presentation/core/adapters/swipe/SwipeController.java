package ru.android.healthvector.presentation.core.adapters.swipe;

import android.support.annotation.Nullable;

public interface SwipeController {
    void closeAllItems();

    void setFabController(@Nullable FabController fabController);

    void updateFabState();
}
