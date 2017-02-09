package ru.android.childdiary.presentation.core.navigation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import ru.android.childdiary.presentation.main.MainActivity;

public class NavigationController {
    private final Context mContext;

    public NavigationController(Context context) {
        mContext = context;
    }

    public void navigateToMain(AppCompatActivity activityContext) {
        Intent intent = new Intent(activityContext, MainActivity.class);
        activityContext.startActivity(intent);
    }
}
