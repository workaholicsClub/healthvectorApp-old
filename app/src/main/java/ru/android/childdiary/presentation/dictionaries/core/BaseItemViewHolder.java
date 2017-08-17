package ru.android.childdiary.presentation.dictionaries.core;

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

public abstract class BaseItemViewHolder<T,
        SL extends SwipeActionListener<? extends SwipeViewHolder<T, SL, IL>>,
        IL extends ItemActionListener<T>>
        extends SwipeViewHolder<T, SL, IL> {
    @BindView(R.id.swipeLayout)
    SwipeLayout swipeLayout;

    @BindView(R.id.bottomView)
    View bottomView;

    @BindView(R.id.actionDelete)
    ImageView imageViewDelete;

    @BindView(R.id.textView)
    TextView textView;

    @BindDimen(R.dimen.event_row_corner_radius)
    float corner;

    public BaseItemViewHolder(View itemView,
                              @NonNull IL itemActionListener,
                              @NonNull SL swipeActionListener) {
        super(itemView, itemActionListener, swipeActionListener);
    }

    @Override
    protected void bind(Context context, @Nullable Sex sex) {
        textView.setText(getTextForValue(context, item));
        //noinspection deprecation
        imageViewDelete.setBackgroundDrawable(ResourcesUtils.getShape(ThemeUtils.getColorAccent(context, sex), corner));

        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, bottomView);
    }

    @Nullable
    protected abstract String getTextForValue(Context context, T item);

    @Override
    public SwipeLayout getSwipeLayout() {
        return swipeLayout;
    }

    @OnClick(R.id.contentView)
    void onContentViewClick() {
        itemActionListener.edit(item);
    }
}
