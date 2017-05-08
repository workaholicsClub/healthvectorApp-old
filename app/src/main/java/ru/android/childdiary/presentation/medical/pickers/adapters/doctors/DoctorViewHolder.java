package ru.android.childdiary.presentation.medical.pickers.adapters.doctors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.presentation.medical.pickers.adapters.core.BaseItemViewHolder;

public class DoctorViewHolder extends BaseItemViewHolder<Doctor,
        DoctorSwipeActionListener,
        DoctorActionListener> {
    @BindView(R.id.textView)
    TextView textView;

    public DoctorViewHolder(View itemView,
                            @NonNull DoctorActionListener itemActionListener,
                            @NonNull DoctorSwipeActionListener swipeActionListener) {
        super(itemView, itemActionListener, swipeActionListener);
    }

    @Nullable
    @Override
    protected String getTextForValue(Context context, Doctor item) {
        return item.getName();
    }
}
