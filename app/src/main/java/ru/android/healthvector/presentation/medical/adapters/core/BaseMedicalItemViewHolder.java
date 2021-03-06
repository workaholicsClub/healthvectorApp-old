package ru.android.healthvector.presentation.medical.adapters.core;

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
import lombok.Getter;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.presentation.core.adapters.swipe.ItemActionListener;
import ru.android.healthvector.presentation.core.adapters.swipe.SwipeActionListener;
import ru.android.healthvector.presentation.core.adapters.swipe.SwipeViewHolder;
import ru.android.healthvector.utils.ui.ResourcesUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;
import ru.android.healthvector.utils.ui.WidgetsUtils;

public abstract class BaseMedicalItemViewHolder<T,
        SL extends SwipeActionListener<? extends SwipeViewHolder<T, SL, IL>>,
        IL extends ItemActionListener<T>>
        extends SwipeViewHolder<T, SL, IL> {
    @Getter
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

    @Nullable
    private Sex sex;

    public BaseMedicalItemViewHolder(View itemView,
                                     @Nullable Sex sex,
                                     @NonNull IL itemActionListener,
                                     @NonNull SL swipeActionListener) {
        super(itemView, itemActionListener, swipeActionListener);
        this.sex = sex;
        setupBackgrounds(itemView.getContext());
    }

    private void setupBackgrounds(Context context) {
        //noinspection deprecation
        imageViewDelete.setBackgroundDrawable(ResourcesUtils.getShape(ThemeUtils.getColorAccent(context, sex), corner));
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

        if (sex != this.sex) {
            this.sex = sex;
            setupBackgrounds(context);
        }

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

    @OnClick(R.id.contentView)
    void onContentViewClick() {
        itemActionListener.edit(item);
    }

    protected abstract boolean isDone(T item);
}
