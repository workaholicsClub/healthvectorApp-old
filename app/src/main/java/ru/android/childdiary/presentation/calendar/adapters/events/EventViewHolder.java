package ru.android.childdiary.presentation.calendar.adapters.events;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewHolder;
import ru.android.childdiary.utils.strings.DateUtils;
import ru.android.childdiary.utils.strings.EventUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

class EventViewHolder extends SwipeViewHolder<MasterEvent, EventSwipeActionListener, EventActionListener> {
    private static final double ALPHA_INCREASING_COEF = 1.5;
    private static final int FADE_DURATION_MS = 500;

    @Getter
    @BindView(R.id.swipeLayout)
    SwipeLayout swipeLayout;

    @BindView(R.id.bottomView)
    View bottomView;

    @BindView(R.id.actionsView)
    View actionsView;

    @BindView(R.id.eventView)
    View eventView;

    @BindView(R.id.textViewTime)
    TextView textViewTime;

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.textViewDescription)
    TextView textViewDescription;

    @BindView(R.id.eventRowActionDone)
    View actionDoneView;

    @BindView(R.id.delimiter1)
    View delimiter1;

    @BindView(R.id.imageViewDoneOrExpired)
    ImageView imageViewDoneOrExpired;

    @BindView(R.id.imageViewRepeating)
    ImageView imageViewRepeating;

    @BindDimen(R.dimen.event_row_corner_radius)
    float corner;

    public EventViewHolder(View itemView, @NonNull EventActionListener itemActionListener, @NonNull EventSwipeActionListener swipeActionListener) {
        super(itemView, itemActionListener, swipeActionListener);
    }

    @Override
    protected void bind(Context context, @Nullable Sex sex) {
        //noinspection deprecation
        eventView.setBackgroundDrawable(
                getEventViewBackgroundDrawable(ResourcesUtils.getEventColor(context, sex, item)));
        //noinspection deprecation
        actionsView.setBackgroundDrawable(
                getActionsViewBackgroundDrawable(ThemeUtils.getColorAccent(context, sex)));

        textViewTime.setText(DateUtils.time(context, item.getDateTime()));
        textViewTitle.setText(EventUtils.getTitle(context, item));
        textViewDescription.setText(EventUtils.getDescription(context, item));
        WidgetsUtils.hideIfEmpty(textViewDescription);

        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, bottomView);

        boolean showActionDone = EventUtils.canBeDone(item);
        actionDoneView.setVisibility(showActionDone ? View.VISIBLE : View.GONE);
        delimiter1.setVisibility(showActionDone ? View.VISIBLE : View.GONE);

        boolean isDone = EventUtils.isDone(item);
        boolean isExpired = EventUtils.isExpired(item);
        int resId = showActionDone ? (isDone ? R.drawable.ic_event_done : (isExpired ? R.drawable.ic_event_expired : 0)) : 0;
        imageViewDoneOrExpired.setImageResource(resId);
        imageViewDoneOrExpired.setVisibility(resId == 0 ? View.GONE : View.VISIBLE);
        imageViewRepeating.setVisibility(item.getLinearGroup() == null ? View.GONE : View.VISIBLE);
    }

    @Override
    public void updatePartially(Context context, MasterEvent event) {
        EventType eventType = event.getEventType();
        if (eventType != EventType.OTHER) {
            textViewDescription.setText(EventUtils.getDescription(context, event));
        }
    }

    private Drawable getEventViewBackgroundDrawable(@ColorInt int color) {
        int alpha = (int) (Color.alpha(color) * ALPHA_INCREASING_COEF);
        alpha = alpha < 0 || alpha > 255 ? 255 : alpha;
        @ColorInt int colorPressed = ColorUtils.setAlphaComponent(color, alpha);
        StateListDrawable background = new StateListDrawable();
        background.setEnterFadeDuration(FADE_DURATION_MS);
        background.setExitFadeDuration(FADE_DURATION_MS);
        background.addState(new int[]{android.R.attr.state_pressed}, ResourcesUtils.getShape(colorPressed, corner));
        background.addState(new int[]{}, ResourcesUtils.getShape(color, corner));
        return background;
    }

    private Drawable getActionsViewBackgroundDrawable(@ColorInt int color) {
        return ResourcesUtils.getShape(color, corner);
    }

    @OnClick(R.id.eventView)
    void onEventViewClick() {
        itemActionListener.edit(item);
    }

    @OnClick(R.id.eventRowActionDelete)
    void onDeleteClick() {
        swipeActionListener.delete(this);
    }

    @OnClick(R.id.eventRowActionMove)
    void onMoveClick() {
        swipeActionListener.move(this);
    }

    @OnClick(R.id.eventRowActionDone)
    void onDoneClick() {
        swipeActionListener.done(this);
    }
}
