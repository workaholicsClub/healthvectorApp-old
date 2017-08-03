package ru.android.childdiary.presentation.settings.adapters;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
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

    @BindView(R.id.imageViewPlaceholder)
    ImageView imageViewPlaceholder;

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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            boolean hasPhoto = !TextUtils.isEmpty(item.getChild().getImageFileName());
            imageViewPhoto.setVisibility(hasPhoto ? View.VISIBLE : View.GONE);
            imageViewPlaceholder.setVisibility(hasPhoto ? View.GONE : View.VISIBLE);
        }
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
