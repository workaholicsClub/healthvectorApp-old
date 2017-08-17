package ru.android.childdiary.presentation.development.partitions.testing.adapters.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.development.testing.data.tests.core.Test;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;

public class TestViewHolder extends BaseRecyclerViewHolder<Test> {
    private final TestClickListener listener;

    @BindView(R.id.textView)
    TextView textView;

    public TestViewHolder(View itemView, @NonNull TestClickListener listener) {
        super(itemView);
        this.listener = listener;
    }

    @Override
    protected void bind(Context context, @Nullable Sex sex) {
        textView.setText(item.getName());
    }

    @OnClick(R.id.contentView)
    void onContentViewClick() {
        listener.showTestDetails(item);
    }
}
