package ru.android.childdiary.presentation.exercises.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.exercises.Exercise;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;

public class ExerciseViewHolder extends BaseRecyclerViewHolder<Exercise> {
    @BindView(R.id.textViewDate)
    TextView textViewDate;

    @BindView(R.id.textViewTime)
    TextView textViewTime;

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.textViewDescription)
    TextView textViewDescription;

    public ExerciseViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Context context, Sex sex, Exercise item) {
        super.bind(context, sex, item);
        textViewTitle.setText(item.getName());
        textViewDescription.setText(item.getCode());
    }
}
