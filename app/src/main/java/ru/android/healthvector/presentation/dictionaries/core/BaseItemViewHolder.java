package ru.android.healthvector.presentation.dictionaries.core;

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

public abstract class BaseItemViewHolder<T,
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

    @BindView(R.id.textView)
    TextView textView;

    @BindDimen(R.dimen.event_row_corner_radius)
    float corner;

    @Nullable
    private Sex sex;

    public BaseItemViewHolder(View itemView,
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
        textView.setText(getTextForValue(context, item));

        if (sex != this.sex) {
            this.sex = sex;
            setupBackgrounds(context);
        }

        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, bottomView);
    }

    @Nullable
    protected abstract String getTextForValue(Context context, T item);

    @OnClick(R.id.contentView)
    void onContentViewClick() {
        itemActionListener.edit(item);
    }
}
