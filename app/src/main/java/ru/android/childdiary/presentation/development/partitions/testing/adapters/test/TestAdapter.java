package ru.android.childdiary.presentation.development.partitions.testing.adapters.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.development.testing.Test;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.childdiary.utils.ObjectUtils;

public class TestAdapter extends BaseRecyclerViewAdapter<Test, TestViewHolder> {
    private final TestClickListener listener;

    public TestAdapter(Context context, @NonNull TestClickListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.test_item, parent, false);
        return new TestViewHolder(v, listener);
    }

    @Override
    public boolean areItemsTheSame(Test oldItem, Test newItem) {
        return ObjectUtils.equals(oldItem.getTestType(), newItem.getTestType());
    }
}
