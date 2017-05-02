package ru.android.childdiary.presentation.medical.adapters.medicines;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.presentation.core.swipe.ItemActionListener;
import ru.android.childdiary.presentation.core.swipe.SwipeActionListener;
import ru.android.childdiary.presentation.core.swipe.SwipeViewHolder;
import ru.android.childdiary.utils.DateUtils;

class MedicineTakingViewHolder extends SwipeViewHolder<MedicineTaking, SwipeActionListener<MedicineTakingViewHolder>, ItemActionListener<MedicineTaking>> {
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

    public MedicineTakingViewHolder(View itemView,
                                    @NonNull ItemActionListener<MedicineTaking> itemActionListener,
                                    @NonNull SwipeActionListener<MedicineTakingViewHolder> swipeActionListener) {
        super(itemView, itemActionListener, swipeActionListener);
    }

    @Override
    public void bind(Context context, Sex sex, MedicineTaking item) {
        super.bind(context, sex, item);

        textViewTime.setText(DateUtils.time(context, item.getDateTime().toLocalTime()));
        textViewTitle.setText(item.toString());
        textViewDescription.setText(null);

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
