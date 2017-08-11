package ru.android.childdiary.presentation.development.partitions.testing.adapters.result;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.development.testing.data.TestResult;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.childdiary.utils.ObjectUtils;

public class TestResultAdapter extends SwipeViewAdapter<TestResult, TestResultViewHolder, TestResultSwipeActionListener, TestResultActionListener>
        implements TestResultSwipeActionListener {
    public TestResultAdapter(Context context, @NonNull TestResultActionListener itemActionListener, @Nullable FabController fabController) {
        super(context, itemActionListener, fabController);
    }

    @Override
    protected TestResultViewHolder createViewHolder(ViewGroup parent) {
        View v = inflater.inflate(R.layout.test_result_item, parent, false);
        return new TestResultViewHolder(v, itemActionListener, this);
    }

    @Override
    public boolean areItemsTheSame(TestResult oldItem, TestResult newItem) {
        return ObjectUtils.equals(oldItem.getId(), newItem.getId());
    }
}
