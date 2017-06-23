package ru.android.childdiary.presentation.settings.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.settings.adapters.items.IntentSettingsItem;

public class IntentSettingsViewHolder extends BaseSettingsViewHolder<IntentSettingsItem> {
    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.textView)
    TextView textView;

    public IntentSettingsViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void bindT(Context context, Sex sex, IntentSettingsItem item) {
        imageView.setImageResource(item.getIconRes());
        textView.setText(item.getTitle());
    }

    @OnClick(R.id.contentView)
    void onContentViewClick() {
        getItemT().getListener().onClick(getItemT());
    }
}
