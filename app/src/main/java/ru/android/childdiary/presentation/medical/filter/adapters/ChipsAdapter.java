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
    protected ChipsViewHolder createViewHolder(ViewGroup parent) {
        View v = inflater.inflate(R.layout.chips_item, parent, false);
        return new ChipsViewHolder(v, chipsDeleteClickListener);
    }

    @Override
    public boolean areItemsTheSame(Chips oldItem, Chips newItem) {
        return oldItem.sameAs(newItem);
    }

    static class ChipsViewHolder extends BaseRecyclerViewHolder<Chips> {
        @BindView(R.id.textView)
        TextView textView;

        @Nullable
        private final ChipsDeleteClickListener chipsDeleteClickListener;

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

    public interface ChipsDeleteClickListener {
        void onChipsDeleteClick(Chips chips);
    }
}
