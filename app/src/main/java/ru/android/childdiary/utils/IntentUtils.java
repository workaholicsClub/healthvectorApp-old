package ru.android.childdiary.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class IntentUtils {
    public static void startIntent(Context context, Intent intent, String message) {
        if (intent.resolveActivity(context.getPackageManager()) == null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else {
            context.startActivity(intent);
        }
    }
}
