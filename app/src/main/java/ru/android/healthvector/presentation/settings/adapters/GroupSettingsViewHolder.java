package ru.android.healthvector.presentation.settings.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.presentation.settings.adapters.items.GroupSettingsItem;

public class GroupSettingsViewHolder extends BaseSettingsViewHolder<GroupSettingsItem> {
    @BindView(R.id.textView)
    TextView textView;

    public GroupSettingsViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void bindT(Context context, Sex sex, GroupSettingsItem item) {
        textView.setText(item.getTitle());
    }
}
