package ru.android.childdiary.presentation.core.fields.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import ru.android.childdiary.data.types.AchievementType;
import ru.android.childdiary.utils.strings.StringUtils;

public class AchievementTypeAdapter extends SpinnerItemAdapter<AchievementType, AchievementTypeAdapter.ViewHolder> {
    public AchievementTypeAdapter(Context context, AchievementType[] achievementTypes) {
        super(context, achievementTypes);
    }

    @Override
    protected ViewHolder createViewHolder(View view) {
        return new AchievementTypeAdapter.ViewHolder(view);
    }

    static class ViewHolder extends SpinnerItemViewHolder<AchievementType> {
        public ViewHolder(View view) {
            super(view);
        }

        @Nullable
        @Override
        protected String getTextForValue(Context context, @NonNull AchievementType item) {
            return StringUtils.achievementType(context, item);
        }
    }
}
