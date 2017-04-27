package ru.android.childdiary.presentation.medical;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.adapters.ViewPagerAdapter;
import ru.android.childdiary.presentation.medical.fragments.medicines.MedicineTakingListFragment;
import ru.android.childdiary.presentation.medical.fragments.visits.DoctorVisitsFragment;
import ru.android.childdiary.utils.ui.ThemeUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public class MedicalDataFragment extends AppPartitionFragment implements MedicalDataView {
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

    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void injectFragment(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_medical_data;
    }

    @Override
    protected void setupUi() {
        setupViewPager();
        hideFab();
    }

    private void setupViewPager() {
        Integer selectedPage = preferences.getInteger(KEY_SELECTED_PAGE, 0).get();
        selectedPage = selectedPage == null ? 0 : selectedPage;
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(putArguments(new DoctorVisitsFragment()), getString(R.string.visits));
        viewPagerAdapter.addFragment(putArguments(new MedicineTakingListFragment()), getString(R.string.medicines));
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(selectedPage, false);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
        WidgetsUtils.setupTabLayoutFont(tabLayout);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                preferences.getInteger(KEY_SELECTED_PAGE).set(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private Fragment putArguments(Fragment fragment) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(ExtraConstants.EXTRA_CHILD, getChild());
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        tabLayout.setBackgroundColor(ThemeUtils.getColorPrimary(getContext(), getSex()));
        fab.setBackgroundTintList(ColorStateList.valueOf(ThemeUtils.getColorAccent(getContext(), getSex())));
    }

    @Override
    public void showChild(@NonNull Child child) {
        super.showChild(child);
        if (child.getId() == null) {
            hideFab();
        } else {
            showFab();
        }
    }

    @Override
    public void navigateToMedicineTakingAdd() {
        Intent intent = AddMedicineTakingActivity.getIntent(getContext());
        startActivity(intent);
    }

    @Override
    public void navigateToDoctorVisitAdd() {
        Intent intent = AddDoctorVisitActivity.getIntent(getContext());
        startActivity(intent);
    }

    @OnClick(R.id.fab)
    void onFabClick() {
        int selectedPage = viewPager.getCurrentItem();
        if (selectedPage == 0) {
            presenter.addDoctorVisit();
        } else if (selectedPage == 1) {
            presenter.addMedicineTaking();
        }
    }

    private void showFab() {
        fab.show();
    }

    private void hideFab() {
        fab.hide();
    }
}
