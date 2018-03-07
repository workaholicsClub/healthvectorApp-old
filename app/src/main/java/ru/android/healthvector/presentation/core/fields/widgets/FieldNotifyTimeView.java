package ru.android.healthvector.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import butterknife.BindView;
import ru.android.healthvector.R;
import ru.android.healthvector.utils.strings.TimeUtils;

public class FieldNotifyTimeView extends FieldDialogView<Integer> {
    @BindView(R.id.notificationIcon)
    ImageView notificationIcon;

    public FieldNotifyTimeView(Context context) {
        super(context);
    }

    public FieldNotifyTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldNotifyTimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public int getValueInt() {
        return getValue() == null ? 0 : getValue();
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_notify_time;
    }

    @Nullable
    @Override
    protected String getTextForValue(@Nullable Integer value) {
        return TimeUtils.notifyTime(getContext(), value);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        notificationIcon.setImageResource(enabled ? R.drawable.ic_notification : R.drawable.ic_notification_disabled);
    }
}
