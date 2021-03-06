package ru.android.healthvector.presentation.development.partitions.testing.adapters.result;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import ru.android.healthvector.R;
import ru.android.healthvector.domain.development.testing.data.TestResult;
import ru.android.healthvector.presentation.core.adapters.swipe.FabController;
import ru.android.healthvector.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.healthvector.utils.ObjectUtils;

public class TestResultAdapter extends SwipeViewAdapter<TestResult, TestResultViewHolder, TestResultSwipeActionListener, TestResultActionListener>
        implements TestResultSwipeActionListener {
    public TestResultAdapter(Context context, @NonNull TestResultActionListener itemActionListener, @Nullable FabController fabController) {
        super(context, itemActionListener, fabController);
    }

    @Override
    protected TestResultViewHolder createUserViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.test_result_item, parent, false);
        return new TestResultViewHolder(v, sex, itemActionListener, this);
    }

    @Override
    public boolean areItemsTheSame(TestResult oldItem, TestResult newItem) {
        return ObjectUtils.equals(oldItem.getId(), newItem.getId());
    }

    @Override
    public boolean paintDividers() {
        return true;
    }

    @Override
    public boolean useFooter() {
        return false;
    }
}
