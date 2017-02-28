package ru.android.childdiary.presentation.profile.edit.fragments;

import android.os.Bundle;

import com.yalantis.ucrop.UCropActivity;

import ru.android.childdiary.utils.ui.ConfigUtils;

public class CropActivity extends UCropActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        ConfigUtils.setupOrientation(this);
        super.onCreate(savedInstanceState);
        // TODO: try to set toolbar font style
    }
}
