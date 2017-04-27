package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.DiaperState;
import ru.android.childdiary.utils.StringUtils;

public class FieldDiaperStateView extends FieldRadioView<DiaperState> {
    public FieldDiaperStateView(Context context) {
        super(context);
    }

    public FieldDiaperStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldDiaperStateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected Class<DiaperState> getEnumType() {
        return DiaperState.class;
    }

    @Override
    @LayoutRes
    protected int getTitleLayoutResourceId() {
        return R.layout.field_diaper_state;
    }

    @Override
    protected String getTextForValue(@Nullable DiaperState value) {
        return StringUtils.diaperState(getContext(), value);
    }
}
