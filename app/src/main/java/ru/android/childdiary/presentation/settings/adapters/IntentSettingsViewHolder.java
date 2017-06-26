package ru.android.childdiary.presentation.settings.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.settings.adapters.items.IntentSettingsItem;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public class IntentSettingsViewHolder extends BaseSettingsViewHolder<IntentSettingsItem> {
    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.textViewSubtitle)
    TextView textViewSubtitle;

    @BindView(R.id.contentView)
    View contentView;

    public IntentSettingsViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void bindT(Context context, Sex sex, IntentSettingsItem item) {
        imageView.setImageResource(item.getIconRes());
        textViewTitle.setText(item.getTitle());
        WidgetsUtils.setupTextView(textViewTitle, item.isEnabled());
        textViewSubtitle.setText(item.getSubtitle());
        textViewSubtitle.setVisibility(item.isEnabled() && !TextUtils.isEmpty(item.getSubtitle())
                ? View.VISIBLE : View.GONE);
        contentView.setBackgroundResource(item.isEnabled() ? R.drawable.background_clickable : 0);
    }

    @OnClick(R.id.contentView)
    void onContentViewClick() {
        if (getItemT().isEnabled()) {
            getItemT().getListener().onClick(getItemT());
        }
    }
}
