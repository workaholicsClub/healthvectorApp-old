package ru.android.childdiary.presentation.medical.adapters.medicines;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.presentation.core.swipe.SwipeViewHolder;
import ru.android.childdiary.utils.DateUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

class MedicineTakingViewHolder extends SwipeViewHolder<MedicineTaking, MedicineTakingSwipeActionListener, MedicineTakingActionListener> {
    @BindView(R.id.swipeLayout)
    SwipeLayout swipeLayout;

    @BindView(R.id.bottomView)
    View bottomView;

    @BindView(R.id.eventRowActionDelete)
    ImageView imageViewDelete;

    @BindView(R.id.eventView)
    View eventView;

    @BindView(R.id.textViewDate)
    TextView textViewDate;

    @BindView(R.id.textViewTime)
    TextView textViewTime;

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.textViewDescription)
    TextView textViewDescription;

    @BindDimen(R.dimen.event_row_corner_radius)
    float corner;

    public MedicineTakingViewHolder(View itemView,
                                    @NonNull MedicineTakingActionListener itemActionListener,
                                    @NonNull MedicineTakingSwipeActionListener swipeActionListener) {
        super(itemView, itemActionListener, swipeActionListener);
    }

    @Override
    public void bind(Context context, Sex sex, MedicineTaking item) {
        super.bind(context, sex, item);

        textViewDate.setText(DateUtils.date(context, item.getDateTime()));
        textViewTime.setText(DateUtils.time(context, item.getDateTime()));
        textViewTitle.setText(item.toString());
        textViewDescription.setText("description");
        //noinspection deprecation
        imageViewDelete.setBackgroundDrawable(ResourcesUtils.getShape(ThemeUtils.getColorAccent(context, sex), corner));

        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, bottomView);
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
