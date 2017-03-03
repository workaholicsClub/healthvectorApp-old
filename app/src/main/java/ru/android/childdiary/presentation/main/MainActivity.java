package ru.android.childdiary.presentation.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
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
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
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
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.main.calendar.DayFragment;
import ru.android.childdiary.presentation.main.calendar.MonthFragment;
import ru.android.childdiary.presentation.main.calendar.ViewPagerAdapter;
import ru.android.childdiary.presentation.main.calendar.WeekFragment;
import ru.android.childdiary.presentation.main.drawer.AccountHeaderActionAdapter;
import ru.android.childdiary.presentation.main.drawer.CustomAccountHeaderBuilder;
import ru.android.childdiary.presentation.main.drawer.CustomDrawerBuilder;
import ru.android.childdiary.presentation.profile.edit.ProfileEditActivity;
import ru.android.childdiary.presentation.profile.review.ProfileReviewActivity;
import ru.android.childdiary.utils.StringUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class MainActivity extends BaseMvpActivity<MainPresenter> implements MainView,
        Drawer.OnDrawerItemClickListener,
        AccountHeader.OnAccountHeaderProfileImageListener,
        AdapterView.OnItemClickListener,
        PopupWindow.OnDismissListener,
        View.OnClickListener,
        PopupMenu.OnMenuItemClickListener {
    private static final int REQUEST_EDIT = 1;
    private static final int REQUEST_ADD = 2;
    private static final int REQUEST_REVIEW = 3;

    private static final int PROFILE_SETTINGS_EDIT = 1;
    private static final int PROFILE_SETTINGS_ADD = 2;
    private static final int PROFILE_SETTINGS_DELETE = 3;
    private static final int PROFILE_SETTINGS_USER = 10;

    @InjectPresenter
    MainPresenter presenter;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private AccountHeader accountHeader;
    private Drawer drawer;
    private DrawerBuilder drawerBuilder;
    private ImageView switcherImage;
    private ListPopupWindow popupWindow;

    public static Intent getIntent(Context context, @Nullable Sex sex) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_SEX, sex);
        return intent;
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
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DayFragment(), getString(R.string.day));
        adapter.addFragment(new WeekFragment(), getString(R.string.week));
        adapter.addFragment(new MonthFragment(), getString(R.string.month));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(2);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.white_transparent), ContextCompat.getColor(this, R.color.white));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));
        tabLayout.setSelectedTabIndicatorHeight(getResources().getDimensionPixelSize(R.dimen.selected_tab_indicator_height));
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar.setNavigationIcon(R.drawable.toolbar_menu);
    }

    @Override
    protected void themeChangedCustom() {
        tabLayout.setBackgroundColor(ThemeUtils.getColorPrimary(this, sex));
        if (accountHeader != null) {
            accountHeader.setBackground(ThemeUtils.getColorPrimaryDrawable(this, sex));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD) {
            presenter.requestActiveChild();
        }
    }

    @Override
    public void showChildList(List<Child> childList) {
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

        profiles.addAll(Stream.of(childList).map(this::mapToProfile).collect(Collectors.toList()));

        closeDrawerWithoutAnimation();
        buildUi(profiles);

        presenter.requestActiveChild();
    }

    @Override
    public void setActive(@Nullable Child child) {
        changeThemeIfNeeded(child);
        if (child == null) {
            getSupportActionBar().setTitle(R.string.app_name);
        } else {
            getSupportActionBar().setTitle(child.getName());
            if (accountHeader != null) {
                accountHeader.setActiveProfile(mapToProfileId(child));
            }
        }
    }

    private IProfile mapToProfile(@NonNull Child child) {
        return new ProfileDrawerItem()
                .withName(child.getName())
                .withEmail(StringUtils.age(this, child))
                .withNameShown(true)
                .withTag(child)
                .withIdentifier(mapToProfileId(child))
                .withIcon(ThemeUtils.getChildIcon(this, child));
    }

    @Override
    public void addChild() {
        navigateToProfileAdd();
    }

    @Override
    public void editChild(@NonNull Child child) {
        navigateToProfileEdit(child);
    }

    @Override
    public void reviewChild(@NonNull Child child) {
        navigateToProfileReview(child);
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
            List<IProfile> profiles = accountHeader.getProfiles();
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
        if (id > PROFILE_SETTINGS_USER) {
            presenter.toggleChild(mapToChildId(id));
        } else if (id == PROFILE_SETTINGS_EDIT) {
            presenter.editChild();
        } else if (id == PROFILE_SETTINGS_ADD) {
            presenter.addChild();
        } else if (id == PROFILE_SETTINGS_DELETE) {
            new AlertDialog.Builder(this, ThemeUtils.getThemeDialog(sex))
                    .setTitle(R.string.remove_child_confirmation_title)
                    .setMessage(R.string.remove_child_confirmation_text)
                    .setPositiveButton(R.string.Yes,
                            (DialogInterface dialog, int which) -> presenter.deleteChild())
                    .setNegativeButton(R.string.Cancel, null)
                    .show();
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

    private void buildUi(List<IProfile> profiles) {
        buildHeader(profiles);
        buildDrawer();

        setupToolbar();

        View container = accountHeader.getView().findViewById(R.id.material_drawer_account_header);
        setUnclickable(container);

        View switcherWrapper = accountHeader.getView().findViewById(R.id.material_drawer_account_header_text_switcher_wrapper);
        switcherWrapper.setOnClickListener(this);

        switcherImage = ButterKnife.findById(accountHeader.getView(), R.id.material_drawer_account_header_text_switcher);
        switcherImage.setImageResource(R.drawable.switcher);
    }

    private void setUnclickable(View view) {
        view.setOnClickListener(null);
        view.setClickable(false);
        view.setFocusable(false);
        view.setFocusableInTouchMode(false);
    }

    private void animateSwitcherIn() {
        switcherImage.clearAnimation();
        ViewCompat.animate(switcherImage).rotation(180).start();
    }

    private void animateSwitcherOut() {
        switcherImage.clearAnimation();
        ViewCompat.animate(switcherImage).rotation(0).start();
    }

    private void buildHeader(List<IProfile> profiles) {
        accountHeader = new CustomAccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .withOnAccountHeaderProfileImageListener(this)
                .withAccountHeader(R.layout.account_header)
                .withHeightRes(R.dimen.account_header_height)
                .withHeaderBackground(ThemeUtils.getColorPrimaryDrawable(this, sex))
                .addProfiles(profiles.toArray(new IProfile[profiles.size()]))
                .build();
    }

    private void buildDrawer() {
        drawerBuilder = new CustomDrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_calendar)
                                .withIcon(FontAwesome.Icon.faw_cart_plus)
                                .withOnDrawerItemClickListener(this),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_development_diary)
                                .withIcon(FontAwesome.Icon.faw_database)
                                .withOnDrawerItemClickListener(this),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_exercises)
                                .withIcon(FontAwesome.Icon.faw_github)
                                .withOnDrawerItemClickListener(this),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_medical_data)
                                .withIcon(FontAwesome.Icon.faw_amazon)
                                .withOnDrawerItemClickListener(this),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_settings)
                                .withIcon(FontAwesome.Icon.faw_cog)
                                .withOnDrawerItemClickListener(this),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_help)
                                .withIcon(FontAwesome.Icon.faw_question)
                                .withOnDrawerItemClickListener(this)
                );
        drawer = drawerBuilder.build();
    }

    private void closeDrawerWithoutAnimation() {
        if (drawerBuilder != null && drawer != null && drawer.isDrawerOpen()) {
            ((CustomDrawerBuilder) drawerBuilder).closeDrawerWithoutAnimation();
        }
    }

    @Override
    public void onBackPressed() {
        boolean dismissed = dismissPopupWindow();
        if (!dismissed) {
            if (drawer != null && drawer.isDrawerOpen()) {
                drawer.closeDrawer();
            } else {
                super.onBackPressed();
            }
        }
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

    private void navigateToProfileEdit(@NonNull Child child) {
        Intent intent = ProfileEditActivity.getIntent(this, child);
        startActivityForResult(intent, REQUEST_EDIT);
    }

    private void navigateToProfileAdd() {
        Intent intent = ProfileEditActivity.getIntent(this, null);
        startActivityForResult(intent, REQUEST_ADD);
    }

    private void navigateToProfileReview(@NonNull Child child) {
        Intent intent = ProfileReviewActivity.getIntent(this, child);
        startActivityForResult(intent, REQUEST_REVIEW);
    }
}
