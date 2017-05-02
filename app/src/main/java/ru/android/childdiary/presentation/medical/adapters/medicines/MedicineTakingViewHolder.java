package ru.android.childdiary.presentation.medical.adapters.medicines;

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
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.presentation.core.swipe.ItemActionListener;
import ru.android.childdiary.presentation.core.swipe.SwipeActionListener;
import ru.android.childdiary.presentation.core.swipe.SwipeViewHolder;
import ru.android.childdiary.utils.DateUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

class MedicineTakingViewHolder extends SwipeViewHolder<MedicineTaking, SwipeActionListener<MedicineTakingViewHolder>, ItemActionListener<MedicineTaking>> {
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

    @BindView(R.id.textViewEventTitle)
    TextView textViewTitle;

    @BindView(R.id.textViewDescription)
    TextView textViewDescription;

    @BindView(R.id.eventRowActionDone)
    View actionDoneView;

    @BindView(R.id.delimiter1)
    View delimiter1;

    @BindDimen(R.dimen.event_row_corner_radius)
    float corner;

    public MedicineTakingViewHolder(View itemView, @NonNull ItemActionListener<MedicineTaking> itemActionListener, @NonNull SwipeActionListener<MedicineTakingViewHolder> swipeActionListener) {
        super(itemView, itemActionListener, swipeActionListener);
    }

    @Override
    public void bind(Context context, Sex sex, MedicineTaking item) {
        super.bind(context, sex, item);

        //noinspection deprecation
        actionsView.setBackgroundDrawable(
                getActionsViewBackgroundDrawable(ThemeUtils.getColorAccent(context, sex)));

        textViewTime.setText(DateUtils.time(context, item.getDateTime().toLocalTime()));
        textViewTitle.setText(item.toString());
        textViewDescription.setText(null);

        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, bottomView);
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
}
