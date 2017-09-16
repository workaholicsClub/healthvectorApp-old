package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.AchievementType;
import ru.android.childdiary.utils.strings.StringUtils;

public class FieldAchievementTypeView extends FieldDialogView<AchievementType> {
    public FieldAchievementTypeView(Context context) {
        super(context);
    }

    public FieldAchievementTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldAchievementTypeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_achievement_type;
    }

    @Nullable
    @Override
    protected String getTextForValue(@Nullable AchievementType value) {
        return StringUtils.achievementType(getContext(), value);
    }
}
