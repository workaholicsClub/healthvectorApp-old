package ru.android.healthvector.presentation.dictionaries.doctors.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.domain.dictionaries.doctors.data.Doctor;
import ru.android.healthvector.presentation.dictionaries.core.BaseItemViewHolder;

public class DoctorViewHolder extends BaseItemViewHolder<Doctor,
        DoctorSwipeActionListener,
        DoctorActionListener> {
    @BindView(R.id.textView)
    TextView textView;

    public DoctorViewHolder(View itemView,
                            @Nullable Sex sex,
                            @NonNull DoctorActionListener itemActionListener,
                            @NonNull DoctorSwipeActionListener swipeActionListener) {
        super(itemView, sex, itemActionListener, swipeActionListener);
    }

    @Nullable
    @Override
    protected String getTextForValue(Context context, Doctor item) {
        return item.getName();
    }

    @OnClick(R.id.actionDelete)
    void onDeleteClick() {
        swipeActionListener.delete(this);
    }
}
