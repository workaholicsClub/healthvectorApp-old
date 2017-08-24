package ru.android.childdiary.presentation.dictionaries.core;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
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

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.adapters.decorators.DividerItemDecoration;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.core.adapters.swipe.ItemActionListener;
import ru.android.childdiary.presentation.core.bindings.RxSearchView;
import ru.android.childdiary.presentation.core.bindings.SearchViewQueryTextEvent;
import ru.android.childdiary.utils.HtmlUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public abstract class BasePickerActivity<T extends Serializable, V extends BasePickerView<T>> extends BaseMvpActivity
        implements BasePickerView<T>, ItemActionListener<T>, FabController, HtmlUtils.OnLinkClickListener {
    private static final String LINK_ADD = "add";

    @BindInt(R.integer.max_length_name)
    protected int maxLengthName;

    @BindInt(R.integer.max_length_name_medium)
    protected int maxLengthNameMedium;

    @BindInt(R.integer.max_length_name_small)
    protected int maxLengthNameSmall;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.textViewIntention)
    TextView textViewIntention;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private BaseRecyclerViewAdapter<T, ? extends BaseRecyclerViewHolder<T>> adapter;
    private boolean pick;
    private String defaultText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        pick = getIntent().getBooleanExtra(ExtraConstants.EXTRA_PICK, false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = createAdapter();
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(this, adapter);
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter.setSex(getSex());
        recyclerView.setAdapter(adapter);

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
        hideKeyboardAndClearFocus();
    }

    @OnClick(R.id.fab)
    void onFabClick() {
        startAddActivity();
    }

    private void startAddActivity() {
        Intent intent = new Intent(this, getAddActivityClass());
        intent.putExtra(ExtraConstants.EXTRA_SEX, getSex());
        intent.putExtra(ExtraConstants.EXTRA_TEXT, defaultText);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        removeToolbarMargin();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        WidgetsUtils.setupSearchView(searchView, getMaxLength());
        unsubscribeOnDestroy(getPresenter().listenForFieldsUpdate(RxSearchView.queryTextChangeEvents(searchView)));
        return true;
    }

    @Override
    public void processSearchEvent(@NonNull SearchViewQueryTextEvent event) {
        defaultText = event.getQueryText().toString();
        if (event.isSubmitted()) {
            hideKeyboardAndClearFocus(event.getView());
        }
    }

    @Override
    public void showList(@NonNull List<T> list, boolean isFiltering) {
        adapter.setItems(list);

        progressBar.setVisibility(View.GONE);
        textViewIntention.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        setupTextViewIntention(isFiltering);
        recyclerView.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void itemDeleted(@NonNull T item) {
    }

    @Override
    public void edit(T item) {
        if (pick) {
            Intent data = new Intent().putExtra(ExtraConstants.EXTRA_ITEM, item);
            setResult(RESULT_OK, data);
            finish();
        }
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

    private void setupTextViewIntention(boolean isFiltering) {
        String text = isFiltering
                ? getString(R.string.nothing_found_format,
                getString(R.string.nothing_found),
                LINK_ADD,
                getIntentionText())
                : getString(R.string.link_format,
                LINK_ADD,
                getIntentionText());
        HtmlUtils.setupClickableLinks(textViewIntention, text, this, ContextCompat.getColor(this, R.color.intention_text));
    }

    protected abstract int getMaxLength();

    protected abstract String getIntentionText();

    @Override
    public void onLinkClick(String url) {
        if (LINK_ADD.equals(url)) {
            startAddActivity();
        }
    }
}
