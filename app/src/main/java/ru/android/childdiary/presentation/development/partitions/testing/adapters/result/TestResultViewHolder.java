package ru.android.childdiary.presentation.development.partitions.testing.adapters.result;

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
import ru.android.childdiary.domain.interactors.development.testing.TestResult;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewHolder;
import ru.android.childdiary.utils.strings.DateUtils;
import ru.android.childdiary.utils.strings.TestUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public class TestResultViewHolder extends SwipeViewHolder<TestResult, TestResultSwipeActionListener, TestResultActionListener> {
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

    public TestResultViewHolder(View itemView,
                                @NonNull TestResultActionListener itemActionListener,
                                @NonNull TestResultSwipeActionListener swipeActionListener) {
        super(itemView, itemActionListener, swipeActionListener);
    }

    @Override
    public void bind(Context context, Sex sex, TestResult item) {
        super.bind(context, sex, item);

        textViewDate.setText(DateUtils.date(context, item.getDate()));
        // TODO textViewTime.setText(DateUtils.time(context, item.getDateTime()));
        WidgetsUtils.hideIfEmpty(textViewTime);

        textViewTitle.setText(TestUtils.getTestName(context, item.getTestType()));
        textViewDescription.setText(TestUtils.toString(context, item.getDomanTestParameter()));

        //noinspection deprecation
        imageViewDelete.setBackgroundDrawable(ResourcesUtils.getShape(ThemeUtils.getColorAccent(context, sex), corner));

        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, bottomView);
    }

    @Override
    public SwipeLayout getSwipeLayout() {
        return swipeLayout;
    }

    @OnClick(R.id.contentView)
    void onContentViewClick() {
        itemActionListener.edit(item);
    }

    @OnClick(R.id.actionDelete)
    void onDeleteClick() {
        swipeActionListener.delete(this);
    }
}
