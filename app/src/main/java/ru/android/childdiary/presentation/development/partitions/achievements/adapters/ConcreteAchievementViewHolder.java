package ru.android.childdiary.presentation.development.partitions.achievements.adapters;

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
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.development.achievement.data.ConcreteAchievement;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeLayoutContainer;
import ru.android.childdiary.presentation.development.partitions.achievements.expandablerecyclerview.viewholders.ChildViewHolder;
import ru.android.childdiary.utils.strings.DateUtils;
import ru.android.childdiary.utils.strings.TimeUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class ConcreteAchievementViewHolder extends ChildViewHolder implements SwipeLayoutContainer {
    private final ConcreteAchievementItemActionListener itemActionListener;
    private final ConcreteAchievementSwipeActionListener swipeActionListener;

    @Getter
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

    @Getter
    private ConcreteAchievement concreteAchievement;

    public ConcreteAchievementViewHolder(View itemView,
                                         @NonNull ConcreteAchievementItemActionListener itemActionListener,
                                         @NonNull ConcreteAchievementSwipeActionListener swipeActionListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.itemActionListener = itemActionListener;
        this.swipeActionListener = swipeActionListener;
    }

    public void bind(Context context, @Nullable Sex sex, @NonNull ConcreteAchievement concreteAchievement) {
        this.concreteAchievement = concreteAchievement;

        String valueStr;

        if (concreteAchievement.getDate() == null) {
            int fromMonths = concreteAchievement.getFromAge().intValue();// TODO double 1.5 months
            TimeUtils.Age fromAge = TimeUtils.Age.builder().months(fromMonths).build();
            String fromAgeStr = TimeUtils.age(context, fromAge);
            if (concreteAchievement.getToAge() == null) {
                valueStr = fromAgeStr;
            } else {
                int toMonths = concreteAchievement.getToAge().intValue();// TODO double
                TimeUtils.Age toAge = TimeUtils.Age.builder().months(toMonths).build();
                String toAgeStr = TimeUtils.age(context, toAge);
                valueStr = context.getString(R.string.from_value_to_value, fromAgeStr, toAgeStr);
            }
        } else {
            String dateStr = DateUtils.date(context, concreteAchievement.getDate());
            TimeUtils.Age age = TimeUtils.getAge(concreteAchievement.getChild().getBirthDate(), concreteAchievement.getDate());
            String ageStr = TimeUtils.age(context, age);
            valueStr = context.getString(R.string.two_values, dateStr, ageStr);
        }

        textViewDate.setText(valueStr);
        textViewConcreteAchievement.setText(concreteAchievement.getName());
        Drawable photo = ResourcesUtils.getPhotoDrawable(context, concreteAchievement.getImageFileName());
        imageView.setImageDrawable(photo);
        imageView.setVisibility(photo == null ? View.GONE : View.VISIBLE);

        //noinspection deprecation
        imageViewDelete.setBackgroundDrawable(ResourcesUtils.getShape(ThemeUtils.getColorAccent(context, sex), corner));

        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, bottomView);
    }

    @OnClick(R.id.contentView)
    void onContentViewClick() {
        itemActionListener.edit(concreteAchievement);
    }

    @OnClick(R.id.actionDelete)
    void onDeleteClick() {
        swipeActionListener.delete(this);
    }
}
