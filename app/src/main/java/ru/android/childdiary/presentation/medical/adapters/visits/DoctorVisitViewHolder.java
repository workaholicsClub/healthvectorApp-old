package ru.android.childdiary.presentation.medical.adapters.visits;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.presentation.medical.adapters.core.BaseMedicalItemViewHolder;
import ru.android.childdiary.utils.DateUtils;

class DoctorVisitViewHolder extends BaseMedicalItemViewHolder<DoctorVisit, DoctorVisitSwipeActionListener, DoctorVisitActionListener> {
    public DoctorVisitViewHolder(View itemView,
                                 @NonNull DoctorVisitActionListener itemActionListener,
                                 @NonNull DoctorVisitSwipeActionListener swipeActionListener) {
        super(itemView, itemActionListener, swipeActionListener);
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
}
