package ru.android.childdiary.presentation.settings.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.settings.adapters.items.ProfileGroupSettingsItem;

public class ProfileGroupSettingsViewHolder extends BaseSettingsViewHolder<ProfileGroupSettingsItem> {
    @BindView(R.id.textView)
    TextView textView;

    public ProfileGroupSettingsViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void bindT(Context context, Sex sex, ProfileGroupSettingsItem item) {
        textView.setText(item.getTitle());
    }

    @OnClick(R.id.imageViewWrapper)
    void onAddProfileClick() {
        getItemT().getListener().addProfile(getItemT());
    }
}
