package ru.android.childdiary.presentation.exercises.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.exercises.Exercise;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.childdiary.utils.ui.ResourcesUtils;

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
    public void bind(Context context, Sex sex, Exercise item) {
        super.bind(context, sex, item);
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
