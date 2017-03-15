package ru.android.childdiary.presentation.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.adapters.ViewPagerAdapter;
import ru.android.childdiary.presentation.events.DiaperEventDetailActivity;
import ru.android.childdiary.presentation.events.FeedEventDetailActivity;
import ru.android.childdiary.presentation.events.OtherEventDetailActivity;
import ru.android.childdiary.presentation.events.PumpEventDetailActivity;
import ru.android.childdiary.presentation.events.SleepEventDetailActivity;
import ru.android.childdiary.presentation.main.calendar.adapters.events.FabController;
import ru.android.childdiary.presentation.main.calendar.fragments.CalendarFragment;
import ru.android.childdiary.presentation.main.calendar.fragments.DayFragment;
import ru.android.childdiary.presentation.main.calendar.fragments.MonthFragment;
import ru.android.childdiary.presentation.main.calendar.fragments.WeekFragment;
import ru.android.childdiary.presentation.main.drawer.AccountHeaderActionAdapter;
import ru.android.childdiary.presentation.main.drawer.CustomAccountHeaderBuilder;
import ru.android.childdiary.presentation.main.drawer.CustomDrawerBuilder;
import ru.android.childdiary.presentation.main.drawer.CustomPrimaryDrawerItem;
import ru.android.childdiary.presentation.main.widgets.FabToolbar;
import ru.android.childdiary.presentation.profile.edit.ProfileEditActivity;
import ru.android.childdiary.presentation.profile.review.ProfileReviewActivity;
import ru.android.childdiary.utils.StringUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class MainActivity extends BaseMvpActivity<MainPresenter> implements MainView,
        Drawer.OnDrawerItemClickListener,
        AccountHeader.OnAccountHeaderProfileImageListener,
        AdapterView.OnItemClickListener,
        PopupWindow.OnDismissListener,
        PopupMenu.OnMenuItemClickListener,
        View.OnClickListener,
        FabController {
    private static final int PROFILE_SETTINGS_EDIT = 1;
    private static final int PROFILE_SETTINGS_ADD = 2;
    private static final int PROFILE_SETTINGS_DELETE = 3;
    private static final int PROFILE_SETTINGS_USER = 10;

    private final PrimaryDrawerItem[] drawerItems = new PrimaryDrawerItem[]{
            new CustomPrimaryDrawerItem()
                    .withName(R.string.drawer_item_calendar)
                    .withOnDrawerItemClickListener(this),
            new CustomPrimaryDrawerItem()
                    .withName(R.string.drawer_item_development_diary)
                    .withOnDrawerItemClickListener(this),
            new CustomPrimaryDrawerItem()
                    .withName(R.string.drawer_item_exercises)
                    .withOnDrawerItemClickListener(this),
            new CustomPrimaryDrawerItem()
                    .withName(R.string.drawer_item_medical_data)
                    .withOnDrawerItemClickListener(this),
            new CustomPrimaryDrawerItem()
                    .withName(R.string.drawer_item_settings)
                    .withOnDrawerItemClickListener(this),
            new CustomPrimaryDrawerItem()
                    .withName(R.string.drawer_item_help)
                    .withOnDrawerItemClickListener(this)
    };

    @InjectPresenter
    MainPresenter presenter;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.fabToolbar)
    FabToolbar fabToolbar;

    private ViewPagerAdapter viewPagerAdapter;
    private AccountHeader accountHeader;
    private Drawer drawer;
    private DrawerBuilder drawerBuilder;
    private ImageView switcherImage;
    private ListPopupWindow popupWindow;

    public static Intent getIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    private static IProfile mapToProfile(Context context, @NonNull Child child) {
        return new ProfileDrawerItem()
                .withName(child.getName())
                .withEmail(StringUtils.age(context, child))
                .withNameShown(true)
                .withTag(child)
                .withIdentifier(mapToProfileId(child))
                .withIcon(ResourcesUtils.getChildIcon(context, child));
    }

    private static long mapToProfileId(@NonNull Child child) {
        return child.getId() + PROFILE_SETTINGS_USER;
    }

    private static long mapToChildId(long profileId) {
        return profileId - PROFILE_SETTINGS_USER;
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViewPager();
        buildUi();
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar.setNavigationIcon(R.drawable.toolbar_action_navigation_drawer);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        tabLayout.setBackgroundColor(ThemeUtils.getColorPrimary(this, sex));
        fabToolbar.setColor(ThemeUtils.getColorAccent(this, sex));
        if (accountHeader != null) {
            accountHeader.setBackground(ThemeUtils.getColorPrimaryDrawable(this, sex));
        }
        updateDrawerItems(true);
    }

    private void updateDrawerItems(boolean notify) {
        int[] icons = ResourcesUtils.getNavigationDrawerItemResources(sex);
        for (int i = 0; i < Math.min(icons.length, drawerItems.length); ++i) {
            drawerItems[i].withIcon(icons[i]);
        }
        if (notify && drawer != null) {
            drawer.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void showChildList(@NonNull List<Child> childList) {
        logger.debug("showChildList: " + StringUtils.childList(childList));

        List<IProfile> profiles = new ArrayList<>();
        if (!childList.isEmpty()) {
            profiles.add(new ProfileSettingDrawerItem()
                    .withName(getString(R.string.edit_child_short))
                    .withIdentifier(PROFILE_SETTINGS_EDIT));
        }
        profiles.add(new ProfileSettingDrawerItem()
                .withName(getString(R.string.add_child))
                .withIdentifier(PROFILE_SETTINGS_ADD));
        if (!childList.isEmpty()) {
            profiles.add(new ProfileSettingDrawerItem()
                    .withName(getString(R.string.remove_child))
                    .withIdentifier(PROFILE_SETTINGS_DELETE));
        }

        profiles.addAll(Stream.of(childList)
                .map(child -> mapToProfile(this, child))
                .collect(Collectors.toList()));

        closeDrawerWithoutAnimation();
        if (accountHeader != null) {
            accountHeader.setProfiles(profiles);
        }
    }

    @Override
    public void showChild(@NonNull Child child) {
        logger.debug("showChild: " + child);
        changeThemeIfNeeded(child);
        if (child == Child.NULL) {
            hideFabBar();
            getSupportActionBar().setTitle(R.string.app_name);
        } else {
            updateFab(viewPager.getCurrentItem());
            getSupportActionBar().setTitle(child.getName());
            if (accountHeader != null) {
                accountHeader.setActiveProfile(mapToProfileId(child));
            }
        }
    }

    @Override
    public void navigateToProfileAdd() {
        Intent intent = ProfileEditActivity.getIntent(this, null);
        startActivity(intent);
    }

    @Override
    public void navigateToProfileEdit(@NonNull Child child) {
        Intent intent = ProfileEditActivity.getIntent(this, child);
        startActivity(intent);
    }

    @Override
    public void navigateToProfileReview() {
        Intent intent = ProfileReviewActivity.getIntent(this);
        startActivity(intent);
    }

    @Override
    public void showDeleteChildConfirmation(@NonNull Child child) {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(sex))
                .setTitle(getString(R.string.remove_child_confirmation_title, child.getName()))
                .setMessage(R.string.remove_child_confirmation_text)
                .setPositiveButton(R.string.Yes,
                        (DialogInterface dialog, int which) -> presenter.deleteChild(child))
                .setNegativeButton(R.string.Cancel, null)
                .show();
    }

    @Override
    public void navigateToDiaperEventAdd() {
        Intent intent = DiaperEventDetailActivity.getIntent(this, null);
        startActivity(intent);
    }

    @Override
    public void navigateToFeedEventAdd() {
        Intent intent = FeedEventDetailActivity.getIntent(this, null);
        startActivity(intent);
    }

    @Override
    public void navigateToOtherEventAdd() {
        Intent intent = OtherEventDetailActivity.getIntent(this, null);
        startActivity(intent);
    }

    @Override
    public void navigateToPumpEventAdd() {
        Intent intent = PumpEventDetailActivity.getIntent(this, null);
        startActivity(intent);
    }

    @Override
    public void navigateToSleepEventAdd() {
        Intent intent = SleepEventDetailActivity.getIntent(this, null);
        startActivity(intent);
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        // TODO: navigation
        showToast(((PrimaryDrawerItem) drawerItem).getName().getText());
        return false;
    }

    @Override
    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
        presenter.reviewChild();
        return false;
    }

    @Override
    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.material_drawer_account_header_text_switcher_wrapper && accountHeader != null) {
            dismissPopupWindow();
            animateSwitcherIn();
            List<IProfile> profiles = new ArrayList<>(accountHeader.getProfiles());
            profiles.remove(accountHeader.getActiveProfile());
            ListAdapter adapter = new AccountHeaderActionAdapter(this, profiles);
            View anchor = v;
            int width = profiles.size() == 1
                    ? getResources().getDimensionPixelSize(R.dimen.account_header_action_item_width_wide)
                    : getResources().getDimensionPixelSize(R.dimen.account_header_action_item_width_narrow);
            int gravity = Gravity.END;

            popupWindow = new ListPopupWindow(this, null, R.attr.actionOverflowMenuStyle, R.style.OverflowMenu);
            popupWindow.setWidth(width);
            popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setAdapter(adapter);
            popupWindow.setAnchorView(anchor);
            popupWindow.setDropDownGravity(gravity);
            popupWindow.setOnItemClickListener(this);
            popupWindow.setOnDismissListener(this);
            popupWindow.show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismissPopupWindow();
        IProfile profile = ((AccountHeaderActionAdapter) parent.getAdapter()).getItems().get(position);
        if (profile instanceof ProfileDrawerItem) {
            Child child = (Child) ((ProfileDrawerItem) profile).getTag();
            presenter.switchChild(child);
        } else if (profile instanceof ProfileSettingDrawerItem) {
            if (id == PROFILE_SETTINGS_EDIT) {
                presenter.editChild();
            } else if (id == PROFILE_SETTINGS_ADD) {
                presenter.addChild();
            } else if (id == PROFILE_SETTINGS_DELETE) {
                presenter.deleteChild();
            }
        }
    }

    @Override
    public void onDismiss() {
        animateSwitcherOut();
        dismissPopupWindow();
    }

    private boolean dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            return true;
        }
        popupWindow = null;
        return false;
    }

    private void setupViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new DayFragment(), getString(R.string.day));
        viewPagerAdapter.addFragment(new WeekFragment(), getString(R.string.week));
        viewPagerAdapter.addFragment(new MonthFragment(), getString(R.string.month));
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(2, false);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateFab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void buildUi() {
        buildHeader();
        buildDrawer();

        setupToolbar();

        View switcherWrapper = accountHeader.getView().findViewById(R.id.material_drawer_account_header_text_switcher_wrapper);
        switcherWrapper.setOnClickListener(this);

        switcherImage = ButterKnife.findById(accountHeader.getView(), R.id.material_drawer_account_header_text_switcher);
        switcherImage.setImageResource(R.drawable.arrow_down_white);
    }

    private void animateSwitcherIn() {
        switcherImage.clearAnimation();
        ViewCompat.animate(switcherImage).rotation(180).start();
    }

    private void animateSwitcherOut() {
        switcherImage.clearAnimation();
        ViewCompat.animate(switcherImage).rotation(0).start();
    }

    private void buildHeader() {
        accountHeader = new CustomAccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .withOnAccountHeaderProfileImageListener(this)
                .withAccountHeader(R.layout.account_header)
                .withHeightRes(R.dimen.account_header_height)
                .withHeaderBackground(ThemeUtils.getColorPrimaryDrawable(this, sex))
                .build();
    }

    private void buildDrawer() {
        updateDrawerItems(false);
        drawerBuilder = new CustomDrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(drawerItems);
        drawer = drawerBuilder.build();
    }

    @OnClick(R.id.addDiaperEvent)
    void onAddDiaperEventClick() {
        presenter.addDiaperEvent();
    }

    @OnClick(R.id.addSleepEvent)
    void onAddSleepEventClick() {
        presenter.addSleepEvent();
    }

    @OnClick(R.id.addFeedEvent)
    void onAddFeedEventClick() {
        presenter.addFeedEvent();
    }

    @OnClick(R.id.addPumpEvent)
    void onAddPumpEventClick() {
        presenter.addPumpEventClick();
    }

    @OnClick(R.id.addOtherEvent)
    void onAddOtherEventClick() {
        presenter.addOtherEventClick();
    }

    private void closeDrawerWithoutAnimation() {
        if (drawerBuilder != null && drawer != null && drawer.isDrawerOpen()) {
            ((CustomDrawerBuilder) drawerBuilder).closeDrawerWithoutAnimation();
        }
    }

    private boolean closeDrawer() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        boolean processed = dismissPopupWindow();
        if (processed) {
            return;
        }

        processed = closeDrawer();
        if (processed) {
            return;
        }

        processed = fabToolbar.hideBar();
        if (processed) {
            return;
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                View menuItemView = findViewById(R.id.menu_filter);
                PopupMenu popupMenu = new PopupMenu(this, menuItemView, Gravity.END, R.attr.actionOverflowMenuStyle, R.style.OverflowMenu);
                popupMenu.inflate(R.menu.filter);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
                break;
            case R.id.menu_overflow:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_diaper:
                showToast(getString(R.string.event_diaper));
                return true;
            case R.id.menu_sleep:
                showToast(getString(R.string.event_sleep));
                return true;
            case R.id.menu_feed:
                showToast(getString(R.string.event_feed));
                return true;
            case R.id.menu_pump:
                showToast(getString(R.string.event_pump));
                return true;
            case R.id.menu_other:
                showToast(getString(R.string.event_other));
                return true;
        }
        return false;
    }

    private void updateFab(int position) {
        CalendarFragment fragment = (CalendarFragment) viewPagerAdapter.getItem(position);
        boolean hasOpenedItems = fragment.getEventAdapter().getSwipeManager().hasOpenedItems();
        if (hasOpenedItems) {
            hideFabBar();
        } else {
            showFab();
        }
    }

    @Override
    public void showFab() {
        fabToolbar.showFab();
    }

    @Override
    public void hideFabBar() {
        fabToolbar.hideFabBar();
    }
}
