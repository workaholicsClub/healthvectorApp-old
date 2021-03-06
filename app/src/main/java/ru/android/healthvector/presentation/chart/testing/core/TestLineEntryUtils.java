package ru.android.healthvector.presentation.chart.testing.core;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import ru.android.healthvector.R;
import ru.android.healthvector.data.types.DomanTestParameter;

class TestLineEntryUtils {
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
