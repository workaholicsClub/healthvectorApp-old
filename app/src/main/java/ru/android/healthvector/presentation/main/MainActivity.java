package ru.android.healthvector.presentation.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
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

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
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

import butterknife.ButterKnife;
import icepick.State;
import io.reactivex.Observable;
import ru.android.healthvector.R;
import ru.android.healthvector.app.ChildDiaryApplication;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.presentation.calendar.CalendarFragment;
import ru.android.healthvector.presentation.core.AppPartitionArguments;
import ru.android.healthvector.presentation.core.AppPartitionFragment;
import ru.android.healthvector.presentation.core.BaseMvpActivity;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.core.adapters.swipe.FabController;
import ru.android.healthvector.presentation.core.adapters.swipe.FabShowBarController;
import ru.android.healthvector.presentation.core.widgets.FabToolbar;
import ru.android.healthvector.presentation.development.DevelopmentDiaryFragment;
import ru.android.healthvector.presentation.development.partitions.core.ChartContainer;
import ru.android.healthvector.presentation.exercises.ExercisesFragment;
import ru.android.healthvector.presentation.help.HelpFragment;
import ru.android.healthvector.presentation.main.drawer.AccountHeaderActionAdapter;
import ru.android.healthvector.presentation.main.drawer.CloseMenuButtonClickListener;
import ru.android.healthvector.presentation.main.drawer.CustomAccountHeaderBuilder;
import ru.android.healthvector.presentation.main.drawer.CustomDrawerBuilder;
import ru.android.healthvector.presentation.main.drawer.CustomPrimaryDrawerItem;
import ru.android.healthvector.presentation.medical.MedicalDataFragment;
import ru.android.healthvector.presentation.profile.ProfileEditActivity;
import ru.android.healthvector.presentation.settings.SettingsFragment;
import ru.android.healthvector.utils.strings.StringUtils;
import ru.android.healthvector.utils.strings.TimeUtils;
import ru.android.healthvector.utils.ui.ResourcesUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;

import static android.support.v4.app.FragmentTransaction.TRANSIT_UNSET;

public class MainActivity extends BaseMvpActivity implements MainView,
        Drawer.OnDrawerItemClickListener,
        Drawer.OnDrawerListener,
        AccountHeader.OnAccountHeaderProfileImageListener,
        AccountHeader.OnAccountHeaderSelectionViewClickListener,
        AdapterView.OnItemClickListener,
        PopupWindow.OnDismissListener,
        CloseMenuButtonClickListener,
        View.OnClickListener,
        FabController {
    private static final String TAG_PROGRESS_DIALOG_DELETING_PROFILE = "TAG_PROGRESS_DIALOG_DELETING_PROFILE";

    private static final int PROFILE_SETTINGS_EDIT = 1;
    private static final int PROFILE_SETTINGS_ADD = 2;
    private static final int PROFILE_SETTINGS_DELETE = 3;
    private static final int PROFILE_SETTINGS_USER = 10;

    @IdRes
    private static final int FRAGMENT_CONTAINER_ID = R.id.mainContent;

    private final PrimaryDrawerItem[] drawerItems = new PrimaryDrawerItem[]{
            new CustomPrimaryDrawerItem()
                    .withTag(AppPartition.CALENDAR)
                    .withName(R.string.drawer_item_calendar)
                    .withOnDrawerItemClickListener(this),
            new CustomPrimaryDrawerItem()
                    .withTag(AppPartition.ADD_EVENT)
                    .withName(R.string.drawer_item_add_event)
                    .withOnDrawerItemClickListener(this),
            new CustomPrimaryDrawerItem()
                    .withTag(AppPartition.DEVELOPMENT_DIARY)
                    .withName(R.string.drawer_item_development_diary)
                    .withOnDrawerItemClickListener(this),
            new CustomPrimaryDrawerItem()
                    .withTag(AppPartition.EXERCISES)
                    .withName(R.string.drawer_item_exercises)
                    .withOnDrawerItemClickListener(this),
            new CustomPrimaryDrawerItem()
                    .withTag(AppPartition.MEDICAL_DATA)
                    .withName(R.string.drawer_item_medical_data)
                    .withOnDrawerItemClickListener(this),
            new CustomPrimaryDrawerItem()
                    .withTag(AppPartition.SETTINGS)
                    .withName(R.string.settings)
                    .withOnDrawerItemClickListener(this),
            new CustomPrimaryDrawerItem()
                    .withTag(AppPartition.HELP)
                    .withName(R.string.help)
                    .withOnDrawerItemClickListener(this),
            new CustomPrimaryDrawerItem()
                    .withTag(AppPartition.PRIVACY_POLICY)
                    .withName(R.string.drawer_item_privacy_policy)
                    .withOnDrawerItemClickListener(this)
    };

    @InjectPresenter
    MainPresenter presenter;

    @State
    AppPartition selectedPartition;

    private AccountHeader accountHeader;
    private Drawer drawer;
    private DrawerBuilder drawerBuilder;
    private ImageView switcherImage;
    private ListPopupWindow popupWindow;
    private Runnable navigationCommand;

    private boolean isFirstTime = false;

    public static Intent getIntent(Context context,
                                   @NonNull AppPartition appPartition,
                                   @Nullable Sex sex) {
        return new Intent(context, MainActivity.class)
                .putExtra(ExtraConstants.EXTRA_APP_PARTITION, appPartition.ordinal())
                .putExtra(ExtraConstants.EXTRA_SEX, sex);
    }

    private static AppPartition readAppPartition(@NonNull Intent intent) {
        int index = intent.getIntExtra(ExtraConstants.EXTRA_APP_PARTITION, 0);
        return AppPartition.values()[index];
    }

    private static IProfile mapToProfile(Context context, @NonNull Child child) {
        return new ProfileDrawerItem()
                .withName(child.getName())
                .withEmail(TimeUtils.age(context, child))
                .withNameShown(true)
                .withTag(child)
                .withIdentifier(mapToProfileId(child))
                .withIcon(ResourcesUtils.getChildIconForAccountHeader(context, child));
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
        setContentView(R.layout.activity_with_one_fragment);
        buildUi();
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_navigation_drawer);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        if (accountHeader != null) {
            accountHeader.setBackground(ThemeUtils.getColorPrimaryDrawable(this, getSex()));
        }
        updateDrawerItems(true);
    }

    private void updateDrawerItems(boolean notify) {
        int[] icons = ResourcesUtils.getNavigationDrawerItemResources(getSex());
        for (int i = 0; i < Math.min(icons.length, drawerItems.length); ++i) {
            drawerItems[i].withIcon(icons[i]);
        }
        if (notify && drawer != null) {
            drawer.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void showDeletingProfile(boolean loading) {
        if (loading) {
            showProgress(TAG_PROGRESS_DIALOG_DELETING_PROFILE,
                    getString(R.string.please_wait),
                    getString(R.string.profile_deleting));
        } else {
            hideProgress(TAG_PROGRESS_DIALOG_DELETING_PROFILE);
        }
    }

    @Override
    public void showChildList(@NonNull List<Child> childList) {
        logger.debug("showChildList: " + StringUtils.toString(childList));

        List<IProfile> profiles = new ArrayList<>();
        if (!childList.isEmpty()) {
            profiles.add(new ProfileSettingDrawerItem()
                    .withName(getString(R.string.edit))
                    .withIdentifier(PROFILE_SETTINGS_EDIT));
        }
        profiles.add(new ProfileSettingDrawerItem()
                .withName(getString(R.string.add_profile))
                .withIdentifier(PROFILE_SETTINGS_ADD));
        if (!childList.isEmpty()) {
            profiles.add(new ProfileSettingDrawerItem()
                    .withName(getString(R.string.delete_child))
                    .withIdentifier(PROFILE_SETTINGS_DELETE));
        }

        profiles.addAll(Observable.fromIterable(childList)
                .map(child -> mapToProfile(this, child))
                .toList()
                .blockingGet());

        closeDrawerWithoutAnimation();
        if (accountHeader != null) {
            accountHeader.setProfiles(profiles);
        }
    }

    @Override
    public void showChild(@NonNull Child child) {
        logger.debug("showChild: " + child);
        changeThemeIfNeeded(child);
        if (child.getId() != null && accountHeader != null) {
            accountHeader.setActiveProfile(mapToProfileId(child));
        }
        AppPartition appPartition = selectedPartition == null ? readAppPartition(getIntent()) : selectedPartition;
        openAppPartition(appPartition);
    }

    private void updateTitle(@NonNull AppPartition appPartition, @NonNull Child child) {
        if (appPartition == AppPartition.SETTINGS) {
            hideToolbarLogo();
            setupToolbarTitle(R.string.settings);
            return;
        }
        if (appPartition == AppPartition.HELP) {
            hideToolbarLogo();
            setupToolbarTitle(R.string.help);
            return;
        }
        if (child.getId() == null) {
            hideToolbarLogo();
            setupToolbarTitle(R.string.app_name);
        } else {
            setupToolbarLogo(ResourcesUtils.getChildIconForToolbar(this, child));
            setupToolbarTitle(child.getName());
        }
    }

    @Override
    public void navigateToProfileAddFirstTime() {
        AppPartition appPartition = readAppPartition(getIntent());
        if (appPartition == AppPartition.CALENDAR) {
            isFirstTime = true;
            navigateToProfileAdd();
        }
    }

    @Override
    public void navigateToProfileAdd() {
        Intent intent = ProfileEditActivity.getIntent(this, null);
        startActivity(intent);
    }

    @Override
    public void navigateToProfileEdit(@NonNull Child child) {
        if (child == Child.NULL) {
            navigateToProfileAdd();
            return;
        }
        Intent intent = ProfileEditActivity.getIntent(this, child);
        startActivity(intent);
    }

    @Override
    public void showDeleteChildConfirmation(@NonNull Child child) {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(getString(R.string.delete_child_confirmation_dialog_title, child.getName()))
                .setMessage(R.string.delete_child_confirmation_dialog_text)
                .setPositiveButton(R.string.delete,
                        (dialog, which) -> presenter.forceDeleteChild(child))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void childDeleted(@NonNull Child child) {
    }

    @Override
    public void navigateToCalendar(@NonNull AppPartitionArguments arguments) {
        openAppPartition(AppPartition.CALENDAR, arguments);
    }

    @Override
    public void showBar() {
        getSupportFragmentManager().executePendingTransactions();
        FabShowBarController fabController = findShowBarController();
        if (fabController != null) {
            fabController.showBar();
        }
    }

    @Override
    public void navigateToDevelopmentDiary(@NonNull AppPartitionArguments arguments) {
        openAppPartition(AppPartition.DEVELOPMENT_DIARY, arguments);
    }

    @Override
    public void navigateToExercises(@NonNull AppPartitionArguments arguments) {
        openAppPartition(AppPartition.EXERCISES, arguments);
    }

    @Override
    public void navigateToMedicalData(@NonNull AppPartitionArguments arguments) {
        openAppPartition(AppPartition.MEDICAL_DATA, arguments);
    }

    @Override
    public void navigateToSettings(@NonNull AppPartitionArguments arguments) {
        openAppPartition(AppPartition.SETTINGS, arguments);
    }

    @Override
    public void navigateToHelp(@NonNull AppPartitionArguments arguments) {
        openAppPartition(AppPartition.HELP, arguments);
    }

    public void openPrivacyPolicy() {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("https://vectordev.org/mobile_app/"));
        startActivity(intent);
    }

    private void openAppPartition(@NonNull AppPartition appPartition,
                                  @NonNull AppPartitionArguments arguments) {
        updateTitle(appPartition, arguments.getChild());

        selectedPartition = appPartition;

        String tag = appPartition.toString();

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ExtraConstants.EXTRA_APP_PARTITION_ARGUMENTS, arguments);
            fragment = createAppPartition(appPartition);
            fragment.setArguments(bundle);
            logger.debug("fragment cache: create new fragment: " + fragment);
        } else {
            logger.debug("fragment cache: show fragment: " + fragment);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(TRANSIT_UNSET)
                .replace(FRAGMENT_CONTAINER_ID, fragment, tag)
                .addToBackStack(null)
                .commit();

        String screenName = tag;
        boolean skipTrackCalendarWhenProfileOpen = screenName.equals("CALENDAR") && isFirstTime;

        if (!skipTrackCalendarWhenProfileOpen) {
            trackScreen(screenName);
        }
        else {
            isFirstTime = false;
        }
    }

    private Fragment createAppPartition(@NonNull AppPartition appPartition) {
        switch (appPartition) {
            case CALENDAR:
                return new CalendarFragment();
            case DEVELOPMENT_DIARY:
                return new DevelopmentDiaryFragment();
            case EXERCISES:
                return new ExercisesFragment();
            case MEDICAL_DATA:
                return new MedicalDataFragment();
            case SETTINGS:
                return new SettingsFragment();
            case HELP:
                return new HelpFragment();
        }
        throw new IllegalArgumentException("Unsupported app partition");
    }

    @Override
    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
        presenter.editChild();
        return true;
    }

    @Override
    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
        return false;
    }

    @Override
    public boolean onClick(View view, IProfile profile) {
        presenter.editChild();
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == switcherImage && accountHeader != null) {
            dismissPopupWindow();
            animateSwitcherIn();
            List<IProfile> profiles = new ArrayList<>(accountHeader.getProfiles());
            profiles.remove(accountHeader.getActiveProfile());
            ListAdapter adapter = new AccountHeaderActionAdapter(this, profiles, this);
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
    public void closeMenu() {
        dismissPopupWindow();
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

    private void openAppPartition(@NonNull AppPartition appPartition) {
        if (drawer != null) {
            int position = appPartition.ordinal() + 1;
            drawer.setSelectionAtPosition(position, false);
        }
        switch (appPartition) {
            case CALENDAR:
                presenter.openCalendar();
                break;
            case DEVELOPMENT_DIARY:
                presenter.openDevelopmentDiary();
                break;
            case EXERCISES:
                presenter.openExercises();
                break;
            case MEDICAL_DATA:
                presenter.openMedicalData();
                break;
            case SETTINGS:
                presenter.openSettings();
                break;
            case HELP:
                presenter.openHelp();
                break;
        }
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        AppPartition appPartition = (AppPartition) drawerItem.getTag();
        if (selectedPartition == appPartition) {
            return false;
        }
        switch (appPartition) {
            case CALENDAR:
                navigationCommand = () -> presenter.openCalendar();
                return false;
            case ADD_EVENT:
                navigationCommand = () -> presenter.openCalendarAndAddEventBar();
                return false;
            case DEVELOPMENT_DIARY:
                navigationCommand = () -> presenter.openDevelopmentDiary();
                return false;
            case EXERCISES:
                navigationCommand = () -> presenter.openExercises();
                return false;
            case MEDICAL_DATA:
                navigationCommand = () -> presenter.openMedicalData();
                return false;
            case SETTINGS:
                navigationCommand = () -> presenter.openSettings();
                return false;
            case HELP:
                navigationCommand = () -> presenter.openHelp();
                return false;
            case PRIVACY_POLICY:
                openPrivacyPolicy();
                return false;
            default:
                return false;
        }
    }

    @Override
    public void onDrawerSlide(View view, float v) {
    }

    @Override
    public void onDrawerOpened(View view) {
    }

    @Override
    public void onDrawerClosed(View view) {
        if (navigationCommand != null) {
            navigationCommand.run();
            navigationCommand = null;
        }
    }

    private void buildUi() {
        if (getToolbar() == null) {
            return;
        }

        buildHeader();
        buildDrawer(getToolbar());

        setupToolbar(getToolbar());

        switcherImage = ButterKnife.findById(accountHeader.getView(), R.id.material_drawer_account_header_text_switcher);
        switcherImage.setImageResource(R.drawable.arrow_down_white);
        switcherImage.setOnClickListener(this);
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
                .withOnAccountHeaderSelectionViewClickListener(this)
                .withOnAccountHeaderProfileImageListener(this)
                .withAccountHeader(R.layout.account_header)
                .withHeightRes(R.dimen.account_header_height)
                .withHeaderBackground(ThemeUtils.getColorPrimaryDrawable(this, getSex()))
                .build();
    }

    private void buildDrawer(@NonNull Toolbar toolbar) {
        updateDrawerItems(false);
        drawerBuilder = new CustomDrawerBuilder()
                .withActivity(this)
                .withOnDrawerListener(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems((IDrawerItem[]) drawerItems);
        drawer = drawerBuilder.build();
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

        processed = hideBar();
        if (processed) {
            return;
        }

        processed = openRootPartition();
        if (processed) {
            return;
        }

        finish();
    }

    private boolean openRootPartition() {
        if (selectedPartition == null) {
            return false;
        }
        switch (selectedPartition) {
            case CALENDAR:
                return false;
            case DEVELOPMENT_DIARY:
            case EXERCISES:
            case MEDICAL_DATA:
            case SETTINGS:
            case HELP:
                openAppPartition(AppPartition.CALENDAR);
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (selectedPartition == AppPartition.CALENDAR
                || selectedPartition == AppPartition.MEDICAL_DATA) {
            removeToolbarMargin();
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.filter, menu);
            return true;
        } else if (selectedPartition == AppPartition.DEVELOPMENT_DIARY) {
            AppPartitionFragment partition = findAppPartition();
            if (partition instanceof DevelopmentDiaryFragment) {
                AppPartitionFragment page = ((DevelopmentDiaryFragment) partition).getSelectedPage();
                if (page instanceof ChartContainer) {
                    removeToolbarMargin();
                    MenuInflater inflater = getMenuInflater();
                    inflater.inflate(R.menu.chart, menu);
                    return true;
                }
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (selectedPartition == AppPartition.CALENDAR
                || selectedPartition == AppPartition.MEDICAL_DATA) {
            switch (item.getItemId()) {
                case R.id.menu_filter:
                    showFilter();
                    return true;
            }
        } else if (selectedPartition == AppPartition.DEVELOPMENT_DIARY) {
            switch (item.getItemId()) {
                case R.id.menu_chart:
                    showChart();
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFilter() {
        AppPartitionFragment fragment = findAppPartition();
        if (fragment != null) {
            fragment.showFilter();
        } else {
            logger.error("no partition found");
        }
    }

    private void showChart() {
        AppPartitionFragment partition = findAppPartition();
        if (partition instanceof DevelopmentDiaryFragment) {
            AppPartitionFragment page = ((DevelopmentDiaryFragment) partition).getSelectedPage();
            if (page instanceof ChartContainer) {
                ((ChartContainer) page).showChart();
            }
        }
    }

    @Override
    public void showFab() {
        FabController fabController = findFabController();
        if (fabController != null) {
            fabController.showFab();
        }
    }

    @Override
    public boolean hideBar() {
        FabController fabController = findFabController();
        return fabController != null && fabController.hideBar();
    }

    @Override
    public void hideBarWithoutAnimation() {
        FabController fabController = findFabController();
        if (fabController != null) {
            fabController.hideBarWithoutAnimation();
        }
    }

    @Override
    public void hideFabBar() {
        FabController fabController = findFabController();
        if (fabController != null) {
            fabController.hideFabBar();
        }
    }

    @Nullable
    private FabController findFabController() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(FRAGMENT_CONTAINER_ID);
        return fragment instanceof FabController ? (FabController) fragment : null;
    }

    @Nullable
    private FabShowBarController findShowBarController() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("CALENDAR");
        return (FabShowBarController) fragment;
    }

    @Nullable
    private AppPartitionFragment findAppPartition() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(FRAGMENT_CONTAINER_ID);
        return fragment instanceof AppPartitionFragment ? (AppPartitionFragment) fragment : null;
    }
}
