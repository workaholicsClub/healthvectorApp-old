package ru.android.childdiary.presentation.testing.chart;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.DomanTestParameter;

class LineEntryUtils {
    public static Drawable getIcon(Context context, @NonNull DomanTestParameter testParameter, boolean selected) {
        @DrawableRes int res;
        switch (testParameter) {
            case MENTAL_VISION:
                res = selected ? R.drawable.dot_selected : R.drawable.dot_normal;
                break;
            case MENTAL_AUDITION:
                res = selected ? R.drawable.dot_selected : R.drawable.dot_normal;
                break;
            case MENTAL_SENSITIVITY:
                res = selected ? R.drawable.dot_selected : R.drawable.dot_normal;
                break;
            case PHYSICAL_MOBILITY:
                res = selected ? R.drawable.dot_selected : R.drawable.dot_normal;
                break;
            case PHYSICAL_SPEECH:
                res = selected ? R.drawable.dot_selected : R.drawable.dot_normal;
                break;
            case PHYSICAL_MANUAL:
                res = selected ? R.drawable.dot_selected : R.drawable.dot_normal;
                break;
            default:
                throw new IllegalStateException("Unsupported test parameter");
        }
        return ContextCompat.getDrawable(context, res);
    }

    @ColorInt
    public static int getLineColor(Context context, @NonNull DomanTestParameter testParameter) {
        @ColorRes int res;
        switch (testParameter) {
            case MENTAL_VISION:
                res = R.color.mental_vision;
                break;
            case MENTAL_AUDITION:
                res = R.color.mental_audition;
                break;
            case MENTAL_SENSITIVITY:
                res = R.color.mental_sensitivity;
                break;
            case PHYSICAL_MOBILITY:
                res = R.color.physical_mobility;
                break;
            case PHYSICAL_SPEECH:
                res = R.color.physical_speech;
                break;
            case PHYSICAL_MANUAL:
                res = R.color.physical_manual;
                break;
            default:
                throw new IllegalStateException("Unsupported test parameter");
        }
        return ContextCompat.getColor(context, res);
    }
}
