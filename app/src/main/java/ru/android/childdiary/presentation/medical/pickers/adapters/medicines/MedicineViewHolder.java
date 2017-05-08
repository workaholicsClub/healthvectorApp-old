package ru.android.childdiary.presentation.medical.pickers.adapters.medicines;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.presentation.medical.pickers.adapters.core.BaseItemViewHolder;

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
}
