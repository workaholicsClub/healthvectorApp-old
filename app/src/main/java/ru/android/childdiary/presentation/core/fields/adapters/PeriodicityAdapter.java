package ru.android.childdiary.presentation.core.fields.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.List;

import ru.android.childdiary.domain.interactors.calendar.data.core.PeriodicityType;
import ru.android.childdiary.utils.strings.StringUtils;

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

        @Nullable
        @Override
        protected String getTextForValue(Context context, @NonNull PeriodicityType item) {
            return StringUtils.periodicity(context, item);
        }
    }
}
