package ru.android.childdiary.presentation.medical.adapters.medicines;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.presentation.medical.adapters.core.BaseMedicalItemViewHolder;
import ru.android.childdiary.utils.DateUtils;
import ru.android.childdiary.utils.DoubleUtils;
import ru.android.childdiary.utils.ObjectUtils;

class MedicineTakingViewHolder extends BaseMedicalItemViewHolder<MedicineTaking, MedicineTakingSwipeActionListener, MedicineTakingActionListener> {
    public MedicineTakingViewHolder(View itemView,
                                    @NonNull MedicineTakingActionListener itemActionListener,
                                    @NonNull MedicineTakingSwipeActionListener swipeActionListener) {
        super(itemView, itemActionListener, swipeActionListener);
    }

    @Override
    @Nullable
    protected String getDateText(Context context, MedicineTaking item) {
        return DateUtils.date(context, item.getDateTime());
    }

    @Override
    @Nullable
    protected String getTimeText(Context context, MedicineTaking item) {
        return DateUtils.time(context, item.getDateTime());
    }

    @Override
    @Nullable
    protected String getTitleText(Context context, MedicineTaking item) {
        return item.getMedicine() == null ? null : item.getMedicine().getName();
    }

    @Override
    @Nullable
    protected String getDescriptionText(Context context, MedicineTaking item) {
        if (ObjectUtils.isPositive(item.getAmount())
                && item.getMedicineMeasure() != null
                && !TextUtils.isEmpty(item.getMedicineMeasure().getName())) {
            String amount = DoubleUtils.multipleUnitFormat(item.getAmount());
            String medicineMeasure = item.getMedicineMeasure().getName();
            return context.getString(R.string.medicine_measure_format, amount, medicineMeasure);
        } else {
            return null;
        }
    }

    @OnClick(R.id.actionDelete)
    void onDeleteClick() {
        swipeActionListener.delete(this);
    }
}
