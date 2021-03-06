package ru.android.healthvector.presentation.development.partitions.testing.adapters.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import ru.android.healthvector.R;
import ru.android.healthvector.domain.development.testing.data.tests.core.Test;
import ru.android.healthvector.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;

public class TestAdapter extends BaseRecyclerViewAdapter<Test, TestViewHolder> {
    private final TestClickListener listener;

    public TestAdapter(Context context, @NonNull TestClickListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected TestViewHolder createUserViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.test_item, parent, false);
        return new TestViewHolder(v, listener);
    }

    @Override
    public boolean areItemsTheSame(Test oldItem, Test newItem) {
        return oldItem.getTestType() == newItem.getTestType();
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
