package ru.android.childdiary.presentation.medical.adapters.visits;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.presentation.core.swipe.SwipeViewHolder;
import ru.android.childdiary.utils.ui.ThemeUtils;

class DoctorVisitViewHolder extends SwipeViewHolder<DoctorVisit, DoctorVisitSwipeActionListener, DoctorVisitActionListener> {
    @BindView(R.id.swipeLayout)
    SwipeLayout swipeLayout;

    @BindView(R.id.bottomView)
    View bottomView;

    @BindView(R.id.actionsView)
    View actionsView;

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

    public DoctorVisitViewHolder(View itemView,
                                 @NonNull DoctorVisitActionListener itemActionListener,
                                 @NonNull DoctorVisitSwipeActionListener swipeActionListener) {
        super(itemView, itemActionListener, swipeActionListener);
    }

    @Override
    public void bind(Context context, Sex sex, DoctorVisit item) {
        super.bind(context, sex, item);

        textViewDate.setText("date");
        textViewTime.setText("time");
        textViewTitle.setText(item.toString());
        textViewDescription.setText("description");
        imageViewDelete.setBackgroundColor(ThemeUtils.getColorAccent(context, sex));

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
