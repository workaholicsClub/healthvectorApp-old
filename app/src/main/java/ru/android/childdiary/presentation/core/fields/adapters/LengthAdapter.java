package ru.android.childdiary.presentation.core.fields.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import ru.android.childdiary.utils.TimeUtils;

public class LengthAdapter extends SpinnerItemAdapter<Integer, LengthAdapter.ViewHolder> {
    public LengthAdapter(Context context, List<Integer> lengthList) {
        super(context, lengthList);
    }

    @Override
    protected LengthAdapter.ViewHolder createViewHolder(View view) {
        return new LengthAdapter.ViewHolder(view);
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
