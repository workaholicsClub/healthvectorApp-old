package ru.android.childdiary.presentation.development.partitions.achievements.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.development.achievement.ConcreteAchievement;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewHolder;
import ru.android.childdiary.utils.ObjectUtils;
import ru.android.childdiary.utils.strings.DateUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class ConcreteAchievementViewHolder extends SwipeViewHolder<ConcreteAchievement, ConcreteAchievementSwipeActionListener, ConcreteAchievementActionListener> {
    @BindView(R.id.swipeLayout)
    SwipeLayout swipeLayout;

    @BindView(R.id.bottomView)
    View bottomView;

    @BindView(R.id.actionDelete)
    ImageView imageViewDelete;

    @BindView(R.id.textViewDate)
    TextView textViewDate;

    @BindView(R.id.textViewConcreteAchievement)
    TextView textViewConcreteAchievement;

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindDimen(R.dimen.event_row_corner_radius)
    float corner;

    public ConcreteAchievementViewHolder(View itemView,
                                         @NonNull ConcreteAchievementActionListener itemActionListener,
                                         @NonNull ConcreteAchievementSwipeActionListener swipeActionListener) {
        super(itemView, itemActionListener, swipeActionListener);
    }

    @Override
    public void bind(Context context, @Nullable Sex sex, ConcreteAchievement item) {
        super.bind(context, sex, item);

        String dateStr = DateUtils.date(context, item.getDate());
        textViewDate.setText(dateStr == null ? context.getString(R.string.fill_achievement_date) : dateStr);
        textViewConcreteAchievement.setText(item.getName());
        Drawable placeholder = ContextCompat.getDrawable(context, R.drawable.ic_placeholder_photo);
        Drawable photo = ResourcesUtils.getPhotoDrawable(context, item.getImageFileName(), placeholder);
        imageView.setImageDrawable(photo);

        //noinspection deprecation
        imageViewDelete.setBackgroundDrawable(ResourcesUtils.getShape(ThemeUtils.getColorAccent(context, sex), corner));

        swipeLayout.setRightSwipeEnabled(ObjectUtils.isFalse(item.getIsPredefined()));
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, bottomView);
    }

    @Override
    public SwipeLayout getSwipeLayout() {
        return swipeLayout;
    }

    @OnClick(R.id.contentView)
    void onContentViewClick() {
        itemActionListener.edit(item);
    }

    @OnClick(R.id.actionDelete)
    void onDeleteClick() {
        swipeActionListener.delete(this);
    }
}
