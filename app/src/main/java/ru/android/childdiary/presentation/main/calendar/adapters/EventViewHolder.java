package ru.android.childdiary.presentation.main.calendar.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.utils.DateUtils;
import ru.android.childdiary.utils.StringUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class EventViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.actionsView)
    View actionsView;

    @BindView(R.id.rootView)
    View rootView;

    @BindView(R.id.textViewTime)
    TextView textViewTime;

    @BindView(R.id.textViewEventType)
    TextView textViewEventType;

    @BindView(R.id.textViewDescription)
    TextView textViewDescription;

    @BindDimen(R.dimen.event_row_corner)
    float corner;

    @BindColor(R.color.swipe_color)
    int swipeColor;

    public EventViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Context context, int position, Sex sex, MasterEvent item) {
        //noinspection deprecation
        actionsView.setBackgroundDrawable(getShape(swipeColor));
        //noinspection deprecation
        rootView.setBackgroundDrawable(getShape(ResourcesUtils.getEventColor(context, sex, item)));

        textViewTime.setText(DateUtils.time(item.getDateTime().toLocalTime()));
        textViewEventType.setText(StringUtils.eventType(context, item.getEventType()));
        textViewDescription.setText(item.getDescription());
    }

    private GradientDrawable getShape(int color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{corner, corner, corner, corner, corner, corner, corner, corner});
        shape.setColor(color);
        return shape;
    }
}