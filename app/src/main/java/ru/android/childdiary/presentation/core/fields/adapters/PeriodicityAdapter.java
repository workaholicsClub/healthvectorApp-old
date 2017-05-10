package ru.android.childdiary.presentation.core.fields.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import ru.android.childdiary.domain.interactors.core.PeriodicityType;
import ru.android.childdiary.utils.TimeUtils;

public class PeriodicityAdapter extends SpinnerItemAdapter<PeriodicityType, PeriodicityAdapter.ViewHolder> {
    public PeriodicityAdapter(Context context, List<PeriodicityType> periodicityList) {
        super(context, periodicityList);
    }

    @Override
    protected PeriodicityAdapter.ViewHolder createViewHolder(View view) {
        return new PeriodicityAdapter.ViewHolder(view);
    }

    static class ViewHolder extends SpinnerItemViewHolder<PeriodicityType> {
        public ViewHolder(View view) {
            super(view);
        }

        @Override
        protected String getTextForValue(Context context, @NonNull PeriodicityType item) {
            return TimeUtils.periodicity(context, item);
        }
    }
}
