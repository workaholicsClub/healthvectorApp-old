package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import butterknife.BindView;
import ru.android.childdiary.R;

public class FieldNotificationSoundView extends FieldDialogView<Uri> {
    @BindView(R.id.soundIcon)
    ImageView soundIcon;

    public FieldNotificationSoundView(Context context) {
        super(context);
    }

    public FieldNotificationSoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldNotificationSoundView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_notification_sound;
    }

    @Nullable
    @Override
    protected String getTextForValue(@Nullable Uri value) {
        return value == null ? null : value.toString();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        soundIcon.setImageResource(enabled ? R.drawable.ic_sound : R.drawable.ic_sound_disabled);
    }
}
