package ru.android.childdiary.presentation.medical.fragments.medicines;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.medical.adapters.MedicineTakingListAdapter;

public class MedicineTakingListFragment extends AppPartitionFragment implements MedicineTakingListView {
    @InjectPresenter
    MedicineTakingListPresenter presenter;

    @BindView(R.id.recyclerViewMedicineTakingList)
    RecyclerView recyclerViewMedicineTakingList;

    private MedicineTakingListAdapter medicineTakingListAdapter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_medicine_taking_list;
    }

    @Override
    protected void setupUi() {
    }

    @Override
    public void showMedicineTakingList(@NonNull MedicineTakingListFilter filter, @NonNull List medicineTakingList) {
    }

    @Override
    public void navigateToMedicineTaking() {
    }
}
