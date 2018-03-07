package ru.android.healthvector.presentation.exercises.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.domain.exercises.data.Exercise;
import ru.android.healthvector.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.healthvector.utils.ui.ResourcesUtils;

public class ExerciseViewHolder extends BaseRecyclerViewHolder<Exercise> {
    private final ExerciseClickListener listener;

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.imageViewExported)
    ImageView imageViewExported;

    @BindView(R.id.imageViewExport)
    ImageView imageViewExport;

    public ExerciseViewHolder(View itemView, @NonNull ExerciseClickListener listener) {
        super(itemView);
        this.listener = listener;
    }

    @Override
    protected void bind(Context context, @Nullable Sex sex) {
        textView.setText(item.getName());
        imageViewExported.setVisibility(item.isExported() ? View.VISIBLE : View.GONE);
        imageViewExported.setImageResource(ResourcesUtils.getExerciseExportedIcon(sex));
        imageViewExport.setImageResource(ResourcesUtils.getExerciseIcon(sex));
    }

    @OnClick(R.id.contentView)
    void onContentViewClick() {
        listener.showExerciseDetails(item);
    }

    @OnClick(R.id.imageViewExport)
    void onExportClick() {
        listener.addConcreteExercise(item);
    }
}
