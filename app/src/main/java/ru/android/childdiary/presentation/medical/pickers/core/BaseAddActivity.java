package ru.android.childdiary.presentation.medical.pickers.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseMvpActivity;

public abstract class BaseAddActivity<T, V> extends BaseMvpActivity implements BaseAddView<T> {
    @BindView(R.id.rootView)
    View rootView;

    @BindView(R.id.imageView)
    protected ImageView imageView;

    @BindView(R.id.autoCompleteTextView)
    protected AutoCompleteTextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_add);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
    }

    @OnClick(R.id.buttonAdd)
    void onButtonAddClick() {

    }
}
