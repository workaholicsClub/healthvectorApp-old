package ru.android.healthvector.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import ru.android.healthvector.data.types.AchievementType;
import ru.android.healthvector.utils.strings.StringUtils;

public class FieldAchievementTypeRadioView extends FieldRadioView<AchievementType> {
    public FieldAchievementTypeRadioView(Context context) {
        super(context);
    }

    public FieldAchievementTypeRadioView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldAchievementTypeRadioView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected Class<AchievementType> getEnumType() {
        return AchievementType.class;
    }

    @Override
    @LayoutRes
    protected int getTitleLayoutResourceId() {
        return 0;
    }

    @Nullable
    @Override
    protected String getTextForValue(@Nullable AchievementType value) {
        return StringUtils.achievementType(getContext(), value);
    }
}
