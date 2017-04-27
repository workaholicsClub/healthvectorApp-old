package ru.android.childdiary.presentation.medical;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BaseMvpActivity;

public class AddMedicineTakingActivity extends BaseMvpActivity {
    public static Intent getIntent(Context context) {
        return new Intent(context, AddMedicineTakingActivity.class);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine_taking);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        // TODO checkbox color
    }
}
