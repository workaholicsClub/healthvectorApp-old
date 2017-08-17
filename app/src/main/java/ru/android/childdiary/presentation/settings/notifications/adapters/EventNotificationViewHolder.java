package ru.android.childdiary.presentation.settings.notifications.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.calendar.data.core.EventNotification;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.childdiary.utils.strings.StringUtils;
import ru.android.childdiary.utils.strings.TimeUtils;

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
