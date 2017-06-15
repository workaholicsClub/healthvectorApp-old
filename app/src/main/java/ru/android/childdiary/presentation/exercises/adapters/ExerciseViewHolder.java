package ru.android.childdiary.presentation.exercises.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.exercises.Exercise;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.childdiary.presentation.core.adapters.swipe.ItemActionListener;

public class ExerciseViewHolder extends BaseRecyclerViewHolder<Exercise> {
    @BindView(R.id.textView)
    TextView textView;

    private final ItemActionListener<Exercise> itemActionListener;

    public ExerciseViewHolder(View itemView, @NonNull ItemActionListener<Exercise> itemActionListener) {
        super(itemView);
        this.itemActionListener = itemActionListener;
    }

    @Override
    public void bind(Context context, Sex sex, Exercise item) {
        super.bind(context, sex, item);
        textView.setText(item.getName());
    }

    @OnClick(R.id.contentView)
    void onContentViewClick() {
        itemActionListener.edit(item);
    }
}
