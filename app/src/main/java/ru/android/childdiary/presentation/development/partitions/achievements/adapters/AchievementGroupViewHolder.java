package ru.android.childdiary.presentation.development.partitions.achievements.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.development.partitions.achievements.expandablerecyclerview.viewholders.GroupViewHolder;

public class AchievementGroupViewHolder extends GroupViewHolder {
    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.imageView)
    ImageView imageView;

    public AchievementGroupViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Context context, @Nullable String title) {
        textView.setText(title);
    }

    @Override
    public void expand() {
        imageView.setImageResource(R.drawable.arrow_up_black);
    }

    @Override
    public void collapse() {
        imageView.setImageResource(R.drawable.arrow_down_black);
    }
}
