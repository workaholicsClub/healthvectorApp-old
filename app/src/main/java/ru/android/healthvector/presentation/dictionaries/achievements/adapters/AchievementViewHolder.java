package ru.android.healthvector.presentation.dictionaries.achievements.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.domain.dictionaries.achievements.data.Achievement;
import ru.android.healthvector.presentation.dictionaries.core.BaseItemViewHolder;

public class AchievementViewHolder extends BaseItemViewHolder<Achievement,
        AchievementSwipeActionListener,
        AchievementActionListener> {
    @BindView(R.id.textView)
    TextView textView;

    public AchievementViewHolder(View itemView,
                                 @Nullable Sex sex,
                                 @NonNull AchievementActionListener itemActionListener,
                                 @NonNull AchievementSwipeActionListener swipeActionListener) {
        super(itemView, sex, itemActionListener, swipeActionListener);
    }

    @Nullable
    @Override
    protected String getTextForValue(Context context, Achievement item) {
        return item.getName();
    }

    @OnClick(R.id.actionDelete)
    void onDeleteClick() {
        swipeActionListener.delete(this);
    }
}
