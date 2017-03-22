package ru.android.childdiary.presentation.calendar.adapters.events;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.utils.DateUtils;
import ru.android.childdiary.utils.StringUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

class EventViewHolder extends RecyclerView.ViewHolder {
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

    @BindView(R.id.textViewEventType)
    TextView textViewEventType;

    @BindView(R.id.textViewDescription)
    TextView textViewDescription;

    @BindView(R.id.eventRowActionDone)
    View actionDoneView;

    @BindView(R.id.delimiter1)
    View delimiter1;

    @BindDimen(R.dimen.event_row_corner_radius)
    float corner;

    @Getter
    private MasterEvent event;
    private EventActionListener eventActionListener;
    private SwipeActionListener swipeActionListener;

    public EventViewHolder(View itemView, @NonNull EventActionListener eventActionListener, @NonNull SwipeActionListener swipeActionListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.swipeActionListener = swipeActionListener;
        this.eventActionListener = eventActionListener;
    }

    public void bind(Context context, int position, Sex sex, MasterEvent event) {
        this.event = event;

        //noinspection deprecation
        eventView.setBackgroundDrawable(
                getEventViewBackgroundDrawable(ResourcesUtils.getEventColor(context, sex, event)));
        //noinspection deprecation
        actionsView.setBackgroundDrawable(
                getActionsViewBackgroundDrawable(ThemeUtils.getColorAccent(context, sex)));

        textViewTime.setText(DateUtils.time(event.getDateTime().toLocalTime()));
        textViewEventType.setText(StringUtils.eventType(context, event.getEventType()));
        textViewDescription.setText(event.getDescription());

        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, bottomView);

        boolean showActionDone = event.getEventType() == EventType.OTHER;
        actionDoneView.setVisibility(showActionDone ? View.VISIBLE : View.GONE);
        delimiter1.setVisibility(showActionDone ? View.VISIBLE : View.GONE);
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

    @OnClick(R.id.eventRowActionDone)
    void onDoneClick() {
        swipeActionListener.done(this);
    }

    @OnClick(R.id.eventRowActionMove)
    void onMoveClick() {
        swipeActionListener.move(this);
    }

    @OnClick(R.id.eventRowActionEdit)
    void onEditClick() {
        swipeActionListener.edit(this);
    }

    @OnClick(R.id.eventView)
    void onEventViewClick() {
        eventActionListener.review(event);
    }

    @OnClick(R.id.eventRowActionDelete)
    void onDeleteClick() {
        swipeActionListener.delete(this);
    }

    interface SwipeActionListener {
        void done(EventViewHolder viewHolder);

        void move(EventViewHolder viewHolder);

        void edit(EventViewHolder viewHolder);

        void delete(EventViewHolder viewHolder);
    }
}
