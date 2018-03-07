package ru.android.healthvector.presentation.core.fields.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.Arrays;

import ru.android.healthvector.data.types.FeedType;
import ru.android.healthvector.utils.strings.StringUtils;

public class FeedTypeAdapter extends SpinnerItemAdapter<FeedType, FeedTypeAdapter.ViewHolder> {
    public FeedTypeAdapter(Context context) {
        super(context, Arrays.asList(FeedType.values()));
    }

    @Override
    protected ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    static class ViewHolder extends SpinnerItemViewHolder<FeedType> {
        public ViewHolder(View view) {
            super(view);
        }

        @Nullable
        @Override
        protected String getTextForValue(Context context, @NonNull FeedType item) {
            return StringUtils.feedType(context, item);
        }
    }
}
