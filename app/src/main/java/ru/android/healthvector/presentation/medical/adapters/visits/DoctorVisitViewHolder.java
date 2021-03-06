package ru.android.healthvector.presentation.medical.adapters.visits;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.OnClick;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.domain.medical.data.DoctorVisit;
import ru.android.healthvector.presentation.medical.adapters.core.BaseMedicalItemViewHolder;
import ru.android.healthvector.utils.strings.DateUtils;

class DoctorVisitViewHolder extends BaseMedicalItemViewHolder<DoctorVisit, DoctorVisitSwipeActionListener, DoctorVisitActionListener> {
    public DoctorVisitViewHolder(View itemView,
                                 @Nullable Sex sex,
                                 @NonNull DoctorVisitActionListener itemActionListener,
                                 @NonNull DoctorVisitSwipeActionListener swipeActionListener) {
        super(itemView, sex, itemActionListener, swipeActionListener);
    }

    @Override
    @Nullable
    protected String getDateText(Context context, DoctorVisit item) {
        return DateUtils.date(context, item.getDateTime());
    }

    @Override
    @Nullable
    protected String getTimeText(Context context, DoctorVisit item) {
        return DateUtils.time(context, item.getDateTime());
    }

    @Override
    @Nullable
    protected String getTitleText(Context context, DoctorVisit item) {
        return item.getDoctor() == null ? null : item.getDoctor().getName();
    }

    @Override
    @Nullable
    protected String getDescriptionText(Context context, DoctorVisit item) {
        return item.getName();
    }

    @OnClick(R.id.actionDelete)
    void onDeleteClick() {
        swipeActionListener.delete(this);
    }

    @Override
    protected boolean isDone(DoctorVisit item) {
        return item.isDone();
    }
}
