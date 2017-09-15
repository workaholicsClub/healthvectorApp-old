package ru.android.childdiary.presentation.development.partitions.achievements.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
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
        super.expand();
        imageView.clearAnimation();
        ViewCompat.animate(imageView).rotation(180).start();
    }

    @Override
    public void collapse() {
        super.collapse();
        imageView.clearAnimation();
        ViewCompat.animate(imageView).rotation(0).start();
    }
}
