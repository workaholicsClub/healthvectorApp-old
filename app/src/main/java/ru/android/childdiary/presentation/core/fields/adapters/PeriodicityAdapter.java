package ru.android.childdiary.presentation.core.fields.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import ru.android.childdiary.utils.TimeUtils;

public class PeriodicityAdapter extends SpinnerItemAdapter<Integer, PeriodicityAdapter.ViewHolder> {
    public PeriodicityAdapter(Context context, List<Integer> periodicityList) {
        super(context, periodicityList);
    }

    @Override
    protected PeriodicityAdapter.ViewHolder createViewHolder(View view) {
        return new PeriodicityAdapter.ViewHolder(view);
    }

    static class ViewHolder extends SpinnerItemViewHolder<Integer> {
        public ViewHolder(View view) {
            super(view);
        }

        @Override
        protected String getTextForValue(Context context, @NonNull Integer item) {
            return TimeUtils.durationLong(context, item);
        }
    }
}
