package ru.android.healthvector.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import butterknife.BindView;
import ru.android.healthvector.R;
import ru.android.healthvector.domain.calendar.data.core.SoundInfo;
import ru.android.healthvector.utils.strings.StringUtils;

public class FieldSoundInfoView extends FieldDialogView<SoundInfo> {
    @BindView(R.id.soundIcon)
    ImageView soundIcon;

    public FieldSoundInfoView(Context context) {
        super(context);
    }

    public FieldSoundInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldSoundInfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_sound_info;
    }

    @Nullable
    @Override
    protected String getTextForValue(@Nullable SoundInfo value) {
        return StringUtils.soundInfoName(getContext(), value);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        soundIcon.setImageResource(enabled ? R.drawable.ic_sound : R.drawable.ic_sound_disabled);
    }
}
