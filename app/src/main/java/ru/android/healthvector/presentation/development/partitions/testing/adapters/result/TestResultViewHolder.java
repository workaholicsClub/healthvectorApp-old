package ru.android.healthvector.presentation.development.partitions.testing.adapters.result;

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
import ru.android.healthvector.domain.development.testing.data.TestResult;
import ru.android.healthvector.presentation.core.adapters.swipe.SwipeViewHolder;
import ru.android.healthvector.utils.strings.DateUtils;
import ru.android.healthvector.utils.strings.TestUtils;
import ru.android.healthvector.utils.strings.TimeUtils;
import ru.android.healthvector.utils.ui.ResourcesUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;
import ru.android.healthvector.utils.ui.WidgetsUtils;

public class TestResultViewHolder extends SwipeViewHolder<TestResult, TestResultSwipeActionListener, TestResultActionListener> {
    @Getter
    @BindView(R.id.swipeLayout)
    SwipeLayout swipeLayout;

    @BindView(R.id.bottomView)
    View bottomView;

    @BindView(R.id.actionDelete)
    ImageView imageViewDelete;

    @BindView(R.id.textViewDate)
    TextView textViewDate;

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.textViewDescription)
    TextView textViewDescription;

    @BindDimen(R.dimen.event_row_corner_radius)
    float corner;

    @Nullable
    private Sex sex;

    public TestResultViewHolder(View itemView,
                                @Nullable Sex sex,
                                @NonNull TestResultActionListener itemActionListener,
                                @NonNull TestResultSwipeActionListener swipeActionListener) {
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
        String dateStr = DateUtils.date(context, item.getDate());
        TimeUtils.Age age = TimeUtils.getAge(item.getBirthDate(), item.getDate());
        String ageStr = TimeUtils.age(context, age);
        String valueStr = context.getString(R.string.two_values, dateStr, ageStr);
        textViewDate.setText(valueStr);

        textViewTitle.setText(TestUtils.getTestTitle(context, item));
        textViewDescription.setText(TestUtils.getTestResultShort(context, item));
        WidgetsUtils.hideIfEmpty(textViewDescription);

        if (sex != this.sex) {
            this.sex = sex;
            setupBackgrounds(context);
        }

        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, bottomView);
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
