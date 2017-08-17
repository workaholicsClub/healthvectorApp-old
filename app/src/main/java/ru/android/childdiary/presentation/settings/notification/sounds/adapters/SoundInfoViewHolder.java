package ru.android.childdiary.presentation.settings.notification.sounds.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.calendar.data.core.SoundInfo;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.childdiary.utils.strings.StringUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;

class SoundInfoViewHolder extends BaseRecyclerViewHolder<SoundInfo> {
    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.imageView)
    ImageView imageView;

    @NonNull
    private SoundSelectedListener listener;

    public SoundInfoViewHolder(View itemView, @NonNull SoundSelectedListener listener) {
        super(itemView);
        this.listener = listener;
    }

    @Override
    protected void bind(Context context, @Nullable Sex sex) {
        String name = StringUtils.soundInfoName(context, item);
        textView.setText(name);
    }

    public void setSelected(boolean selected, @Nullable Sex sex) {
        imageView.setImageResource(ResourcesUtils.getRadioRes(sex, selected));
    }

    @OnClick(R.id.contentView)
    public void onClicked() {
        listener.onSoundSelected(position, item);
    }
}
