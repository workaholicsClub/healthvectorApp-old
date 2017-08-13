package ru.android.childdiary.presentation.exercises.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.exercises.data.Exercise;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.childdiary.utils.ObjectUtils;

public class ExerciseAdapter extends BaseRecyclerViewAdapter<Exercise, ExerciseViewHolder> {
    private final ExerciseClickListener listener;

    public ExerciseAdapter(Context context, @NonNull ExerciseClickListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    public boolean areItemsTheSame(Exercise oldItem, Exercise newItem) {
        return ObjectUtils.equals(oldItem.getId(), newItem.getId());
    }

    @Override
    protected ExerciseViewHolder createUserViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.exercise_item, parent, false);
        return new ExerciseViewHolder(v, listener);
    }

    @Override
    public boolean paintDividers() {
        return true;
    }

    @Override
    public boolean useFooter() {
        return false;
    }
}
