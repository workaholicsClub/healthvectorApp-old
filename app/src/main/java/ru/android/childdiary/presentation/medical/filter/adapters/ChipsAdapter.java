package ru.android.childdiary.presentation.medical.filter.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;

public class ChipsAdapter extends BaseRecyclerViewAdapter<Chips, ChipsAdapter.ChipsViewHolder> {
    public ChipsAdapter(Context context) {
        super(context);
    }

    @Override
    protected ChipsViewHolder createViewHolder(ViewGroup parent) {
        View v = inflater.inflate(R.layout.chips_item, parent, false);
        return new ChipsViewHolder(v);
    }

    @Override
    public boolean areItemsTheSame(Chips oldItem, Chips newItem) {
        return oldItem.sameAs(newItem);
    }

    static class ChipsViewHolder extends BaseRecyclerViewHolder<Chips> {
        @BindView(R.id.textView)
        TextView textView;

        public ChipsViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(Context context, Sex sex, Chips item) {
            super.bind(context, sex, item);
            textView.setText(item.getText(context));
        }
    }
}
