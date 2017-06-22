package ru.android.childdiary.presentation.medical.filter.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;

public class ChipsAdapter extends BaseRecyclerViewAdapter<Chips, ChipsAdapter.ChipsViewHolder> {
    @Nullable
    private final ChipsDeleteClickListener chipsDeleteClickListener;

    public ChipsAdapter(Context context, @Nullable ChipsDeleteClickListener chipsDeleteClickListener) {
        super(context);
        this.chipsDeleteClickListener = chipsDeleteClickListener;
    }

    @Override
    public ChipsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.chips_item, parent, false);
        return new ChipsViewHolder(v, chipsDeleteClickListener);
    }

    @Override
    public boolean areItemsTheSame(Chips oldItem, Chips newItem) {
        return oldItem.sameAs(newItem);
    }

    public interface ChipsDeleteClickListener {
        void onChipsDeleteClick(Chips chips);
    }

    static class ChipsViewHolder extends BaseRecyclerViewHolder<Chips> {
        @Nullable
        private final ChipsDeleteClickListener chipsDeleteClickListener;
        @BindView(R.id.textView)
        TextView textView;

        public ChipsViewHolder(View itemView, @Nullable ChipsDeleteClickListener chipsDeleteClickListener) {
            super(itemView);
            this.chipsDeleteClickListener = chipsDeleteClickListener;
        }

        @Override
        public void bind(Context context, Sex sex, Chips item) {
            super.bind(context, sex, item);
            textView.setText(item.getText(context));
        }

        @OnClick(R.id.actionChipsDelete)
        void onClick() {
            if (chipsDeleteClickListener != null) {
                chipsDeleteClickListener.onChipsDeleteClick(item);
            }
        }
    }
}
