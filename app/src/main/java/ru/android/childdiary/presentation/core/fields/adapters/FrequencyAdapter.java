package ru.android.childdiary.presentation.core.fields.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.List;

import ru.android.childdiary.utils.StringUtils;

public class FrequencyAdapter extends SpinnerItemAdapter<Integer, FrequencyAdapter.ViewHolder> {
    public FrequencyAdapter(Context context, List<Integer> frequencyList) {
        super(context, frequencyList);
    }

    @Override
    protected FrequencyAdapter.ViewHolder createViewHolder(View view) {
        return new FrequencyAdapter.ViewHolder(view);
    }

    static class ViewHolder extends SpinnerItemViewHolder<Integer> {
        public ViewHolder(View view) {
            super(view);
        }

        @Nullable
        @Override
        protected String getTextForValue(Context context, @NonNull Integer item) {
            return StringUtils.frequency(context, item);
        }
    }
}
