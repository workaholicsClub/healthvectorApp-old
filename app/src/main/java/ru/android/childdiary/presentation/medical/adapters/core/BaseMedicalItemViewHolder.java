package ru.android.childdiary.presentation.medical.adapters.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.core.adapters.swipe.ItemActionListener;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeActionListener;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewHolder;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public abstract class BaseMedicalItemViewHolder<T,
        SL extends SwipeActionListener<? extends SwipeViewHolder<T, SL, IL>>,
        IL extends ItemActionListener<T>>
        extends SwipeViewHolder<T, SL, IL> {
    @BindView(R.id.swipeLayout)
    SwipeLayout swipeLayout;

    @BindView(R.id.bottomView)
    View bottomView;

    @BindView(R.id.actionDelete)
    ImageView imageViewDelete;

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

    public BaseMedicalItemViewHolder(View itemView,
                                     @NonNull IL itemActionListener,
                                     @NonNull SL swipeActionListener) {
        super(itemView, itemActionListener, swipeActionListener);
    }

    @Override
    protected void bind(Context context, @Nullable Sex sex) {
        boolean isDone = isDone(item);
        boolean enabled = !isDone;

        textViewDate.setText(getDateText(context, item));
        textViewTime.setText(getTimeText(context, item));
        WidgetsUtils.hideIfEmpty(textViewTime);

        WidgetsUtils.setupTextView(textViewDate, enabled);
        WidgetsUtils.setupTextView(textViewTime, enabled);

        textViewTitle.setText(getTitleText(context, item));
        textViewDescription.setText(getDescriptionText(context, item));

        WidgetsUtils.setupTextView(textViewTitle, enabled);
        WidgetsUtils.setupTextView(textViewDescription, enabled);

        WidgetsUtils.strikeTextView(textViewTitle, isDone);
        WidgetsUtils.strikeTextView(textViewDescription, isDone);

        //noinspection deprecation
        imageViewDelete.setBackgroundDrawable(ResourcesUtils.getShape(ThemeUtils.getColorAccent(context, sex), corner));

        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, bottomView);
    }

    @Nullable
    protected abstract String getDateText(Context context, T item);

    @Nullable
    protected abstract String getTimeText(Context context, T item);

    @Nullable
    protected abstract String getTitleText(Context context, T item);

    @Nullable
    protected abstract String getDescriptionText(Context context, T item);

    @Override
    public SwipeLayout getSwipeLayout() {
        return swipeLayout;
    }

    @OnClick(R.id.contentView)
    void onContentViewClick() {
        itemActionListener.edit(item);
    }

    protected abstract boolean isDone(T item);
}
