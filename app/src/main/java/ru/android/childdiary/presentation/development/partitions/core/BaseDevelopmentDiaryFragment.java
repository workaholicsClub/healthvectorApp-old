package ru.android.childdiary.presentation.development.partitions.core;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;

public abstract class BaseDevelopmentDiaryFragment<V extends BaseDevelopmentDiaryView> extends AppPartitionFragment
        implements BaseDevelopmentDiaryView {
    @BindView(R.id.imageView)
    protected ImageView imageView;

    @BindView(R.id.textViewIntention)
    protected TextView textViewIntention;

    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    protected ProgressBar progressBar;

    @Nullable
    protected FabController fabController;

    public abstract SwipeViewAdapter getAdapter();

    protected abstract BaseDevelopmentDiaryPresenter<V> getPresenter();

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_app_partition_with_list;
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
}
