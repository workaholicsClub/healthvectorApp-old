package ru.android.childdiary.presentation.chart.antropometry.core;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import ru.android.childdiary.R;

class AntropometryLineEntryUtils {
    public static Drawable getIcon(Context context, boolean selected) {
        @DrawableRes int res = selected ? R.drawable.dot_selected : R.drawable.dot_normal;
        return ContextCompat.getDrawable(context, res);
    }
}
