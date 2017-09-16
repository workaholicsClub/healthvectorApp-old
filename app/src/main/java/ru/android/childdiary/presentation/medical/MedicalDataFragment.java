package ru.android.childdiary.presentation.medical;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import org.joda.time.LocalTime;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.medical.data.DoctorVisit;
import ru.android.childdiary.domain.medical.data.MedicineTaking;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.core.adapters.PagesAdapter;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.childdiary.presentation.medical.add.medicines.AddDoctorVisitActivity;
import ru.android.childdiary.presentation.medical.add.visits.AddMedicineTakingActivity;
import ru.android.childdiary.presentation.medical.partitions.core.BaseMedicalDataFragment;
import ru.android.childdiary.presentation.medical.partitions.medicines.MedicineTakingListFragment;
import ru.android.childdiary.presentation.medical.partitions.visits.DoctorVisitsFragment;
import ru.android.childdiary.utils.ui.ThemeUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public class MedicalDataFragment extends AppPartitionFragment implements MedicalDataView,
        FabController {
    private static final String KEY_SELECTED_PAGE = "medical_data.selected_page";

    @Inject
    RxSharedPreferences preferences;

    @InjectPresenter
    MedicalDataPresenter presenter;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private PagesAdapter pagesAdapter;

    @Override
    protected void injectFragment(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_app_partition_with_tabs;
    }

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
        setupViewPager();
        hideFabBar();
    }

    private void setupViewPager() {
        Integer selectedPage = preferences.getInteger(KEY_SELECTED_PAGE, 0).get();
        selectedPage = selectedPage == null ? 0 : selectedPage;
        pagesAdapter = new PagesAdapter(getChildFragmentManager());
        pagesAdapter.addFragment(putArguments(new DoctorVisitsFragment()), getString(R.string.doctor_visits));
        pagesAdapter.addFragment(putArguments(new MedicineTakingListFragment()), getString(R.string.medicines));
        viewPager.setAdapter(pagesAdapter);
        viewPager.setCurrentItem(selectedPage, false);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        WidgetsUtils.setupTabLayoutFont(tabLayout);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                preferences.getInteger(KEY_SELECTED_PAGE).set(position);
                updateSwipeLayouts(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void updateSwipeLayouts(int position) {
        SwipeViewAdapter adapter = getSwipeViewAdapter(position);
        if (adapter != null) {
            adapter.updateFabState();
        } else {
            logger.error("selected page: " + position + "; event adapter is null");
        }
    }

    private void closeAllItems(int position) {
        SwipeViewAdapter adapter = getSwipeViewAdapter(position);
        if (adapter != null) {
            adapter.closeAllItems();
        } else {
            logger.error("selected page: " + position + "; adapter is null");
        }
    }

    @Nullable
    private SwipeViewAdapter getSwipeViewAdapter(int position) {
        BaseMedicalDataFragment fragment = getSelectedPage(position);
        return fragment == null ? null : fragment.getAdapter();
    }

    @Nullable
    private BaseMedicalDataFragment getSelectedPage(int position) {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments == null) {
            return null;
        }
        for (Fragment fragment : fragments) {
            if (position == 0 && fragment instanceof DoctorVisitsFragment) {
                return (DoctorVisitsFragment) fragment;
            } else if (position == 1 && fragment instanceof MedicineTakingListFragment) {
                return (MedicineTakingListFragment) fragment;
            }
        }
        return null;
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        tabLayout.setBackgroundColor(ThemeUtils.getColorPrimary(getContext(), getSex()));
        fab.setBackgroundTintList(ColorStateList.valueOf(ThemeUtils.getColorAccent(getContext(), getSex())));
    }

    @Override
    public void onResume() {
        super.onResume();
        int position = viewPager.getCurrentItem();
        closeAllItems(position);
    }

    @Override
    public void showChild(@NonNull Child child) {
        super.showChild(child);
        if (child.getId() == null) {
            hideFabBar();
        }
    }

    @Override
    public void showFilter() {
        int position = viewPager.getCurrentItem();
        BaseMedicalDataFragment fragment = getSelectedPage(position);
        if (fragment != null) {
            fragment.showFilter();
        } else {
            logger.error("selected page: " + position + "; partition is null");
        }
    }

    @Override
    public void navigateToMedicineTakingAdd(@NonNull MedicineTaking defaultMedicineTaking,
                                            @Nullable LocalTime startTime,
                                            @Nullable LocalTime finishTime) {
        Intent intent = AddMedicineTakingActivity.getIntent(getContext(), defaultMedicineTaking,
                startTime, finishTime);
        startActivity(intent);
    }

    @Override
    public void navigateToDoctorVisitAdd(@NonNull DoctorVisit defaultDoctorVisit,
                                         @Nullable LocalTime startTime,
                                         @Nullable LocalTime finishTime) {
        Intent intent = AddDoctorVisitActivity.getIntent(getContext(), defaultDoctorVisit,
                startTime, finishTime);
        startActivity(intent);
    }

    @Override
    public void noChildSpecified() {
        showToast(getString(R.string.intention_add_child_profile));
    }

    @OnClick(R.id.fab)
    void onFabClick() {
        int selectedPage = viewPager.getCurrentItem();
        switch (selectedPage) {
            case 0:
                presenter.addDoctorVisit();
                break;
            case 1:
                presenter.addMedicineTaking();
                break;
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
}
