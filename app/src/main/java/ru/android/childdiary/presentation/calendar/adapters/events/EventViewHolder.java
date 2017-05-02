package ru.android.childdiary.presentation.calendar.adapters.events;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.graphics.ColorUtils;
import android.view.View;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.presentation.core.swipe.SwipeViewHolder;
import ru.android.childdiary.utils.DateUtils;
import ru.android.childdiary.utils.EventHelper;
import ru.android.childdiary.utils.StringUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

class EventViewHolder extends SwipeViewHolder<MasterEvent, EventSwipeActionListener, EventActionListener> {
    private static final double ALPHA_INCREASING_COEF = 1.5;
    private static final int FADE_DURATION_MS = 500;

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

    @BindDimen(R.dimen.event_row_corner_radius)
    float corner;

    public EventViewHolder(View itemView, @NonNull EventActionListener itemActionListener, @NonNull EventSwipeActionListener swipeActionListener) {
        super(itemView, itemActionListener, swipeActionListener);
    }

    @Override
    public void bind(Context context, Sex sex, MasterEvent event) {
        super.bind(context, sex, event);

        //noinspection deprecation
        eventView.setBackgroundDrawable(
                getEventViewBackgroundDrawable(ResourcesUtils.getEventColor(context, sex, event)));
        //noinspection deprecation
        actionsView.setBackgroundDrawable(
                getActionsViewBackgroundDrawable(ThemeUtils.getColorAccent(context, sex)));

        textViewTime.setText(DateUtils.time(context, event.getDateTime().toLocalTime()));
        EventType eventType = event.getEventType();
        if (eventType == EventType.OTHER) {
            textViewTitle.setText(EventHelper.getDescription(context, event));
            textViewDescription.setText(null);
        } else {
            textViewTitle.setText(StringUtils.eventType(context, eventType));
            textViewDescription.setText(EventHelper.getDescription(context, event));
        }

        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, bottomView);

        boolean showActionDone = EventHelper.canBeDone(event);
        actionDoneView.setVisibility(showActionDone ? View.VISIBLE : View.GONE);
        delimiter1.setVisibility(showActionDone ? View.VISIBLE : View.GONE);

        boolean isDone = EventHelper.isDone(event);
        boolean isExpired = EventHelper.isExpired(event);
        int left = showActionDone ? (isDone ? R.drawable.ic_event_done : (isExpired ? R.drawable.ic_event_expired : 0)) : 0;
        textViewTitle.setCompoundDrawablesWithIntrinsicBounds(left, 0, 0, 0);
    }

    @Override
    public void updatePartially(Context context, MasterEvent event) {
        EventType eventType = event.getEventType();
        if (eventType != EventType.OTHER) {
            textViewDescription.setText(EventHelper.getDescription(context, event));
        }
    }

    private Drawable getEventViewBackgroundDrawable(@ColorInt int color) {
        int alpha = (int) (Color.alpha(color) * ALPHA_INCREASING_COEF);
        alpha = alpha < 0 || alpha > 255 ? 255 : alpha;
        @ColorInt int colorPressed = ColorUtils.setAlphaComponent(color, alpha);
        StateListDrawable background = new StateListDrawable();
        background.setEnterFadeDuration(FADE_DURATION_MS);
        background.setExitFadeDuration(FADE_DURATION_MS);
        background.addState(new int[]{android.R.attr.state_pressed}, getShape(colorPressed));
        background.addState(new int[]{}, getShape(color));
        return background;
    }

    private Drawable getActionsViewBackgroundDrawable(@ColorInt int color) {
        return getShape(color);
    }

    private GradientDrawable getShape(@ColorInt int color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{corner, corner, corner, corner, corner, corner, corner, corner});
        shape.setColor(color);
        return shape;
    }

    @Override
    public SwipeLayout getSwipeLayout() {
        return swipeLayout;
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
