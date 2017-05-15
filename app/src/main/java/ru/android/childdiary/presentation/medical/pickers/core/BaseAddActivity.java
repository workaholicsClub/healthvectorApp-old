package ru.android.childdiary.presentation.medical.pickers.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseMvpActivity;

public abstract class BaseAddActivity<T, V> extends BaseMvpActivity implements BaseAddView<T> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
    }
}
