package ru.android.childdiary.presentation.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;

import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseActivity;

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
    }
}
