package ru.android.childdiary.presentation.medical.pickers.core;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.childdiary.utils.ui.ThemeUtils;

public abstract class BasePickerActivity<T, V> extends BaseMvpActivity implements BasePickerView<T> {
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private BaseRecyclerViewAdapter<T, ? extends BaseRecyclerViewHolder<T>> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = createAdapter();
        recyclerView.setAdapter(adapter);

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        fab.setBackgroundTintList(ColorStateList.valueOf(ThemeUtils.getColorAccent(this, getSex())));
    }

    @OnClick(R.id.fab)
    void onFabClick() {
        Intent intent = new Intent(this, getAddActivityClass());
        intent.putExtra(ExtraConstants.EXTRA_SEX, getSex());
        startActivity(intent);
    }

    @Override
    public void showList(@NonNull List<T> list) {
        adapter.setItems(list);
    }

    protected abstract Class<? extends BaseAddActivity<T, ? extends BaseAddView<T>>> getAddActivityClass();

    protected abstract BaseRecyclerViewAdapter<T, ? extends BaseRecyclerViewHolder<T>> createAdapter();
}
