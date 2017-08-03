package ru.android.childdiary.presentation.settings.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.settings.adapters.items.ProfileSettingsItem;

public class ProfileSettingsViewHolder extends BaseSettingsViewHolder<ProfileSettingsItem> {
    @BindView(R.id.imageViewPhoto)
    ImageView imageViewPhoto;

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.contentView)
    View contentView;

    public ProfileSettingsViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void bindT(Context context, Sex sex, ProfileSettingsItem item) {
        imageViewPhoto.setImageDrawable(item.getIcon());
        textViewTitle.setText(item.getTitle());
    }

    @OnClick(R.id.contentView)
    void onContentViewClick() {
        getItemT().getListener().reviewProfile(getItemT());
    }

    @OnClick(R.id.imageViewWrapper)
    void onDeleteProfileClick() {
        getItemT().getListener().deleteProfile(getItemT());
    }
}
