package ru.android.childdiary.presentation.testing.chart;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
}
