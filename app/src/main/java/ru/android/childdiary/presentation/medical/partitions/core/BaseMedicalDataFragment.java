package ru.android.childdiary.presentation.medical.partitions.core;

import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;

public abstract class BaseMedicalDataFragment extends AppPartitionFragment {
    public abstract SwipeViewAdapter getAdapter();
}
