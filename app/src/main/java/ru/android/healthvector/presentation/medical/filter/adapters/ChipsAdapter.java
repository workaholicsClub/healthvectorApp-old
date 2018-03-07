package ru.android.healthvector.presentation.medical.filter.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.healthvector.presentation.core.adapters.recycler.BaseRecyclerViewHolder;

public class ChipsAdapter extends BaseRecyclerViewAdapter<Chips, ChipsAdapter.ChipsViewHolder> {
    @Nullable
    private final ChipsDeleteClickListener chipsDeleteClickListener;

    public ChipsAdapter(Context context, @Nullable ChipsDeleteClickListener chipsDeleteClickListener) {
        super(context);
        this.chipsDeleteClickListener = chipsDeleteClickListener;
    }

    @Override
    protected ChipsViewHolder createUserViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.chips_item, parent, false);
        return new ChipsViewHolder(v, chipsDeleteClickListener);
    }

    @Override
    public boolean areItemsTheSame(Chips oldItem, Chips newItem) {
        return oldItem.sameAs(newItem);
    }

    @Override
    public boolean paintDividers() {
        return false;
    }

    @Override
    public boolean useFooter() {
        return false;
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
        protected void bind(Context context, @Nullable Sex sex) {
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
