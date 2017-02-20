package ru.android.childdiary.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.child.Child;

public class UiUtils {
    public static int getPreferredTheme(@Nullable Child child) {
        if (child == null) {
            return R.style.AppTheme;
        }

        return child.getSex() == Sex.MALE ? R.style.AppTheme_Boy : R.style.AppTheme_Girl;
    }

    public static Drawable getPreferredAccountHeaderColor(Context context, @Nullable Child child) {
        if (child == null) {
            return context.getResources().getDrawable(R.color.colorPrimary);
        }

        return child.getSex() == Sex.MALE
                ? context.getResources().getDrawable(R.color.colorPrimaryBoy)
                : context.getResources().getDrawable(R.color.colorPrimaryGirl);
    }
}
