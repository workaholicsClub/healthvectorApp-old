package ru.android.childdiary.presentation.medical.adapters.medicines;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.medical.data.MedicineTaking;
import ru.android.childdiary.presentation.medical.adapters.core.BaseMedicalItemViewHolder;
import ru.android.childdiary.utils.strings.DateUtils;
import ru.android.childdiary.utils.strings.StringUtils;

class MedicineTakingViewHolder extends BaseMedicalItemViewHolder<MedicineTaking, MedicineTakingSwipeActionListener, MedicineTakingActionListener> {
    public MedicineTakingViewHolder(View itemView,
                                    @Nullable Sex sex,
                                    @NonNull MedicineTakingActionListener itemActionListener,
                                    @NonNull MedicineTakingSwipeActionListener swipeActionListener) {
        super(itemView, sex, itemActionListener, swipeActionListener);
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
        return StringUtils.medicineMeasureValue(context, item.getAmount(), item.getMedicineMeasure());
    }

    @OnClick(R.id.actionDelete)
    void onDeleteClick() {
        swipeActionListener.delete(this);
    }

    @Override
    protected boolean isDone(MedicineTaking item) {
        return item.isDone();
    }
}
