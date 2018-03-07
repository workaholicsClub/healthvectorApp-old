package ru.android.healthvector.presentation.medical.partitions.core;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import ru.android.healthvector.R;
import ru.android.healthvector.presentation.core.AppPartitionFragment;
import ru.android.healthvector.presentation.core.adapters.swipe.FabController;
import ru.android.healthvector.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.healthvector.presentation.medical.filter.adapters.Chips;
import ru.android.healthvector.presentation.medical.filter.adapters.ChipsAdapter;
import ru.android.healthvector.presentation.profile.ProfileEditActivity;
import ru.android.healthvector.utils.ui.ThemeUtils;

public abstract class BaseMedicalDataFragment<V extends BaseMedicalDataView> extends AppPartitionFragment
        implements BaseMedicalDataView, ChipsAdapter.ChipsDeleteClickListener {
    private static final String TAG_PROGRESS_DIALOG_DELETING_EVENTS = "TAG_PROGRESS_DIALOG_DELETING_EVENTS";

    @BindView(R.id.footer)
    protected View footer;

    @BindView(R.id.textViewIntention)
    protected TextView textViewIntention;

    @BindView(R.id.textViewNothingFound)
    protected TextView textViewNothingFound;

    @BindView(R.id.recyclerViewChips)
    protected RecyclerView recyclerViewChips;

    @BindView(R.id.line)
    protected View line;

    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    protected ProgressBar progressBar;

    protected ChipsAdapter chipsAdapter;

    @Nullable
    protected FabController fabController;

    public abstract SwipeViewAdapter getAdapter();

    protected abstract BaseMedicalDataPresenter<V> getPresenter();

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_app_partition_with_list_and_chips;
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        getAdapter().setSex(getSex());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FabController) {
            fabController = (FabController) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fabController = null;
    }

    @Override
    public void showFilter() {
        getPresenter().requestFilterDialog();
    }

    @Override
    public void showDeletingEvents(boolean loading) {
        if (loading) {
            showProgress(TAG_PROGRESS_DIALOG_DELETING_EVENTS,
                    getString(R.string.please_wait),
                    getString(R.string.events_deleting));
        } else {
            hideProgress(TAG_PROGRESS_DIALOG_DELETING_EVENTS);
        }
    }

    @Override
    public void noChildSpecified() {
        if (getContext() == null) {
            logger.error("context is null");
            return;
        }
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.add_profile_to_continue)
                .setPositiveButton(R.string.add, (dialog, which) -> getPresenter().addProfile())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void showNoDataToFilter() {
        showToast(getString(R.string.no_data_to_filter));
    }

    @Override
    public void onChipsDeleteClick(Chips chips) {
        boolean result = chipsAdapter.deleteItem(chips);
        if (result) {
            getPresenter().setFilter(chipsAdapter.getItems());
        }
    }

    @Override
    public void navigateToProfileAdd() {
        Intent intent = ProfileEditActivity.getIntent(getContext(), null);
        startActivity(intent);
    }
}
