package ru.android.childdiary.presentation.dictionaries.medicines.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.dictionaries.medicines.data.Medicine;
import ru.android.childdiary.presentation.dictionaries.core.BaseItemViewHolder;

public class MedicineViewHolder extends BaseItemViewHolder<Medicine,
        MedicineSwipeActionListener,
        MedicineActionListener> {
    @BindView(R.id.textView)
    TextView textView;

    public MedicineViewHolder(View itemView,
                              @NonNull MedicineActionListener itemActionListener,
                              @NonNull MedicineSwipeActionListener swipeActionListener) {
        super(itemView, itemActionListener, swipeActionListener);
    }

    @Nullable
    @Override
    protected String getTextForValue(Context context, Medicine item) {
        return item.getName();
    }

    @OnClick(R.id.actionDelete)
    void onDeleteClick() {
        swipeActionListener.delete(this);
    }
}
