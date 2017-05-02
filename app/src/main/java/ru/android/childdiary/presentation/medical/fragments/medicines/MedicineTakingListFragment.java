package ru.android.childdiary.presentation.medical.fragments.medicines;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.core.swipe.FabController;
import ru.android.childdiary.presentation.core.swipe.ItemActionListener;
import ru.android.childdiary.presentation.medical.adapters.medicines.MedicineTakingAdapter;

public class MedicineTakingListFragment extends AppPartitionFragment
        implements MedicineTakingListView, ItemActionListener<MedicineTaking> {
    @InjectPresenter
    MedicineTakingListPresenter presenter;

    @BindView(R.id.recyclerViewMedicineTakingList)
    RecyclerView recyclerView;

    private MedicineTakingAdapter adapter;
    private FabController fabController;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_medicine_taking_list;
    }

    @Override
    protected void setupUi() {
        adapter = new MedicineTakingAdapter(getContext(), this, fabController);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showMedicineTakingList(@NonNull MedicineTakingListFilter filter, @NonNull List<MedicineTaking> medicineTakingList) {
        adapter.setItems(medicineTakingList);
    }

    @Override
    public void navigateToMedicineTaking() {
    }

    @Override
    public void delete(MedicineTaking item) {

    }

    @Override
    public void edit(MedicineTaking item) {

    }
}
