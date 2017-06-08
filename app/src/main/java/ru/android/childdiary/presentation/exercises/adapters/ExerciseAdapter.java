package ru.android.childdiary.presentation.exercises.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.exercises.Exercise;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.childdiary.utils.ObjectUtils;

public class ExerciseAdapter extends BaseRecyclerViewAdapter<Exercise, ExerciseViewHolder> {
    public ExerciseAdapter(Context context) {
        super(context);
    }

    @Override
    public boolean areItemsTheSame(Exercise oldItem, Exercise newItem) {
        return ObjectUtils.equals(oldItem.getId(), newItem.getId());
    }

    @Override
    protected ExerciseViewHolder createViewHolder(ViewGroup parent) {
        View v = inflater.inflate(R.layout.exercise_item, parent, false);
        return new ExerciseViewHolder(v);
    }
}
