package ru.android.childdiary.presentation.dictionaries.medicinemeasure.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.dictionaries.medicinemeasure.data.MedicineMeasure;
import ru.android.childdiary.presentation.dictionaries.core.BaseItemViewHolder;

public class MedicineMeasureViewHolder extends BaseItemViewHolder<MedicineMeasure,
        MedicineMeasureSwipeActionListener,
        MedicineMeasureActionListener> {
    @BindView(R.id.textView)
    TextView textView;

    public MedicineMeasureViewHolder(View itemView,
                                     @Nullable Sex sex,
                                     @NonNull MedicineMeasureActionListener itemActionListener,
                                     @NonNull MedicineMeasureSwipeActionListener swipeActionListener) {
        super(itemView, sex, itemActionListener, swipeActionListener);
    }

    @Nullable
    @Override
    protected String getTextForValue(Context context, MedicineMeasure item) {
        return item.getName();
    }

    @OnClick(R.id.actionDelete)
    void onDeleteClick() {
        swipeActionListener.delete(this);
    }
}
