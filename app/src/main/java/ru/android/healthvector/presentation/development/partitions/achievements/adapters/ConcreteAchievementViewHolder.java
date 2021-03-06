package ru.android.healthvector.presentation.development.partitions.achievements.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import lombok.Getter;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.AchievementType;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.domain.development.achievement.data.ConcreteAchievement;
import ru.android.healthvector.presentation.core.adapters.swipe.SwipeViewHolder;
import ru.android.healthvector.utils.strings.DateUtils;
import ru.android.healthvector.utils.strings.StringUtils;
import ru.android.healthvector.utils.strings.TimeUtils;
import ru.android.healthvector.utils.ui.ResourcesUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;

class ConcreteAchievementViewHolder extends SwipeViewHolder<ConcreteAchievementItem, ConcreteAchievementSwipeActionListener, ConcreteAchievementItemActionListener> {
    // concrete achievement declarations
    @Nullable
    @Getter
    @BindView(R.id.swipeLayout)
    SwipeLayout swipeLayout;

    @Nullable
    @BindView(R.id.bottomView)
    View bottomView;

    @Nullable
    @BindView(R.id.actionDelete)
    ImageView imageViewDelete;

    @Nullable
    @BindView(R.id.textViewDate)
    TextView textViewDate;

    @Nullable
    @BindView(R.id.textViewConcreteAchievement)
    TextView textViewConcreteAchievement;

    @Nullable
    @BindView(R.id.imageView)
    ImageView imageView;

    @BindDimen(R.dimen.event_row_corner_radius)
    float corner;

    // achievement type declarations
    @Nullable
    @BindView(R.id.textView)
    TextView textView;

    @Nullable
    @BindView(R.id.imageViewArrow)
    ImageView imageViewArrow;

    @Nullable
    private Sex sex;

    public ConcreteAchievementViewHolder(View itemView,
                                         @Nullable Sex sex,
                                         boolean isChild,
                                         @NonNull ConcreteAchievementItemActionListener itemActionListener,
                                         @NonNull ConcreteAchievementSwipeActionListener swipeActionListener) {
        super(itemView, itemActionListener, swipeActionListener);
        this.sex = sex;
        if (isChild) {
            setupBackgrounds(itemView.getContext());
        }
    }

    private void setupBackgrounds(Context context) {
        assert imageViewDelete != null;
        //noinspection deprecation
        imageViewDelete.setBackgroundDrawable(ResourcesUtils.getShape(ThemeUtils.getColorAccent(context, sex), corner));
    }

    @Override
    protected void bind(Context context, @Nullable Sex sex) {
        if (getItem().isGroup()) {
            setupGroup(context, sex);
        } else {
            setupChild(context, sex);
        }
    }

    private void setupGroup(Context context, @Nullable Sex sex) {
        AchievementType achievementType = getItem().getAchievementType();
        assert textView != null;
        assert imageViewArrow != null;
        textView.setText(StringUtils.achievementType(context, achievementType));
        imageViewArrow.setImageResource(getItem().isExpanded()
                ? R.drawable.arrow_up_black : R.drawable.arrow_down_black);
    }

    private void setupChild(Context context, @Nullable Sex sex) {
        ConcreteAchievement concreteAchievement = getItem().getConcreteAchievement();
        String valueStr;

        if (concreteAchievement.getDate() == null) {
            double fromMonths = concreteAchievement.getFromAge();
            String fromAgeStr = TimeUtils.age(context, fromMonths);
            if (concreteAchievement.getToAge() == null) {
                valueStr = fromAgeStr;
            } else {
                double toMonths = concreteAchievement.getToAge();
                String toAgeStr = TimeUtils.age(context, toMonths);
                valueStr = context.getString(R.string.from_value_to_value, fromAgeStr, toAgeStr);
            }
        } else {
            String dateStr = DateUtils.date(context, concreteAchievement.getDate());
            TimeUtils.Age age = TimeUtils.getAge(concreteAchievement.getChild().getBirthDate(), concreteAchievement.getDate());
            String ageStr = TimeUtils.age(context, age);
            valueStr = context.getString(R.string.two_values, dateStr, ageStr);
        }

        assert textViewDate != null;
        assert textViewConcreteAchievement != null;
        assert imageView != null;
        assert swipeLayout != null;
        textViewDate.setText(valueStr);
        textViewConcreteAchievement.setText(concreteAchievement.getName());
        Drawable photo = ResourcesUtils.getPhotoDrawable(context, concreteAchievement.getImageFileName());
        imageView.setImageDrawable(photo);
        imageView.setVisibility(photo == null ? View.GONE : View.VISIBLE);

        if (sex != this.sex) {
            this.sex = sex;
            setupBackgrounds(context);
        }

        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, bottomView);
    }

    @OnClick(R.id.contentView)
    void onContentViewClick() {
        itemActionListener.edit(getItem());
    }

    @Optional
    @OnClick(R.id.actionDelete)
    void onDeleteClick() {
        swipeActionListener.delete(this);
    }
}
