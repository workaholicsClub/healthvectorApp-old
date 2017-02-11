package ru.android.childdiary.presentation.core.navigation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.presentation.main.MainActivity;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class NavigationController {
    Context context;

    public void navigateToMain(AppCompatActivity activityContext) {
        Intent intent = MainActivity.getIntent(activityContext);
        activityContext.startActivity(intent);
    }
}
