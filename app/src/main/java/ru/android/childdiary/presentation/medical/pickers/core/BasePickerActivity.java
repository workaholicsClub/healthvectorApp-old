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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.core.adapters.swipe.ItemActionListener;
import ru.android.childdiary.presentation.core.bindings.RxSearchView;
import ru.android.childdiary.presentation.core.bindings.SearchViewQueryTextEvent;
import ru.android.childdiary.utils.ui.ThemeUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public abstract class BasePickerActivity<T extends Serializable, V extends BasePickerView<T>> extends BaseMvpActivity
        implements BasePickerView<T>, ItemActionListener<T>, FabController {
    @BindView(R.id.rootView)
    View rootView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.textViewIntention)
    TextView textViewIntention;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private BaseRecyclerViewAdapter<T, ? extends BaseRecyclerViewHolder<T>> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = createAdapter();
        adapter.setSex(getSex());
        recyclerView.setAdapter(adapter);

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        progressBar.setVisibility(View.VISIBLE);
        textViewIntention.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        if (adapter != null) {
            adapter.setSex(getSex());
        }
        fab.setBackgroundTintList(ColorStateList.valueOf(ThemeUtils.getColorAccent(this, getSex())));
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideKeyboardAndClearFocus(rootView.findFocus());
    }

    @OnClick(R.id.fab)
    void onFabClick() {
        Intent intent = new Intent(this, getAddActivityClass());
        intent.putExtra(ExtraConstants.EXTRA_SEX, getSex());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        removeToolbarMargin();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        WidgetsUtils.setupSearchView(searchView);
        unsubscribeOnDestroy(getPresenter().listenForFieldsUpdate(RxSearchView.queryTextChangeEvents(searchView)));
        return true;
    }

    @Override
    public void processSearchEvent(@NonNull SearchViewQueryTextEvent event) {
        if (event.isSubmitted()) {
            hideKeyboardAndClearFocus(event.getView());
        }
    }

    @Override
    public void showList(@NonNull List<T> list, boolean isFiltering) {
        adapter.setItems(list);

        progressBar.setVisibility(View.GONE);
        textViewIntention.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        textViewIntention.setText(getIntentionText(isFiltering));
        recyclerView.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void itemDeleted(@NonNull T item) {
    }

    @Override
    public void edit(T item) {
        Intent data = new Intent().putExtra(ExtraConstants.EXTRA_ITEM, item);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void showFab() {
        fab.show();
    }

    @Override
    public boolean hideBar() {
        return false;
    }

    @Override
    public void hideBarWithoutAnimation() {
    }

    @Override
    public void hideFabBar() {
        fab.hide();
    }

    protected abstract BasePickerPresenter<T, V> getPresenter();

    protected abstract Class<? extends BaseAddActivity<T, ? extends BaseAddView<T>>> getAddActivityClass();

    protected abstract BaseRecyclerViewAdapter<T, ? extends BaseRecyclerViewHolder<T>> createAdapter();

    protected abstract String getIntentionText(boolean isFiltering);
}
