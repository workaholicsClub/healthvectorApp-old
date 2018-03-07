package ru.android.healthvector.presentation.settings.notifications.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.domain.calendar.data.core.EventNotification;
import ru.android.healthvector.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.healthvector.utils.strings.StringUtils;
import ru.android.healthvector.utils.strings.TimeUtils;

public class EventNotificationViewHolder extends BaseRecyclerViewHolder<EventNotification> {
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.textViewSubtitle)
    TextView textViewSubtitle;

    @NonNull
    private ItemClickedListener listener;

    public EventNotificationViewHolder(View itemView, @NonNull ItemClickedListener listener) {
        super(itemView);
        this.listener = listener;
    }

    @Override
    protected void bind(Context context, @Nullable Sex sex) {
        textViewTitle.setText(StringUtils.eventType(context, item.getEventType()));
        textViewSubtitle.setText(TimeUtils.notifyTime(context, item.getNotifyTime()));
    }

    @OnClick(R.id.contentView)
    public void onClicked() {
        listener.onItemClicked(item);
    }
}
