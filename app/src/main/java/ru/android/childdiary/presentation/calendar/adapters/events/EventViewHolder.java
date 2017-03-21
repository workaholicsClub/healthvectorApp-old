package ru.android.childdiary.presentation.calendar.adapters.events;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
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

    @BindView(R.id.delimiter3)
    View delimiter3;

    @BindDimen(R.dimen.event_row_corner_radius)
    float corner;

    @Getter
    private MasterEvent event;
    private SwipeActionListener listener;

    public EventViewHolder(View itemView, @NonNull SwipeActionListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
    }

    public void bind(Context context, int position, Sex sex, MasterEvent event) {
        this.event = event;

        //noinspection deprecation
        actionsView.setBackgroundDrawable(getShape(ThemeUtils.getColorAccent(context, sex)));
        //noinspection deprecation
        eventView.setBackgroundDrawable(getShape(ResourcesUtils.getEventColor(context, sex, event)));

        textViewTime.setText(DateUtils.time(event.getDateTime().toLocalTime()));
        textViewEventType.setText(StringUtils.eventType(context, event.getEventType()));
        textViewDescription.setText(event.getDescription());

        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, bottomView);

        boolean showActionDone = event.getEventType() == EventType.OTHER;
        actionDoneView.setVisibility(showActionDone ? View.VISIBLE : View.GONE);
        delimiter3.setVisibility(showActionDone ? View.VISIBLE : View.GONE);
    }

    private GradientDrawable getShape(int color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{corner, corner, corner, corner, corner, corner, corner, corner});
        shape.setColor(color);
        return shape;
    }

    @OnClick(R.id.eventRowActionDelete)
    void onDeleteClick() {
        listener.delete(this);
    }

    @OnClick(R.id.eventRowActionMove)
    void onMoveClick() {
        listener.move(this);
    }

    @OnClick(R.id.eventRowActionEdit)
    void onEditClick() {
        listener.edit(this);
    }

    @OnClick(R.id.eventRowActionDone)
    void onDoneClick() {
        listener.done(this);
    }

    interface SwipeActionListener {
        void delete(EventViewHolder viewHolder);

        void move(EventViewHolder viewHolder);

        void edit(EventViewHolder viewHolder);

        void done(EventViewHolder viewHolder);
    }
}
